package org.nbone.lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Dict Enum
 */
public class Dict<T> {

    //private Integer id;
    private T code;
    private String name;
    private String description;

    private String type;


    public Dict() {
    }

    public Dict(T code, String name) {
        this.code = code;
        this.name = name;
    }

    public Dict(T code, String name,String type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }
    public Dict(T code, String name, String description, String type) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.type = type;
    }


    public static <K> List<Dict<K>> from(Map<K, String> map) {
        if (map == null || map.isEmpty()) {
            return Collections.emptyList();
        }
        List<Dict<K>> dictList = new ArrayList<Dict<K>>(map.size());
        for (Map.Entry<?, String> entry : map.entrySet()) {
            dictList.add(new Dict(entry.getKey(), entry.getValue()));
        }
        return dictList;
    }

    public T getCode() {
        return code;
    }

    public void setCode(T code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

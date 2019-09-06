package org.nbone.persistence.mapper;

import org.nbone.persistence.annotation.MappedBy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author chenyicheng
 * @version 1.0
 * @since 2019/6/27
 */
public class MapperUtils {

    public static List<Field> getExtFields(Class<?> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        List<Field> extFields = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(MappedBy.class)) {
                if (extFields == null) {
                    extFields = new ArrayList<>();
                }
               /* MappedBy mappedBy = field.getAnnotation(MappedBy.class);
                String fieldName    = mappedBy.name();
                QueryType queryType = mappedBy.queryType();*/
                extFields.add(field);
            }
        }
        return extFields;
    }

    public static Map<String,Field> getExtFieldsMap(Class<?> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        Map<String,Field> extFields = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(MappedBy.class)) {
                if (extFields == null) {
                    extFields = new HashMap<String,Field>();
                }
               /* MappedBy mappedBy = field.getAnnotation(MappedBy.class);
                String fieldName    = mappedBy.name();
                QueryType queryType = mappedBy.queryType();*/
                extFields.put(field.getName(),field);
            }
        }
        return extFields;
    }


}

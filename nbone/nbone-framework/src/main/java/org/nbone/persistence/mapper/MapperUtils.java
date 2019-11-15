package org.nbone.persistence.mapper;

import org.nbone.persistence.annotation.FieldProperty;
import org.nbone.persistence.annotation.MappedBy;
import org.nbone.persistence.annotation.QueryOperation;
import org.nbone.persistence.enums.QueryType;
import org.springframework.util.StringUtils;

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

    public static Map<String, QueryOperation> getExtFieldsMap(Class<?> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        Map<String,QueryOperation> extFields = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(MappedBy.class)) {
                if (extFields == null) {
                    extFields = new HashMap<String,QueryOperation>();
                }
                MappedBy mappedBy = field.getAnnotation(MappedBy.class);
                String fieldName    = mappedBy.name();
                QueryType queryType = mappedBy.queryType();
                QueryOperation queryOperation = new QueryOperation(fieldName,field.getType(),queryType);
                extFields.put(field.getName(),queryOperation);
            }
        }
        return extFields;
    }

    public static Map<String, QueryOperation> getQueryOperationMap(Class<?> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        Map<String, QueryOperation> operationMap = new HashMap<String, QueryOperation>();
        for (Field field : fields) {
            FieldProperty fieldProperty = field.getAnnotation(FieldProperty.class);
            MappedBy mappedBy = field.getAnnotation(MappedBy.class);
            if (fieldProperty != null) {
                String fieldName = field.getName();
                String mappingName = fieldProperty.name();
                if (StringUtils.isEmpty(mappingName)) {
                    mappingName = fieldName;
                }
                QueryType queryType = fieldProperty.queryType();
                QueryOperation queryOperation = new QueryOperation(mappingName, field.getType(), queryType);
                operationMap.put(fieldName, queryOperation);
            } else if (mappedBy != null) {
                String mappingName = mappedBy.name();
                QueryType queryType = mappedBy.queryType();
                QueryOperation queryOperation = new QueryOperation(mappingName, field.getType(), queryType);
                operationMap.put(field.getName(), queryOperation);
            }
        }
        return operationMap;
    }


}

package org.nbone.persistence;

import lombok.Data;
import org.nbone.mvc.domain.GroupQuery;
import org.nbone.mvc.domain.RequestQuery;
import org.nbone.persistence.annotation.FieldLevel;
import org.nbone.persistence.util.SqlUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author thinking
 * @version 1.0
 */
@Data
public class SqlConfig {

    public static final int PrimaryMode = 0;
    public static final int MiddleMode = 1;
    public static final int HighMode = 2;

    public static final int ObjectMode = 3;

    public static final SqlConfig EMPTY = new SqlConfig(-1);

    private String aliasName = " tempA";

    /**
     * 高级时使用
     */
    private SqlOperations sqlOperations = new SqlOperations();

    /**
     * 实体类型
     */
    private Class<?> entityClass;

    /**
     * 返回映射类型 默认采用 entityClass
     */
    private Class<?> mappingClass;

    /**
     * 字段名称数组,用于按需返回字段
     */
    private String[] fieldNames;

    /**
     * 字段名称数组,用于排除返回字段
     */
    private String[] excludeFields;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * @MapKey 将返回的List 转换成Map默认使用主键作为map key
     */
    private String mapKey;

    /**
     * @MapKey map key Type (可为空, 为空时使用目标实体字段类型)
     */
    private Class<?> mapKeyType;
    /**
     * map key 对应的值 可为空默认entity
     */
    private String mapValueName;

    /**
     * 扩展字段名称数组
     */
    private String[] extFields;

    private boolean usedExtField = true;
    /**
     * 默认采用Java字段模式
     */
    private boolean dbFieldMode = false;
    /**
     * 查询是否去重
     */
    private boolean distinct = false;
    /**
     * 字段级别
     */
    private FieldLevel fieldLevel;
    /**
     * 分页查询 限制结果集大小 排序
     */
    private RequestQuery requestQuery;
    /**
     * 分组查询
     */
    private GroupQuery groupQuery;
    /**
     * 第一个条件语句
     */
    private String firstCondition;

    /**
     * 第一个操作语句 , 和 firstCondition 参数冲突
     */
    private SqlOperation firstOperation;
    /**
     * 追加条件语句
     */
    private String condition;
    /**
     * where 条件字段
     */
    private String[] conditionFields;
    /**
     * 忽略的 where 条件字段
     */
    private List<String> ignoreFields;
    /**
     * where 之后的语句 追加[增加]查询条件 或者 group by/order by 子句
     */
    private String[] afterWhere;

    /**
     * 排序字段 id DESC
     */
    private String orderBy;
    /**
     * 此实体Bean 引用其他的实体Bean列表
     */
    private List<Class<?>> entityReferences;
    /**
     * 默认为初级
     */
    private int sqlMode = PrimaryMode;

    /**
     * 中级时使用
     */
    //number String key/value
    private Map<String, String> inNumStrMap;
    private Map<String, String> notinNumStrMap;
    //String key
    private String[] inStringFields;
    private Map<String, String> inStringFieldsMap;

    private List dtField;
    /**
     * 初中高级通用
     */
    private String[] orderFieldASC;
    private String[] orderFieldDESC;

    public SqlConfig() {
        this.sqlMode = PrimaryMode;
    }

    public SqlConfig(String aliasName) {
        this.aliasName = aliasName;
        this.sqlMode = PrimaryMode;
    }

    public SqlConfig(int mode) {
        this.sqlMode = mode;
    }

    /**
     * MiddleMode Constructor
     *
     * @param inStringFields
     */
    public SqlConfig(String[] inStringFields) {
        this.inStringFields = inStringFields;
        this.inStringFieldsMap = stringArray2Map(inStringFields);

        this.sqlMode = MiddleMode;
    }

    /**
     * HighMode Constructor
     *
     * @param sqlOperationMap
     */
    public SqlConfig(Map<String, SqlOperation> sqlOperationMap) {
        this.addAll(sqlOperationMap);
        this.sqlMode = HighMode;
    }

    public SqlConfig(SqlOperations sqlOperations) {
        this.sqlOperations = sqlOperations;
        this.sqlMode = HighMode;
    }

    public SqlConfig(String fieldName, String operType) {
        addOperation(fieldName, operType);
    }

    public SqlConfig(String fieldName, String operType, String fieldName1, String operationType) {
        addOperation(fieldName, operType).addOperation(fieldName1, operationType);
    }

    //-------------------------------------------------------------------------
    //factory method
    //-------------------------------------------------------------------------
    public static SqlConfig getSqlConfig(int mode) {
        return new SqlConfig(mode);
    }

    public static SqlConfig getHighMode() {
        return new SqlConfig((Map<String, SqlOperation>) null);
    }

    public static SqlConfig getMiddleMode(String[] inStringFields) {
        return new SqlConfig(inStringFields);
    }


    //-------------------------------------------------------------------------
    //特殊方法
    //-------------------------------------------------------------------------
    public Map<String, String> stringArray2Map(String[] strArray) {
        Map<String, String> resultMap = new HashMap<String, String>();
        if (strArray != null) {
            for (int i = 0; i < strArray.length; i++) {
                String did = strArray[i];
                resultMap.put(did, did);
            }
        }
        //XXX to MiddleMode
        this.sqlMode = MiddleMode;
        return resultMap;
    }

    public static String andInNumber(String dbFieldName, boolean isIn, String values) {
        return SqlUtils.inNumber("and", dbFieldName, isIn, values);
    }

    public static String dbInNumber(String andOr, String dbFieldName, boolean isIn, String values) {
        return SqlUtils.inNumber(andOr, dbFieldName, isIn, values);
    }

    public static String in(String andOr, String dbFieldName, Class<?> nameType, Object values, boolean isIn) {
        StringBuilder sql = SqlUtils.in(andOr, dbFieldName, nameType, values, isIn);
        return sql != null ? sql.toString() : null;
    }

    public static String dbBetween(String andOr, String dbFieldName, String beginFieldName, String endFieldName, boolean is) {
        StringBuilder sql = SqlUtils.between(andOr, dbFieldName, beginFieldName, endFieldName, is);
        return sql != null ? sql.toString() : null;
    }

    public static String dbBetween(String andOr, String dbFieldName, Object[] values, boolean is) {
        StringBuilder sql = SqlUtils.between(andOr, dbFieldName, values, is);
        return sql != null ? sql.toString() : null;
    }


    public static SqlConfig builder() {
        return new SqlConfig();
    }
    public static SqlConfig builder(int mode) {
        return new SqlConfig(mode);
    }

    public SqlConfig distinct(boolean distinct) {
        this.distinct = distinct;
        return this;
    }

    public <T> SqlConfig withEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
        return this;
    }

    public <T> SqlConfig withMappingClass(Class<T> mappingClass) {
        this.mappingClass = mappingClass;
        return this;
    }

    public SqlConfig withJavaFieldMode() {
        this.dbFieldMode = false;
        return this;
    }

    public SqlConfig withDBFieldMode() {
        this.dbFieldMode = true;
        return this;
    }

    public SqlConfig withFieldNames(String[] fieldNames) {
        this.fieldNames = fieldNames;
        return this;
    }

    public SqlConfig fieldNames(String[] fieldNames) {
        return withFieldNames(fieldNames);
    }

    public SqlConfig fieldNames(String fieldName, String fieldName1) {
        return withFieldNames(new String[]{fieldName, fieldName1});
    }

    public SqlConfig fieldNames(String fieldName, String fieldName1, String fieldName2) {
        return withFieldNames(new String[]{fieldName, fieldName1, fieldName2});
    }

    public SqlConfig excludeFields(String[] excludeFields) {
        this.excludeFields = excludeFields;
        return this;
    }
    public SqlConfig excludeFields(String fieldName) {
        this.excludeFields = new String[]{fieldName};
        return this;
    }
    public SqlConfig excludeFields(String fieldName, String fieldName1) {
        this.excludeFields = new String[]{fieldName,fieldName1};
        return this;
    }

    public SqlConfig tableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public SqlConfig mapKey(String mapKey) {
        this.mapKey = mapKey;
        return this;
    }
    public SqlConfig mapKeyType(Class<?> mapKeyType) {
        this.mapKeyType = mapKeyType;
        return this;
    }
    public SqlConfig mapValueName(String valueName) {
        this.mapValueName = valueName;
        return this;
    }

    public SqlConfig extFields(String[] extFields) {
        this.extFields = extFields;
        return this;
    }

    public SqlConfig extFields(String fieldName) {
        this.extFields = new String[]{fieldName};
        return this;
    }

    public SqlConfig extFields(String fieldName, String fieldName1) {
        this.extFields = new String[]{fieldName, fieldName1};
        return this;
    }

    public SqlConfig usedExtField(boolean usedExtField) {
        this.usedExtField = usedExtField;
        return this;
    }


    public SqlConfig fieldLevel(FieldLevel fieldLevel) {
        this.fieldLevel = fieldLevel;
        return this;
    }

    public SqlConfig requestQuery(RequestQuery requestQuery) {
        this.requestQuery = requestQuery;
        return this;
    }

    public SqlConfig groupQuery(GroupQuery groupQuery) {
        this.groupQuery = groupQuery;
        return this;
    }

    public SqlConfig firstCondition(String firstCondition) {
        if (firstOperation != null) {
            throw new IllegalArgumentException("already set firstOperation=" + firstOperation.getFieldName());
        }
        this.firstCondition = firstCondition;
        return this;
    }

    public SqlConfig firstCondition(SqlOperation sqlOperation) {
        if (firstCondition != null) {
            throw new IllegalArgumentException("already set firstCondition=" + firstCondition);
        }
        this.firstOperation = sqlOperation;
        return this;
    }

    public SqlConfig condition(String condition) {
        this.condition = condition;
        return this;
    }
    public SqlConfig appendCondition(String appendCondition) {
        if(this.condition == null){
            this.condition = appendCondition;
        }else {
            this.condition =  this.condition + " " + appendCondition;
        }
        return this;
    }

    public SqlConfig conditionFields(String[] conditionFields) {
        this.conditionFields = conditionFields;
        return this;
    }

    public SqlConfig conditionFields(String conditionField) {
        this.conditionFields = new String[]{conditionField};
        return this;
    }

    public SqlConfig ignoreFields(String... ignoreFields) {
        this.ignoreFields = Arrays.asList(ignoreFields);
        return this;
    }

    public SqlConfig afterWhere(String[] afterWhere) {
        this.afterWhere = afterWhere;
        return this;
    }

    public SqlConfig afterWhere(String afterWhere) {
        this.afterWhere = new String[]{afterWhere};
        return this;
    }

    public SqlConfig orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }


    /**
     * @param fieldName
     * @param isIn
     * @param values    object[] / List / number strings -> 1,2,3,4,5 / strings list-> 693fe72810,f84f369553
     * @return
     */
    public SqlConfig in(String fieldName, boolean isIn, Object values) {
        this.sqlMode = HighMode;
        this.sqlOperations.addOperationIn(fieldName, isIn, values);
        return this;
    }

    /**
     * @param fieldName
     * @param isIn
     * @param values    "1,2,3,4,5"
     * @return
     */
    public SqlConfig inNumber(String fieldName, boolean isIn, String values) {
        return in(fieldName, isIn, values);
    }

    public SqlConfig between(String fieldName, Object beginValue, Object endValue) {
        this.sqlMode = HighMode;
        this.sqlOperations.addOperationBetween(fieldName, beginValue, endValue);
        return this;
    }

    public static Map<String, String> operation(String fieldName, String operationType) {
        return operation(fieldName, operationType, null, null);
    }

    public static Map<String, String> operation(String name, String operationType, String name1, String operationType1) {
        Map<String, String> operationMap = new HashMap<String, String>();
        if (name != null && operationType != null) {
            operationMap.put(name, operationType);
        }
        if (name1 != null && operationType1 != null) {
            operationMap.put(name1, operationType1);
        }
        return operationMap;
    }

    public static Map<String, String> operation(String name, String operation, String name1, String operation1, String... ops) {
        Map<String, String> operationMap = operation(name, operation, name1, operation1);
        if (ops.length > 0 && ops.length % 2 == 0) {
            for (int i = 0; i < ops.length; i = i + 2) {
                operationMap.put(ops[i], ops[i + 1]);
            }
        }
        return operationMap;
    }


    public SqlConfig addOperation(String fieldName, String operType) {
        this.sqlOperations.addOperation(fieldName, operType);
        return this;
    }
    public SqlConfig addOperation(String fieldName, String operationType, Object value) {
        this.sqlOperations.addOperation(fieldName, operationType,value);
        return this;
    }

    public SqlConfig addOperation(String name, String operationType, String name1, String operationType1) {
        if (name != null && operationType != null) {
            addOperation(name, operationType);
        }
        if (name1 != null && operationType1 != null) {
            addOperation(name1, operationType1);
        }
        return this;
    }

    public SqlConfig addOperationBetween(String fieldName, Object beginValue, Object endValue) {
        this.sqlOperations.addOperationBetween(fieldName, beginValue, endValue);
        return this;
    }
    public SqlConfig addOperationBetween(String fieldName, Object[] values) {
        this.sqlOperations.addOperationBetween(fieldName, values[0], values[1]);
        return this;
    }
    /**
     * @param fieldName entity fieldName
     * @param isIn      true in false not in
     * @param values    Collection / Object[] / string list
     * @return
     */
    public SqlConfig addOperationIn(String fieldName, boolean isIn, Object values) {
        this.sqlOperations.addOperationIn(fieldName, isIn, values);
        return this;
    }

    public SqlConfig addOperation(SqlOperation sqlOperation) {
        this.sqlOperations.addOperation(sqlOperation);
        return this;
    }

    public SqlConfig addAll(SqlOperation[] sqlOperations) {
        this.sqlOperations.addAll(sqlOperations);
        return this;
    }

    public SqlConfig addAll(Map<String, SqlOperation> sqlOperationMap) {
        this.sqlOperations.addAll(sqlOperationMap);
        return this;
    }

    public SqlConfig addOperationMapString(Map<String, String> sqlOperationMap) {
        this.sqlOperations.addMapString(sqlOperationMap);
        return this;
    }

    public void setSqlOperations(SqlOperations sqlOperations) {
        this.sqlOperations = sqlOperations;
    }

    public SqlConfig remove(SqlOperation sqlOperation) {
        this.sqlOperations.remove(sqlOperation);
        return this;
    }

    //-----------------------------Range property------------------------------------
    public SqlConfig addSqlOperationRange(String leftName, String rightName, Object value) {
        this.sqlOperations.addOperationRange(leftName,rightName,value);
        return this;
    }

    //-------------------------------------------------------------------------
    //setter / getter
    //-------------------------------------------------------------------------
    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }


    public Map<String, SqlOperation> getSqlOperationAsMap() {
        return this.sqlOperations.getSqlOperationAsMap();
    }

    public SqlOperation getSqlOperation(String fieldName) {
        return this.sqlOperations.getSqlOperation(fieldName);
    }

    public Class<?> getMappingClass() {
        if (mappingClass != null) {
            return mappingClass;
        }
        return entityClass;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(String[] fieldNames) {
        this.fieldNames = fieldNames;
    }

    public boolean isDbFieldMode() {
        return dbFieldMode;
    }

    public void setDbFieldMode(boolean dbFieldMode) {
        this.dbFieldMode = dbFieldMode;
    }


    public void setInStringFields(String[] inStringFields) {
        this.inStringFields = inStringFields;
        //XXX: stringArray2Map
        this.inStringFieldsMap = this.stringArray2Map(inStringFields);
        this.setSqlMode(MiddleMode);
    }

    /**
     * 当使用jdbc时 使用表字段名称
     *
     * @param orderFieldASC
     */
    public void setOrderFieldsASC(String... orderFieldASC) {
        this.orderFieldASC = orderFieldASC;
    }

    /**
     * 当使用jdbc时 使用表字段名称
     *
     * @param orderFieldDESC
     */
    public void setOrderFieldsDESC(String... orderFieldDESC) {
        this.orderFieldDESC = orderFieldDESC;
    }


}

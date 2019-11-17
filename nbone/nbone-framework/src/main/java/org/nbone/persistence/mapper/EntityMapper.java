package org.nbone.persistence.mapper;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.nbone.persistence.annotation.FieldLevel;
import org.nbone.persistence.annotation.QueryOperation;
import org.nbone.persistence.entity.DynamicTableName;
import org.nbone.persistence.exception.PrimaryKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * 描述java对象的数据库映射信息（数据库表的映射、字段的映射）<br>
 * 
 * @author thinking
 * @since 2015-12-12
 * @see javax.persistence.Entity
 * @see javax.persistence.Table
 * @see org.springframework.jdbc.core.RowMapper
 * 
 */
public class EntityMapper<T> {

	private static final Logger logger = LoggerFactory.getLogger(EntityMapper.class);

	public final static String  S = "s";
	public final static String BETWEEN  = "Between";
	public final static String _BETWEEN = "_between";
    /**
     * 当使用注解定义实体Bean时使用
     */
    private Annotation tableAnnotation;
    /**
     * 数据库表主键映射列表：（单个字段的唯一键）（几个字段组合起来的唯一键）primaryKeys
     */
    private List<FieldMapper> primaryKeyFields = new ArrayList<FieldMapper>(1);
    private String[] primaryKeys;

    /**
     * 数据库表的名称
     */
    private String  tableName;
    /**
     * 映射实体Bean class  entityName
     */
    private Class<T> entityClass;
	/**
	 * 启用 select *
	 */
    private boolean selectStar;
    /**
     * 以数据库字段为Key, 数据库表字段映射列表 最好使用LinkedHashMap保证key的顺序
     */
    private Map<String, FieldMapper> fieldMappers;

	/**
	 * 以JavaBean字段为Key , 最好使用LinkedHashMap保证key的顺序
	 */
	private Map<String, FieldMapper> propertyMappers;

	/**
	 * 实体类中的扩展字段 用于in 查询 和 between 查询
	 */
	private Map<String, QueryOperation> extFields;
	/**
	 * 当使用其他类(非映射实体类)实例的bean查询时使用
	 */
	private Map<Class<?>, Map<String, QueryOperation>> queryOperations;
    /**
     * Field Property is Load(查询级别)
     */
    private boolean fieldPropertyLoad;
    /**
     * 数据库列名称数组
     */
    private String[] columnNames;
    /**
     * id,name,age
     */
    private String   commaDelimitedColumns;
    
    /**
     * 根据Id查询 使用?占位符
     */
    private String selectSqlWithId;

    /**
     * Spring Jdbc
     */
    private RowMapper<T> rowMapper;
    
    

	public EntityMapper(Class<T> entityClazz) {
		this(entityClazz, 10);
	}
	
	/**
	 * 设定预定的容量  XXX： thinking 2016-08-02
	 * @param entityClass
	 * @param fieldInitialCapacity
	 */
	public EntityMapper(Class<T> entityClass, int fieldInitialCapacity) {
		fieldInitialCapacity = fieldInitialCapacity +5;
		
		if(fieldInitialCapacity < 10){
			fieldInitialCapacity = 16;
		}
		
		this.entityClass = entityClass;
		this.fieldMappers =  new LinkedHashMap<String, FieldMapper>(fieldInitialCapacity);
		this.propertyMappers = new LinkedHashMap<String, FieldMapper>(fieldInitialCapacity);
	}
	
	public Annotation getTableAnnotation() {
		return tableAnnotation;
	}

	public void setTableAnnotation(Annotation tableAnnotation) {
		this.tableAnnotation = tableAnnotation;
	}

	public String[] getPrimaryKeys() {
		if(primaryKeys == null){
			List<FieldMapper> primaryKeyFields = getPrimaryKeyFields();
			primaryKeys = new String[primaryKeyFields.size()];
			for (int i = 0; i < primaryKeyFields.size(); i++) {
				primaryKeys[i] = primaryKeyFields.get(i).getDbFieldName();
			}
		}
		return  primaryKeys;
	}
	public String getPrimaryKey() {
		FieldMapper primaryKey = getPrimaryKeyFieldMapper();
		if (primaryKey == null) {
			return null;
		}
		return primaryKey.getDbFieldName();
	}

	public FieldMapper getPrimaryKeyFieldMapper() {
		List<FieldMapper> fieldMappers = getPrimaryKeyFields();
		if (fieldMappers != null && fieldMappers.size() > 0) {
			return fieldMappers.get(0);
		}
		return null;
	}

	public String getPrimaryKeyProperty() {
		FieldMapper fieldMapper = getPrimaryKeyFieldMapper();
		return fieldMapper != null ? fieldMapper.getFieldName() : null;
	}

	
	/**
	 * 没有加载主键时加载主键字段列表和名称
	 * @return
	 */
	public List<FieldMapper> getPrimaryKeyFields() {
		if(primaryKeyFields == null || primaryKeyFields.size() == 0){
			for (Map.Entry<String,FieldMapper> entry : fieldMappers.entrySet()) {
				FieldMapper fieldMapper =  entry.getValue();
				if(fieldMapper.isPrimaryKey()){
					primaryKeyFields.add(fieldMapper);
				}
			}
		}
		return primaryKeyFields;
	}

	public void setPrimaryKeyFields(List<FieldMapper> primaryKeyFields) {
		this.primaryKeyFields = primaryKeyFields;
	}

	/**
	 * 映射实体Bean的全名称(包含包名称)
	 * @return
	 */
	public String getEntityName() {
		return entityClass.getName();
	}
    
	/**
	 * <li> 优先使用 dbTableName设置的表名称
	 * <li> 次之使用 注解映射的表名称
	 * <li> 次之最后使用 entityClazz的短名称
	 * @return TableName
	 */
	private String getTableName() {
		//当没有设置dbTableName时 使用注解映射的名称
		//1.
		if(tableName == null){
			//2.
			if(tableAnnotation == null){
				tableName = getTableName(entityClass);
				return tableName;
			}
			//3.
			else if(tableAnnotation instanceof Table){
				Table table  = (Table) tableAnnotation;
				tableName = table.name();
			}
		}
		
		return tableName;
	}

	public String getTableName(Object object) {
        String tableName;
        if (object == null) {
            return getTableName();
        }
        if (object instanceof DynamicTableName) {
            tableName = ((DynamicTableName) object).getTableName();
            if (StringUtils.isEmpty(tableName)) {
                String message = "class " + object.getClass() + " implements interface DynamicTableName method getTableName return not Empty";
                logger.error(message);
                //throw new IllegalArgumentException(message);
                //use default
                tableName = getTableName();
            }
        } else if (object instanceof Map) {
            tableName = (String) ((Map) object).get(DynamicTableName.TABLE_NAME_KEY);
            if (StringUtils.isEmpty(tableName)) {
                tableName = getTableName();
            }

        } else {
            tableName = getTableName();
        }
        return tableName;
	}
	public static String getTableName(Class<?> entityClass) {
		Annotation[] classAnnotations = entityClass.getDeclaredAnnotations();
		if (classAnnotations.length == 0) {
			throw new RuntimeException("Class " + entityClass.getName() + " has no '@Entity' annotation, can't build 'EntityMapper'.");
		}

		Entity entity = entityClass.getAnnotation(Entity.class);
		if (entity == null) {
			throw new RuntimeException("Class " + entityClass.getName() + " has no '@Entity' annotation, "
					+ "which has the database table information," + "can't build 'EntityMapper'.");
		}
		String tableName = entity.name();
		Table table = entityClass.getAnnotation(Table.class);
		if (table != null) {
			tableName = StringUtils.hasLength(table.name()) ? table.name() : tableName;
		}
		return StringUtils.hasLength(tableName) ? tableName : entityClass.getSimpleName();
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public boolean isSelectStar() {
		return selectStar;
	}

	public void setSelectStar(boolean selectStar) {
		this.selectStar = selectStar;
	}

	/**
     * @see #fieldMappers
     */
	public Map<String, FieldMapper> getFieldMappers() {
		
		return fieldMappers;
	}

	public void setFieldMappers(Map<String, FieldMapper> fieldMappers) {
		this.fieldMappers.putAll(fieldMappers);
		this.propertyMappers.putAll(fieldMappers);

	}
	
	public FieldMapper getFieldMapper(String dbFieldName) {
		return fieldMappers.get(dbFieldName);
	}

	public FieldMapper getFieldMapperByProperty(String propertyName) {
		if (propertyName == null) {
			return null;
		}
		return propertyMappers.get(propertyName);
	}
	
	/**
	 * 根据数据库字段名称查询Java字段名称
	 * @param dbFieldName
	 * @return
	 */
	public String getFieldName(String dbFieldName) {
		return fieldMappers.get(dbFieldName).getFieldName();
	}
	/**
	 * 根据Java字段名称查询数据库字段名称
	 * @param fieldName
	 * @return
	 */
	public String getDbFieldName(String fieldName) {
		FieldMapper fieldMapper = getFieldMapperByProperty(fieldName);
		if(fieldMapper == null){
			return null;
		}
		return fieldMapper.getDbFieldName();
	
	}
	
	/**
	 * 
	 * @param dbFieldName
	 * @param fieldMapper
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public EntityMapper addFieldMapper(String dbFieldName, FieldMapper fieldMapper) {
		this.fieldMappers.put(dbFieldName, fieldMapper);
		this.propertyMappers.put(fieldMapper.getFieldName(),fieldMapper);
		return this;
	}
	
	public PropertyDescriptor getPropertyDescriptor(String fieldName) {
		return propertyMappers.get(fieldName).getPropertyDescriptor();
	}

	public Collection<QueryOperation> getExtFields() {
		if (extFields == null) {
			return null;
		}
		return extFields.values();
	}
	public Map<String,QueryOperation> getExtFieldsMap() {
		return extFields;
	}
	public Map<String, QueryOperation> getExtFieldsMap(Class<?> beanClass) {
		if (beanClass == null || beanClass == entityClass) {
			return extFields;
		}
		if (queryOperations == null) {
			queryOperations = new HashMap<>(4);
			Map<String, QueryOperation> operationMap = MapperUtils.getQueryOperationMap(beanClass);
			queryOperations.put(beanClass, operationMap);
			return operationMap;
		}
		if (queryOperations.containsKey(beanClass)) {
			return queryOperations.get(beanClass);
		}
		Map<String, QueryOperation> operationMap = MapperUtils.getQueryOperationMap(beanClass);
		queryOperations.put(beanClass, operationMap);
		return operationMap;
	}
	public QueryOperation getExtField(Class<?> beanClass,String fieldName) {
		Map<String, QueryOperation> queryOperationMap = getExtFieldsMap(beanClass);
		if(queryOperationMap == null){
			return null;
		}
		return queryOperationMap.get(fieldName);
	}

	public QueryOperation getExtField(String fieldName) {
		if (extFields == null) {
			return null;
		}
		return extFields.get(fieldName);
	}

	public void setExtFields(Map<String,QueryOperation> extFields) {
		this.extFields = extFields;
	}

	public void addExtFields(QueryOperation extField) {
		if(this.extFields == null){
			this.extFields = new HashMap<String,QueryOperation>();
		}
		extFields.put(extField.getName(),extField);
	}

	public Collection<FieldMapper> getFieldMapperList() {
		return fieldMappers.values();
	}
	

	public boolean isFieldPropertyLoad() {
		return fieldPropertyLoad;
	}

	public void setFieldPropertyLoad(boolean fieldPropertyLoad) {
		this.fieldPropertyLoad = fieldPropertyLoad;
	}

	/**
     * @see #columnNames
     */
	public String[] getColumnNames() {
		if(columnNames == null || columnNames.length == 0){
			Set<String> keys = fieldMappers.keySet();
			columnNames =  new String[keys.size()];
			columnNames = keys.toArray(columnNames);
			return columnNames;
		}
		return columnNames;
	}


	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

    /**
     * @see #commaDelimitedColumns
     */
	public String getCommaDelimitedColumns() {
		if(commaDelimitedColumns == null){
			String[] names = getColumnNames();
			commaDelimitedColumns = StringUtils.arrayToCommaDelimitedString(names);
			return commaDelimitedColumns;
		}
		
		return commaDelimitedColumns;
	}

	public void setCommaDelimitedColumns(String commaDelimitedColumns) {
		this.commaDelimitedColumns = commaDelimitedColumns;
	}

	/**
	 * 查询全部sql
	 * @return
	 */
	public StringBuilder getSelectAllSql(Object object,boolean distinct,String tableName) {
		StringBuilder selectAllSql = new StringBuilder();
		selectAllSql.append("SELECT ");
		if (distinct) {
			selectAllSql.append("DISTINCT ");
		}
		selectAllSql.append(selectStar ? "*" : this.getCommaDelimitedColumns()).append(" FROM ");
		selectAllSql.append(StringUtils.hasLength(tableName) ? tableName : this.getTableName(object));
		return selectAllSql;
	}
	/**
	 * 根字段返回查询sql 用于按需字段查询
	 * @param fieldNames
	 * @param isDbFieldName true为数据库字段名称,false 为Java字段名称
	 * @return
	 */
	public StringBuilder getSelectAllSql(Object object,String[] fieldNames,boolean isDbFieldName,boolean distinct) {
		if(fieldNames == null || fieldNames.length == 0){
			return getSelectAllSql(object,distinct,null);
		}
	    StringBuilder selectAllSql = new StringBuilder("SELECT ");
		if(distinct){
			selectAllSql.append("DISTINCT ");
		}
		if (isDbFieldName) {
			selectAllSql.append(StringUtils.arrayToDelimitedString(fieldNames, ","));
			
		}else{
			for (int i = 0; i < fieldNames.length; i++) {
				String dbFieldName = getDbFieldName(fieldNames[i]);

				if(dbFieldName == null || dbFieldName.length() == 0){
					throw  new IllegalArgumentException(fieldNames[i]+" java property mapping dbFieldName is null.");
				}

				if(i > 0){
					selectAllSql.append(",");
				}
				selectAllSql.append(dbFieldName);
			}
		}
		
	    selectAllSql.append(" FROM ").append(this.getTableName(object));
		return selectAllSql;
	}
	
	public StringBuilder getSelectAllSql(Object object,FieldLevel fieldLevel,boolean distinct) {
		if(fieldLevel == null || fieldLevel == FieldLevel.ALL || !fieldPropertyLoad){
			return getSelectAllSql(object,distinct,null);
		}
		// query column
		StringBuilder selectAllSql = new StringBuilder("SELECT ");
		if(distinct){
			selectAllSql.append("DISTINCT ");
		}
		int count = 0;
		int inputLevel = fieldLevel.getId();
		for (Map.Entry<String,FieldMapper> entry : fieldMappers.entrySet()) {
			FieldMapper fieldMapper =  entry.getValue();
			int level = fieldMapper.getFieldLevel().getId();
			if(inputLevel >= level){
				String dbFieldName = fieldMapper.getDbFieldName();
				if(count > 0){
					selectAllSql.append(",");
				}
				selectAllSql.append(dbFieldName);

				count ++;
			}
		}

		selectAllSql.append(" FROM ").append(this.getTableName(object));
		return selectAllSql;
	}

	/**
	 *   select id,count(id) countNum  from User
	 */
	public StringBuilder getGroupSelectAllSql(Object object,String queryColumns) {
		// query column
		StringBuilder selectSql = new StringBuilder();
		selectSql.append("SELECT ").append(queryColumns).append(" FROM ").append(this.getTableName(object));
		return  selectSql;
	}

   /**
    * @return
    */
	public StringBuilder getDeleteAllSql(Object object,String tableName) {
        StringBuilder deleteAllSql = new StringBuilder();
		String name = StringUtils.hasLength(tableName) ? tableName : this.getTableName(object);
		deleteAllSql.append("delete from ").append(name);
		return deleteAllSql;
	}

	public StringBuilder getCountSql(Object object) {
		StringBuilder countSql = new StringBuilder();
		countSql.append("select count(1) from ").append(this.getTableName(object));
		return countSql;
	}

	
	public String getSelectSqlWithId() {
		if(selectSqlWithId == null){
			StringBuilder querySql= getSelectAllSql(null,false,null);
			String  primaryKey=  getPrimaryKey();
			if(primaryKey == null){
				throw new PrimaryKeyException("table name "+this.tableName +" not have primaryKey");
			}
			querySql.append(" where ").append(primaryKey).append(" = ?");
			
			this.selectSqlWithId = querySql.toString();
		}
		return selectSqlWithId;
	}

	public String getDeleteSqlWithId(Object object) {
		StringBuilder deleteSql = getDeleteAllSql(object,null);
        String  primaryKey=  getPrimaryKey();
        if(primaryKey == null){
            throw new PrimaryKeyException("table name "+this.tableName +" not have primaryKey");
        }

		deleteSql.append(" where ").append(primaryKey).append(" = ?");

		return deleteSql.toString();
	}

	public  RowMapper<T> getRowMapper() {
		return  rowMapper;
	}
	

	public void setRowMapper(RowMapper<T> rowMapper) {
		this.rowMapper = rowMapper;
	}


	/**
	 * @see javax.persistence.Transient
	 */
	public static class ExcludeTransientFieldFilter implements ReflectionUtils.FieldFilter {

		private final Class<? extends Annotation> annotationType = Transient.class;

		private final String[] IGNORE_PROPERTY = {"class", "serialVersionUID"};

		//AnnotationUtils.getAnnotation(field, annotationType) == null;
		// 返回 true 是需要使用的属性
		@Override
        public boolean matches(Field field) {
            if (isIgnoreProperties(field)) {
                return false;
            }
            //Exclude static field
            if (Modifier.isStatic(field.getModifiers())) {
                return false;
            }
            return field.getAnnotation(annotationType) == null;
        }

		private boolean isIgnoreProperties(Field field) {
			for (String ignore : IGNORE_PROPERTY) {
				if (ignore.equals(field.getName())) {
					return true;
				}
			}
			return false;
		}

		public String getDescription() {
			return String.format("Annotation filter for %s", annotationType.getName());
		}
	}
	

}

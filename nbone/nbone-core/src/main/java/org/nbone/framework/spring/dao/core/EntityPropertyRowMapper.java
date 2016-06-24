package org.nbone.framework.spring.dao.core;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.persistence.mapper.DbMappingBuilder;
import org.nbone.persistence.mapper.FieldMapper;
import org.nbone.persistence.mapper.TableMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;
/**
 * <p> spring BeanPropertyRowMapper 方便但效率不高故重新实现RowMapper
 * 
 * @author thinking
 * @version 1.0 
 * @see org.springframework.jdbc.core.BeanPropertyRowMapper
 * @see org.springframework.jdbc.core.RowMapper
 * @since spring 2.5
 * 
 */
public class EntityPropertyRowMapper<T> implements RowMapper<T> {
	protected final Log logger = LogFactory.getLog(getClass());
	
	/** The class we are mapping to */
	private Class<T> mappedClass;
	
	/** Map of the fields we provide mapping for */
	private Map<String, PropertyDescriptor> mappedFields;
	
	
	private TableMapper<T> tableMapper;
	
	
	public EntityPropertyRowMapper(Class<T> mappedClass) {
		this(DbMappingBuilder.getEntityMapper(mappedClass));
	}
	
	public EntityPropertyRowMapper(TableMapper<T> tableMapper) {
		this.tableMapper  = tableMapper;
		this.mappedClass = tableMapper.getEntityClazz();
		this.mappedFields = tableMapper.getMappedPropertys();
		this.initialize(mappedClass);
	}
	
	
	/**
	 * Initialize the mapping metadata for the given class.
	 * @param mappedClass the mapped class.
	 */
	protected void initialize(Class<T> mappedClass) {
	}
	
	
	@Override
	public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
		Assert.state(this.mappedClass != null, "Mapped class was not specified");
		T mappedObject = BeanUtils.instantiate(this.mappedClass);
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);

		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		
		for (int index = 1; index <= columnCount; index++) {
			String column = JdbcUtils.lookupColumnName(rsmd, index);
			String dbFieldName = column.replaceAll(" ", "");
			FieldMapper fieldMapper = this.tableMapper.getFieldMapper(dbFieldName);
			PropertyDescriptor pd;
			if (fieldMapper != null && (pd=fieldMapper.getPropertyDescriptor()) != null ) {
				try {
					Object value = getColumnValue(rs, index, pd);
					if (logger.isDebugEnabled() && rowNumber == 0) {
						logger.debug("Mapping column '" + column + "' to property '" +pd.getName() + "' of type " + pd.getPropertyType());
					}
					try {
						bw.setPropertyValue(pd.getName(), value);
					}
					catch (TypeMismatchException e) {
						if (value == null) {
							logger.debug("Intercepted TypeMismatchException for row " + rowNumber +
									" and column '" + column + "' with value " + value +
									" when setting property '" + pd.getName() + "' of type " + pd.getPropertyType() +
									" on object: " + mappedObject);
						}
						else {
							throw e;
						}
					}
				}
				catch (NotWritablePropertyException ex) {
					throw new DataRetrievalFailureException("Unable to map column " + column + " to property " + pd.getName(), ex);
				}
			}
		}
		
		
		return mappedObject;
	}
	
	
	protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
		return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
	}

}

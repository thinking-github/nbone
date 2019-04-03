package org.nbone.framework.spring.dao.simple;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

/**
 * 
 * @author thinking
 * @version 1.0 
 * @see org.springframework.jdbc.core.simple.AbstractJdbcInsert
 * @see org.springframework.jdbc.core.simple.SimpleJdbcInsert
 * @see spring 2.5
 */
public class SimpleJdbcInsert extends AbstractJdbcInsert implements SimpleJdbcInsertOperations {
	
	/**
	 * Constructor that takes one parameter with the JDBC DataSource to use when creating the
	 * JdbcTemplate.
	 * @param dataSource the {@code DataSource} to use
	 * @see org.springframework.jdbc.core.JdbcTemplate#setDataSource
	 */
	public SimpleJdbcInsert(DataSource dataSource) {
		super(dataSource);
	}

	/**
	 * Alternative Constructor that takes one parameter with the JdbcTemplate to be used.
	 * @param jdbcTemplate the {@code JdbcTemplate} to use
	 * @see org.springframework.jdbc.core.JdbcTemplate#setDataSource
	 */
	public SimpleJdbcInsert(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
	}


	@Override
	public SimpleJdbcInsert withTableName(String tableName) {
		setTableName(tableName);
		return this;
	}

	@Override
	public SimpleJdbcInsert withSchemaName(String schemaName) {
		setSchemaName(schemaName);
		return this;
	}

	@Override
	public SimpleJdbcInsert withCatalogName(String catalogName) {
		setCatalogName(catalogName);
		return this;
	}

	@Override
	public SimpleJdbcInsert usingColumns(String... columnNames) {
		setColumnNames(Arrays.asList(columnNames));
		return this;
	}

	@Override
	public SimpleJdbcInsert usingGeneratedKeyColumns(String... columnNames) {
		setGeneratedKeyNames(columnNames);
		return this;
	}

	@Override
	public SimpleJdbcInsertOperations withoutTableColumnMetaDataAccess() {
		setAccessTableColumnMetaData(false);
		return this;
	}

	@Override
	public SimpleJdbcInsertOperations includeSynonymsForTableColumnMetaData() {
		setOverrideIncludeSynonymsDefault(true);
		return this;
	}


	/**
	 * spring 5 废除
	 * @param nativeJdbcExtractor
	 * @return
	 */
	@Override
	public SimpleJdbcInsertOperations useNativeJdbcExtractorForMetaData(NativeJdbcExtractor nativeJdbcExtractor) {
		setNativeJdbcExtractor(nativeJdbcExtractor);
		return this;
	}
	@Override
	public int execute(Map<String, ?> args) {
		return doExecute(args);
	}

	@Override
	public int execute(SqlParameterSource parameterSource) {
		return doExecute(parameterSource);
	}

	@Override
	public Number executeAndReturnKey(Map<String, ?> args) {
		return doExecuteAndReturnKey(args);
	}

	@Override
	public Number executeAndReturnKey(SqlParameterSource parameterSource) {
		return doExecuteAndReturnKey(parameterSource);
	}

	@Override
	public KeyHolder executeAndReturnKeyHolder(Map<String, ?> args) {
		return doExecuteAndReturnKeyHolder(args);
	}

	@Override
	public KeyHolder executeAndReturnKeyHolder(SqlParameterSource parameterSource) {
		return doExecuteAndReturnKeyHolder(parameterSource);
	}

	@Override
	public int[] executeBatch(Map<String, ?>... batch) {
		return doExecuteBatch(batch);
	}

	@Override
	public int[] executeBatch(SqlParameterSource... batch) {
		return doExecuteBatch(batch);
	}


	//------------------------------------
	// XXX: thinking new add
	public int dbExecuteBatch(SqlParameterSource... batch) {
		checkCompiled();
		// insert into () values(),(),(),()
		String sql = getInsertString();
		StringBuilder insertSqls =  new StringBuilder(sql);
		String append = sql.substring(sql.lastIndexOf('('));
		for (int i = 0; i < batch.length -1 ; i++) {
			insertSqls.append(",").append(append);
		}

		List<List<Object>> batchValues = new ArrayList<List<Object>>(batch.length);
		for (SqlParameterSource parameterSource : batch) {
			batchValues.add(matchInParameterValuesWithInsertColumns(parameterSource));
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Executing statement " + insertSqls + " with batch of size: " + batchValues.size());
		}

		int row  = getJdbcTemplate().update(insertSqls.toString(),
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						setParameterValues(ps,batchValues,getInsertTypes());
					}
				});

		logger.debug("Executing statement with result of size: " + row);
		return row;
	}

	private void setParameterValues(PreparedStatement preparedStatement, List<List<Object>> values, int... columnTypes) throws SQLException {
		int colIndex = 0;
		for (List<Object> beanValues : values) {
			for (Object value : beanValues) {
				colIndex++;
				if (columnTypes == null || colIndex > columnTypes.length) {
					StatementCreatorUtils.setParameterValue(preparedStatement, colIndex, SqlTypeValue.TYPE_UNKNOWN, value);
				}
				else {
					StatementCreatorUtils.setParameterValue(preparedStatement, colIndex, columnTypes[colIndex - 1], value);
				}
			}

		}
	}




}

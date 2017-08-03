package org.nbone.framework.spring.dao.simple;

import java.util.Arrays;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
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


}

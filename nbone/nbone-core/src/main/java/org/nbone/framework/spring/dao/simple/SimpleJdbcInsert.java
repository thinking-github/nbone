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

	public SimpleJdbcInsert(DataSource dataSource) {
		super(dataSource);
	}

	public SimpleJdbcInsert(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
	}

	public SimpleJdbcInsert withTableName(String tableName) {
		setTableName(tableName);
		return this;
	}

	public SimpleJdbcInsert withSchemaName(String schemaName) {
		setSchemaName(schemaName);
		return this;
	}

	public SimpleJdbcInsert withCatalogName(String catalogName) {
		setCatalogName(catalogName);
		return this;
	}

	public SimpleJdbcInsert usingColumns(String... columnNames) {
		setColumnNames(Arrays.asList(columnNames));
		return this;
	}

	public SimpleJdbcInsert usingGeneratedKeyColumns(String... columnNames) {
		setGeneratedKeyNames(columnNames);
		return this;
	}

	public SimpleJdbcInsertOperations withoutTableColumnMetaDataAccess() {
		setAccessTableColumnMetaData(false);
		return this;
	}

	public SimpleJdbcInsertOperations includeSynonymsForTableColumnMetaData() {
		setOverrideIncludeSynonymsDefault(true);
		return this;
	}

	public SimpleJdbcInsertOperations useNativeJdbcExtractorForMetaData(NativeJdbcExtractor nativeJdbcExtractor) {
		setNativeJdbcExtractor(nativeJdbcExtractor);
		return this;
	}

	public int execute(Map<String, Object> args) {
		return doExecute(args);
	}

	public int execute(SqlParameterSource parameterSource) {
		return doExecute(parameterSource);
	}

	public Number executeAndReturnKey(Map<String, Object> args) {
		return doExecuteAndReturnKey(args);
	}

	public Number executeAndReturnKey(SqlParameterSource parameterSource) {
		return doExecuteAndReturnKey(parameterSource);
	}

	public KeyHolder executeAndReturnKeyHolder(Map<String, Object> args) {
		return doExecuteAndReturnKeyHolder(args);
	}

	public KeyHolder executeAndReturnKeyHolder(SqlParameterSource parameterSource) {
		return doExecuteAndReturnKeyHolder(parameterSource);
	}

	public int[] executeBatch(Map<String, Object>[] batch) {
		return doExecuteBatch(batch);
	}

	public int[] executeBatch(SqlParameterSource[] batch) {
		return doExecuteBatch(batch);
	}

}

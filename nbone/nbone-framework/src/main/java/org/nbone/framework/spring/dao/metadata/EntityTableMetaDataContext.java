package org.nbone.framework.spring.dao.metadata;

import org.nbone.framework.spring.dao.core.EntityPropertySqlParameterSource;
import org.springframework.jdbc.core.metadata.TableMetaDataContext;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p> spring TableMetaDataContext 方便但效率不高故重新实现 TableMetaDataContext
 *
 * @author thinking
 * @version 1.0
 * @see org.springframework.jdbc.core.metadata.TableMetaDataContext
 * @since spring 2.5
 */
public class EntityTableMetaDataContext extends TableMetaDataContext {


    //private boolean initialize;
    private boolean processMetaData;

    public EntityTableMetaDataContext() {
    }

    public EntityTableMetaDataContext(String tableName) {
        this.setTableName(tableName);
    }

    @Override
    public void processMetaData(DataSource dataSource, List<String> declaredColumns, String[] generatedKeyNames) {
        if (this.processMetaData) {
            return;
        }
        super.processMetaData(dataSource, declaredColumns, generatedKeyNames);
        this.processMetaData = true;
    }

    /*@Override
    protected List<String> reconcileColumnsToUse(List<String> declaredColumns, String[] generatedKeyNames) {

        return super.reconcileColumnsToUse(declaredColumns, generatedKeyNames);
    }*/

    @Override
    public List<Object> matchInParameterValuesWithInsertColumns(SqlParameterSource parameterSource) {
        /**
         * 优先使用 EntityPropertySqlParameterSource
         */
        if (parameterSource instanceof EntityPropertySqlParameterSource) {
            List<Object> values = new ArrayList<Object>();

            List<String> tableColumns = this.getTableColumns();
            if (tableColumns == null || tableColumns.size() == 0) {
                logger.error(">>>>> table " + this.getTableName() + " columns is Empty.");
            }

            for (String column : this.getTableColumns()) {
                if (parameterSource.hasValue(column)) {
                    values.add(SqlParameterSourceUtils.getTypedValue(parameterSource, column));
                } else {
                    values.add(null);
                }
            }
            return values;
        }


        return super.matchInParameterValuesWithInsertColumns(parameterSource);
    }


    @Override
    public List<Object> matchInParameterValuesWithInsertColumns(Map<String, ?> inParameters) {
        return super.matchInParameterValuesWithInsertColumns(inParameters);
    }


}

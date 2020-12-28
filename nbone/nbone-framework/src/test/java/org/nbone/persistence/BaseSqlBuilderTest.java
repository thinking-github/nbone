package org.nbone.persistence;

import java.util.Date;
import java.util.Map;

import org.junit.Test;
import org.nbone.lang.MathOperation;
import org.nbone.persistence.annotation.FieldLevel;
import org.nbone.persistence.criterion.QueryOperator;
import org.nbone.persistence.enums.JdbcFrameWork;
import org.nbone.persistence.model.SqlModel;
import org.nbone.persistence.util.SqlUtils;

public class BaseSqlBuilderTest {
	
	private SqlBuilder sqlBuilder = new BaseSqlBuilder(JdbcFrameWork.SPRING_JDBC) {
		
	};
	@Test
	public void testbuildSelectSql(){
		SqlConfig sqlConfig = new SqlConfig(-1);
		sqlConfig.setFieldLevel(FieldLevel.ALL);
		SqlModel<Object> sqlModel1 = sqlBuilder.selectSql(getTsProjectDTO(),sqlConfig);
		System.out.println(sqlModel1.getSql());
	}
	
	//@Test
	public void testbuildSimpleSelectSql(){
		SqlModel<Object> sqlModel = sqlBuilder.selectSql(getTsProjectDTO(),new SqlConfig(SqlConfig.PrimaryMode));
		System.out.println(sqlModel.getSql());
		
	}
	//@Test
	public void testbuildSelectSqlByIds(){
		
		SqlModel<TsProjectDTO> sqlModel = sqlBuilder.selectSqlByIds(TsProjectDTO.class, new Object[]{1030,1037},null);
		System.out.println(sqlModel.getSql());
		
	}
	
	//@Test
	public void testbuildDeleteSqlByIds(){
		
		SqlModel<TsProjectDTO> sqlModel = sqlBuilder.deleteSqlByIds(TsProjectDTO.class, new Long[]{1030L,1037L},null);
		System.out.println(sqlModel.getSql());
		
	}
	
	//@Test
	public void testbuildDeleteSql(){
		TsProjectDTO object = new TsProjectDTO();
		object.setCode("2");
		
		SqlModel<Object> sqlModel = sqlBuilder.deleteSqlByEntity(object, false,null);
		String sqld = sqlModel.getEntityMapper().getDeleteSqlWithId(null);
		String sqls = sqlModel.getEntityMapper().getSelectSqlWithId();
		System.out.println(sqlModel.getSql());
		System.out.println(sqld);
		System.out.println(sqls);
		
	}
	
	//@Test
	public void testSQlUtils(){
		System.out.println(SqlUtils.stringSplit2In("id", "1,2", int.class));
		System.out.println(SqlUtils.list2In("id", new Object[]{1,2}));
		
	}
	//@Test
	public void testbuildUpdateSql(){
		TsProjectDTO object = getTsProjectDTO();
		object.setId(9L);
		SqlModel<Object> sqlModel = sqlBuilder.updateSql(object,null,null);
		System.out.println(sqlModel.getSql());
	}
	
	//@Test
	public void testbuildObjectModeSelectSql(){
		TsProjectDTO object = getTsProjectDTO();
		object.setId(9L);
		SqlConfig sqlConfig  = new SqlConfig();
		sqlConfig.addOperation("createDt", QueryOperator.is_not_null);
		sqlConfig.addOperationIn("id",true, new Object[]{1,2,3});
		sqlConfig.addOperationBetween("modifyDt", new Date(), new Date());
		
		sqlConfig.addSqlOperationRange("createDt","modifyDt", new Date());
		
		sqlConfig.setOrderFieldsASC("id","modify_dt");
		sqlConfig.setOrderFieldsDESC("create_dt");
		SqlModel<Map<String, ?>> sqlModel = sqlBuilder.objectModeSelectSql(object, sqlConfig);
		
		System.out.println(sqlModel.getSql());
	}
	
	/**
	 * 自定义字段查询
	 */
	//@Test
	public void testCustomFieldBuildSelectSql(){
		TsProjectDTO object = getTsProjectDTO();
		object.setId(9L);
		object.setSort(2);
		
		SqlConfig sqlConfig = new SqlConfig(-1);
		sqlConfig.setFieldNames(new String[]{"id","name"});
		sqlConfig.setDbFieldMode(false);
		
		SqlModel<Object> sqlModel= sqlBuilder.selectSql(object, sqlConfig);
		
		SqlModel<Object> sqlModel2 = sqlBuilder.updateMathOperationSql(object,null, MathOperation.ADD,null,null);
		
		SqlModel<Object> sqlModel3 = sqlBuilder.countSql(object,sqlConfig);
		
		System.out.println(sqlModel.getSql());
		System.out.println(sqlModel2.getSql());
	}
	
	
	public TsProjectDTO getTsProjectDTO(){
		TsProjectDTO object = new TsProjectDTO();
		object.setTypeId(1);
		object.setName("thinking00000000");
		object.setCode("222");
		object.setIsEnable(true);
		return object;
	}

}

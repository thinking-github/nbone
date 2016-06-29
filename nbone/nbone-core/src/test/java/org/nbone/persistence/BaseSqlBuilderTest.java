package org.nbone.persistence;

import org.junit.Test;
import org.nbone.persistence.enums.JdbcFrameWork;
import org.nbone.persistence.model.SqlModel;
import org.nbone.persistence.util.SqlUtils;

public class BaseSqlBuilderTest {
	
	private SqlBuilder sqlBuilder = new BaseSqlBuilder(JdbcFrameWork.SPRING_JDBC) {
		
	};
	//@Test
	public void testbuildSelectSql(){
		SqlModel<Object> sqlModel = sqlBuilder.buildSelectSql(getTsProjectDTO());
		System.out.println(sqlModel.getSql());
	}
	
	//@Test
	public void testbuildSimpleSelectSql(){
		SqlModel<Object> sqlModel = sqlBuilder.buildSimpleSelectSql(getTsProjectDTO());
		System.out.println(sqlModel.getSql());
		
	}
	//@Test
	public void testbuildSelectSqlByIds(){
		
		SqlModel<TsProjectDTO> sqlModel = sqlBuilder.buildSelectSqlByIds(TsProjectDTO.class, new Object[]{1030,1037});
		System.out.println(sqlModel.getSql());
		
	}
	
	@Test
	public void testbuildDeleteSqlByIds(){
		
		SqlModel<TsProjectDTO> sqlModel = sqlBuilder.buildDeleteSqlByIds(TsProjectDTO.class, new Long[]{1030L,1037L});
		System.out.println(sqlModel.getSql());
		
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
		SqlModel<Object> sqlModel = sqlBuilder.buildUpdateSql(object);
		System.out.println(sqlModel.getSql());
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

package org.nbone.persistence.support;

public class PageSuport {
	
	
	
	public static String toMysqlPage(String sql,int offset, int pageSize){
		
		return "select sys_tempp.* from ( " + sql+ " )  sys_tempp LIMIT " + pageSize * (offset-1) + ","+ pageSize;
	}
	
	public static String toOraclePage(String sql,int offset, int pageSize){
		StringBuilder sbSql = new StringBuilder();
		sbSql.append("select system_temp.*  from (select  temp.*  ,rownum as rownums   from (");
		sbSql.append(sql).append(" ) temp ) system_temp  where  system_temp.rownums >");
		sbSql.append(pageSize * (offset - 1));  
		// 加了个等号,不然会少查一条记录
		sbSql.append(" and system_temp.rownums <= ");
		sbSql.append(pageSize * (offset));
		
		return sbSql.toString();
	}
	
	
	public static String getCountSqlString(String sql) {
		return "select count(1) as total  from ( " + sql + " ) sysT";
	}

	

}

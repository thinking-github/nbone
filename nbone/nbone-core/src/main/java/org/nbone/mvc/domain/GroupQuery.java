package org.nbone.mvc.domain;

/**
 * @author chenyicheng
 * @version 1.0
 * @since 2019/6/27
 */
public class GroupQuery {

    /**
     * 分组字段 多个以逗号隔开
     */
    private String groupBy;

    /**
     * 排序字段 id DESC
     */
    private String orderBy;

    /**
     * 查询列（包含统计函数列） select id,count(id) countNum  from User
     */
    private String queryColumn;

    /**
     *返回结果集映射类
     */
    private Class<?> mapClass;



    public static GroupQuery build(){
        return  new GroupQuery();
    }

    //链式调用方法
    /**
     * 构建分组查询语句
     * @param columnName
     * @return
     */
    public GroupQuery groupBy(String columnName){
        this.groupBy = columnName;
        return  this;
    }
    public GroupQuery orderBy(String column){
        this.orderBy = column;
        return  this;
    }
    public GroupQuery mapClass(Class<?> mapClass){
        this.mapClass = mapClass;
        return  this;
    }
    public GroupQuery queryColumn(String queryColumn){
        this.queryColumn = queryColumn;
        return  this;
    }


    /**
     * 分组字段
     * @return
     */
    public String groupBy(){
        return groupBy;
    }

    /**
     * 排序字段
     * @return
     */
    public String orderBy(){
        return orderBy ;
    }

    /**
     * 查询字段
     * @return
     */
    public String queryColumn(){

        return queryColumn ;
    }

    /**
     * 返回 select sql 语句片段
     * @return
     */
    public String getQueryColumnBySql(){
        if(queryColumn == null){
            throw   new IllegalArgumentException("queryColumn columnName not is null.");
        }
        if(queryColumn.indexOf(groupBy) == -1){
            return queryColumn + "," + groupBy;
        }
        return  queryColumn;
    }

    /**
     * 返回分组sql语句
     * @return
     */
    public String getGroupBySql(){
        if(groupBy == null){
            throw   new IllegalArgumentException("groupBy columnName not is null.");
        }
        return  " group by " + groupBy;
    }

    /**
     * 返回排序sql语句
     * @return
     */
    public String getOrderBySql(){
        if(orderBy == null){
            return  null;
        }
        return  " order by " + orderBy;
    }

    /**
     * 返回映射的类型 实体对象、map
     * @return
     */
    public Class<?> getMapClass() {
        return mapClass;
    }

}

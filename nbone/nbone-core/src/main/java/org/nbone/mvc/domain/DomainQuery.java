package org.nbone.mvc.domain;

/**
 * 查询实体接口
 * @author chenyicheng
 * @verison 1.0
 * @since 2017年8月3日
 *
 */
public interface DomainQuery {

	/**
	 * 追加至Where之后
	 * @return
	 */
	public String appendWhere();

	/**
	 * group by 子句
	 * @return
	 */
	public String groupBy();


	public String having();
	
	public String orderBy();

	

}

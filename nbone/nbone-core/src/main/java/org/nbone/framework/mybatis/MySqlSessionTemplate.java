package org.nbone.framework.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
/**
 * 
 * @author thinking
 *
 */
public class MySqlSessionTemplate extends SqlSessionTemplate  {

	public MySqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		super(sqlSessionFactory);
	}

}

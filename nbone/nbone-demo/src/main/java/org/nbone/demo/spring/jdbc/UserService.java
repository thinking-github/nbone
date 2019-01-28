package org.nbone.demo.spring.jdbc;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.nbone.demo.common.domain.User;
import org.nbone.framework.spring.dao.namedparam.NamedJdbcDao;
import org.nbone.lang.MathOperation;
import org.nbone.mvc.service.BaseServiceDomain;
import org.nbone.mvc.service.SuperService;
import org.nbone.persistence.SqlConfig;
import org.nbone.persistence.annotation.FieldLevel;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseServiceDomain<User,Long> implements SuperService<User,Long> {
	
	@Resource
	private NamedJdbcDao namedJdbcDao;
    
	@Override
	public Long save(User object) {
		return (Long) namedJdbcDao.save(object);
	}
	@Override
	public void update(User object) {
		namedJdbcDao.update(object);
	}
	@Override
	public void delete(Long id) {
		namedJdbcDao.delete(User.class,id);
	}
	@Override
	public User get(Long id) {
		return namedJdbcDao.get(id);
	}
    /*根据对象实际有效参数 删除和查询 
     */
	@Override
	public void deleteByEntityParams(User object) {
		super.deleteByEntityParams(object);
	}
	//严格操作规约查询 select * from user where name ='chenyicheng' and age = 18
	@Override
	public List<User> getForList(User object) {
		return namedJdbcDao.getForList(object,null);
	}
	//常用操作规约查询 select * from user where name like '%chenyicheng%' and age = 18
	@Override
	public List<User> queryForList(User object) {
		return namedJdbcDao.queryForList(object,(String)null);
	}
	//自定义操作符号查询 select * from user where name like '%chenyicheng%' and age >= 18
	public List<User> queryForList(User object,SqlConfig config) {
		return namedJdbcDao.queryForList(object,config);
	}
	//按需查询(定义字段级别) select id,name,age from user where name like '%chen%' and age = 18
	public List<User> queryForList(User object,FieldLevel fieldLevel) {
		return namedJdbcDao.queryForList(object,fieldLevel,null);
	}
	//按需查询 select id from user where name name ='chenyicheng' and age = 18
	@Override
	public <E> List<E> getForList(Object object, String fieldName,Class<E> requiredType) {
		return namedJdbcDao.getForList(object, fieldName,requiredType);
	}
	//按需查询(给定字段列表) select id,name from user where name name ='chenyicheng' and age = 18
	@Override
	public List<User> getForListWithFieldNames(Object object, String[] fieldNames) {
		return namedJdbcDao.getForListWithFieldNames(object, fieldNames,false);
	}
	
	//分页查询 select * from user where name ='chenyicheng' and age = 18
	@Override
	public Page<User> getForPage(Object object, int pageNum, int pageSize,String... afterWhere) {
		return namedJdbcDao.getForPage(object, pageNum, pageSize,afterWhere);
	}
	//分页查询 select * from user where name like '%chenyicheng%' and age = 18
	@Override
	public Page<User> queryForPage(Object object, int pageNum, int pageSize,String... afterWhere) {
		return namedJdbcDao.queryForPage(object, pageNum, pageSize,afterWhere);
	}
	@Override
	public Page<User> findForPage(Object object, int pageNum, int pageSize,String... afterWhere) {
		return namedJdbcDao.findForPage(object, pageNum, pageSize,afterWhere);
	}
	//限制查询 select * from user where name ='chenyicheng' and age = 18 limit 10
    @Override
	public List<User> getForLimit(Object object, int limit, String... afterWhere) {
		return super.getForLimit(object, limit, afterWhere);
	}
    //限制查询 select * from user where name like '%chenyicheng%' and age = 18 limit 10
	@Override
	public List<User> queryForLimit(Object object, int limit, String... afterWhere) {
		return super.queryForLimit(object, limit, afterWhere);
	}
	
	//字段计算 update set user age = age+1  where id = 1
	@Override
	public int updateMathOperation(Object object, MathOperation mathOperation) {
		return super.updateMathOperation(object, mathOperation);
	}
	//批量操作 增 删 改
	@Override
	public void batchInsert(Collection<User> objects) {
		super.batchInsert(objects);
	}
	@Override
	public void batchInsert(User[] objects) {
		super.batchInsert(objects);
	}
	@Override
	public void batchUpdate(Collection<User> objects) {
		super.batchUpdate(objects);
	}
	@Override
	public void batchUpdate(User[] objects) {
		super.batchUpdate(objects);
	}
	@Override
	public void batchDelete(Class<User> clazz, List<Serializable> ids) {
		super.batchDelete(clazz, ids);
	}
	@Override
	public void batchDelete(Class<User> clazz, Serializable[] ids) {
		super.batchDelete(clazz, ids);
	}
		
	

}

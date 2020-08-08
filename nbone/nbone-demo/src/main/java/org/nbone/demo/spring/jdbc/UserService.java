package org.nbone.demo.spring.jdbc;

import org.nbone.demo.common.domain.User;
import org.nbone.demo.common.domain.UserQuery;
import org.nbone.framework.spring.dao.namedparam.NamedJdbcDao;
import org.nbone.lang.MathOperation;
import org.nbone.mvc.domain.GroupQuery;
import org.nbone.mvc.service.BaseServiceDomain;
import org.nbone.mvc.service.SuperService;
import org.nbone.persistence.SqlConfig;
import org.nbone.persistence.annotation.FieldLevel;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

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
	public void delete(Long id,String tableName) {
		namedJdbcDao.delete(User.class,id,tableName);
	}
	@Override
	public User get(Long id,String tableName) {
		return namedJdbcDao.get(User.class,id,tableName);
	}
    /*根据对象实际有效参数 删除和查询 
     */
	@Override
	public void deleteByEntity(User object) {
		namedJdbcDao.deleteByEntity(object);
	}

	//严格操作规约查询 select * from user where id='100' and name ='chenyicheng' and age = 18 and create_time = 100000
	@Override
	public List<User> getForList(User object) {
		User user  = new User("100","chenyicheng",18);
		user.setCreateTime(new Date());
		return namedJdbcDao.getForList(object,null);
	}

	//select * from user where name ='chenyicheng' and age = 18 and create_time > 100000 and id in(1,2,3,4,5) and update_time BETWEEN 1000 AND 2000
	public List<User> getForList1(User object) {
		SqlConfig sqlConfig = SqlConfig.builder()
				.addOperation("createTime",">")
				//.addOperation("id","in", Arrays.asList(1,2,3,4,5))
				.addOperationIn("id",true,Arrays.asList(1,2,3,4,5))
				.addOperationBetween("updateTime",1000,2000);
		return namedJdbcDao.getForList(object,sqlConfig);
	}

    //查询对象单独定义Bean 查询
	public List<User> getForList2(UserQuery userQuery) {
		SqlConfig sqlConfig = SqlConfig.builder().withEntityClass(User.class);
		return namedJdbcDao.getForList(userQuery,sqlConfig);
	}
	//HttpServletRequest RequestParam 直接查询 /user?id=1&name=thinking
	public List<User> getForList2(HttpServletRequest request) {
		SqlConfig sqlConfig = SqlConfig.builder().withEntityClass(User.class);
		return namedJdbcDao.requestQuery(request,sqlConfig);
	}
	//HttpServletRequest RequestParam 直接分页查询 /user?id=1&name=thinking&pageNum=1&pageSize=20
	public Page<User> getForPage(HttpServletRequest request) {
		SqlConfig sqlConfig = SqlConfig.builder().withEntityClass(User.class);
		return namedJdbcDao.requestQueryPage(request,sqlConfig);
	}

	//常用操作规约查询 select * from user where name like '%chenyicheng%' and age = 18
	@Override
	public List<User> queryForList(User object) {
		return namedJdbcDao.queryForList(object,null);
	}
	//自定义操作符号查询 select * from user where name like '%chenyicheng%' and age >= 18
	public List<User> queryForList(User object, SqlConfig config) {
		return namedJdbcDao.queryForList(object,config);
	}
	//按需查询(定义字段级别) select id,name,age from user where name like '%chen%' and age = 18
	public List<User> queryForList(User object,FieldLevel fieldLevel) {
		SqlConfig sqlConfig = new SqlConfig(SqlConfig.PrimaryMode).fieldLevel(fieldLevel);
		return namedJdbcDao.queryForList(object,sqlConfig);
	}
	//按需查询 select id from user where name name ='chenyicheng' and age = 18
	@Override
	public <E> List<E> getForList(Object object, String fieldName,Class<E> requiredType,String... afterWhere) {
		return namedJdbcDao.getForList(object, fieldName,requiredType);
	}
	//按需查询(给定字段列表) select id,name from user where name name ='chenyicheng' and age = 18
	public List<User> getForList(Object object, String[] fieldNames,String... afterWhere) {
		SqlConfig sqlConfig = new SqlConfig(-1).fieldNames(fieldNames).afterWhere(afterWhere);
		return namedJdbcDao.getForList(object, sqlConfig);
	}
	
	//分页查询 select * from user where name ='chenyicheng' and age = 18 limit 0,20
	@Override
	public Page<User> getForPage(User object,SqlConfig sqlConfig, int pageNum, int pageSize) {
		return namedJdbcDao.getForPage(object, sqlConfig,pageNum, pageSize);
	}
	//分页查询 select * from user where name like '%chenyicheng%' and age = 18 limit 0,20
	@Override
	public Page<User> queryForPage(User object, int pageNum, int pageSize,String... afterWhere) {
		SqlConfig sqlConfig = new SqlConfig(SqlConfig.PrimaryMode).afterWhere(afterWhere);
		return namedJdbcDao.queryForPage(object,sqlConfig, pageNum, pageSize);
	}

	@Override
	public Page<User> findForPage(Object object, int pageNum, int pageSize,String... afterWhere) {
		return namedJdbcDao.findForPage(object, pageNum, pageSize,afterWhere);
	}
	//限制查询 select * from user where name ='chenyicheng' and age = 18 limit 10
    @Override
	public List<User> getForLimit(Object object, Map<String,String> operationMap ,GroupQuery group, int limit, String... afterWhere) {
		return super.getForLimit(object,operationMap,group,limit, afterWhere);
	}

	//限制查询 select * from user where name like '%chenyicheng%' and age = 18 limit 10
	@Override
	public List<User> queryForLimit(User object,SqlConfig sqlConfig, int limit) {
		return super.queryForLimit(object,sqlConfig, limit);
	}

	//条件查询 转化成Map select * from user where name = 'chenyicheng' and age = 18
	public Map<String,User> getMapKeyValue(User object) {
		SqlConfig sqlConfig = SqlConfig.builder(-1).mapKey("id").mapKeyType(String.class);
		return namedJdbcDao.getMapKeyValue(object,sqlConfig);
	}

	//字段计算 update set user age = age+1  where id = 1
	@Override
	public int updateMathOperation(Object object,String property,MathOperation mathOperation) {
		return super.updateMathOperation(object,null, mathOperation);
	}
	//批量操作 增 删 改
	@Override
	public void batchInsert(Collection<User> objects,boolean jdbcBatch) {
		super.batchInsert(objects,jdbcBatch);
	}
	@Override
	public void batchInsert(User[] objects,boolean jdbcBatch) {
		super.batchInsert(objects,jdbcBatch);
	}
	@Override
	public void batchUpdate(Collection<User> objects,String...propertys) {
		super.batchUpdate(objects,propertys);
	}
	@Override
	public void batchUpdate(User[] objects,String...propertys) {
		super.batchUpdate(objects,propertys);
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

package org.nbone.demo.spring.jdbc;

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
public class UserService extends BaseServiceDomain<User, Long> implements SuperService<User, Long> {
	
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

	@Override
	public void deleteByEntityParams(User object) {
		super.deleteByEntityParams(object);
	}
	@Override
	public List<User> getForList(User object) {
		return namedJdbcDao.getForList(object);
	}
	@Override
	public List<User> queryForList(User object) {
		return namedJdbcDao.queryForList(object);
	}
	public List<User> queryForList(User object,SqlConfig config) {
		return namedJdbcDao.queryForList(object,config);
	}
	
	
	
	public List<User> queryForList(User object,FieldLevel fieldLevel) {
		return namedJdbcDao.queryForList(object,fieldLevel);
	}
	
	@Override
	public <E> List<E> getForList(Object object, String fieldName) {
		return namedJdbcDao.getForList(object, fieldName);
	}
	
	@Override
	public List<User> getForListWithFieldNames(Object object, String[] fieldNames) {
		return namedJdbcDao.getForListWithFieldNames(object, fieldNames,false);
	}
	
	
	@Override
	public Page<User> getForPage(Object object, int pageNum, int pageSize) {
		return namedJdbcDao.getForPage(object, pageNum, pageSize);
	}

	@Override
	public Page<User> queryForPage(Object object, int pageNum, int pageSize) {
		return namedJdbcDao.queryForPage(object, pageNum, pageSize);
	}

	@Override
	public Page<User> findForPage(Object object, int pageNum, int pageSize) {
		return namedJdbcDao.findForPage(object, pageNum, pageSize);
	}


	

	@Override
	public int updateMathOperation(Object object, MathOperation mathOperation) {
		return super.updateMathOperation(object, mathOperation);
	}
	
	
	
	
	

}

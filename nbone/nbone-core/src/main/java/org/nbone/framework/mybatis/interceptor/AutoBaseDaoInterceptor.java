package org.nbone.framework.mybatis.interceptor;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.nbone.framework.mybatis.MybatisSqlBuilder;
import org.nbone.persistence.mapper.TableMapper;
import org.nbone.persistence.model.SqlModel;
import org.nbone.util.reflect.ReflectFieldUtil;



/**
 * 通过拦截<code>StatementHandler</code> of <code>prepare</code> method<br>
 * 根据参数parameterObject配置的注解信息，自动生成sql语句。 <br>
 * <pre>
 * 
 * {@code
 *  resultMapId  符合如下规范  EntityClass + Mapper
 * <resultMap id="AddressbaseMapper" type="com.nbone.basecenter.domain.AddressBase">
 * }
 * </pre>
 * 
 * @author thinking
 * @since 2015-12-12
 * 
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class AutoBaseDaoInterceptor implements Interceptor {
	
	
	private String resultMapIdSuffix = "Mapper"; 
	
	
	
	
    private static final Log logger = LogFactory.getLog(AutoBaseDaoInterceptor.class);
    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
    	
    	StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
    	RoutingStatementHandler rsHandler = null;
    	if (invocation.getTarget().getClass().isAssignableFrom(RoutingStatementHandler.class)) {
    		rsHandler = (RoutingStatementHandler) statementHandler;
    	}
    	ParameterHandler parameterHandler = statementHandler.getParameterHandler();
        BoundSql bsql = statementHandler.getBoundSql();
        String sql = bsql.getSql();
        Object parameterObject = bsql.getParameterObject();
        
        MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY,DEFAULT_OBJECT_WRAPPER_FACTORY);
        // 分离代理对象链
        while (metaStatementHandler.hasGetter("h")) {
            Object object = metaStatementHandler.getValue("h");
            metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
        }
        // 分离最后一个代理对象的目标类
        while (metaStatementHandler.hasGetter("target")) {
            Object object = metaStatementHandler.getValue("target");
            metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
        }
        
        BaseStatementHandler basestatH = (BaseStatementHandler) metaStatementHandler.getValue("delegate");
        MappedStatement ms = (MappedStatement)ReflectFieldUtil.getFieldValue(basestatH, "mappedStatement");
        Configuration configuration = (Configuration)ReflectFieldUtil.getFieldValue(basestatH, "configuration");
        
        
        if (null == sql || "".equals(sql)) {
            SqlModel model = null ;
            String id = ms.getId();
            id = id.substring(id.lastIndexOf(".") + 1);
            
            //resultMap id 格式为 entityClassName+Mapper
            Class<?> entityClass = parameterObject.getClass();
            String name  = entityClass.getSimpleName();
            String resultMapId =  name+resultMapIdSuffix;
            boolean boo = resultMapId.equals("AddressBaseDTOMapper");
            
             MybatisSqlBuilder.buildTableMapper(entityClass,resultMapId);
            if ("insert".equals(id) || "insertAuto".equals(id)) {
            	model = MybatisSqlBuilder.oxm_me.buildInsertSql(parameterObject);
            } else if ("update".equals(id)|| "updateAuto".equals(id)) {
            	model = MybatisSqlBuilder.oxm_me.buildUpdateSql(parameterObject);
            } else if ("delete".equals(id) || "deleteAuto".equals(id)) {
            	model = MybatisSqlBuilder.oxm_me.buildDeleteSql(parameterObject);
            } else if ("select".equals(id) || "get".equals(id)) {
            	model = MybatisSqlBuilder.oxm_me.buildSelectSql(parameterObject);
            }
            logger.info("================AutoCRUDInterceptor==========================");
            logger.info(model.getSql());
            
            logger.debug("Auto generated sql:" + model.getSql());
            
            SqlSource sqlSource = buildSqlSource(configuration, model.getSql(), parameterObject.getClass());
            BoundSql newboSql =  sqlSource.getBoundSql(parameterObject);
            
            List<ParameterMapping> parameterMappings = newboSql.getParameterMappings();
            metaStatementHandler.setValue("delegate.boundSql.sql",newboSql.getSql());
            metaStatementHandler.setValue("delegate.boundSql.parameterMappings", parameterMappings);
        }
        // 传递给下一个拦截器处理
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
            return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    	String test = properties.getProperty("test");
    	logger.debug("-----------------------------------:"+test);
    	
    	
    	String resultMapIdSuffix = properties.getProperty("resultMapIdSuffix");
    	
    	if(resultMapIdSuffix != null){
    		this.resultMapIdSuffix = resultMapIdSuffix;
    	}
    	
    	

    }

    private SqlSource buildSqlSource(Configuration configuration, String originalSql, Class<?> parameterType) {
        SqlSourceBuilder builder = new SqlSourceBuilder(configuration);
        return builder.parse(originalSql, parameterType,null);
    }
}

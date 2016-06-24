package org.nbone.framework.mybatis;

import org.nbone.persistence.BaseSqlBuilder;
import org.nbone.persistence.enums.JdbcFrameWork;
import org.nbone.persistence.mapper.FieldMapper;
import org.nbone.persistence.mapper.TableMapper;

/**
 * 通过自定义tableMapper and FieldMapper 生成SQl
 * @author thinking
 * @since 2015-12-12
 * @see TableMapper
 * @see FieldMapper
 *
 */
public class MybatisSqlBuilder  extends BaseSqlBuilder{
	
	public final static MybatisSqlBuilder annotation_me = new MybatisSqlBuilder(annotation);
	
	public final static MybatisSqlBuilder oxm_me = new MybatisSqlBuilder(oxm);
	
    private String prefix = "" ;
    private String suffix = "Mapper";
	
	private int ormType = 1;
 	
    

	private MybatisSqlBuilder(int ormType) {
		super(JdbcFrameWork.MYBATIS);
		this.ormType = ormType;
	}

	
	

}

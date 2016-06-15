package org.nbone.framework.mybatis.generator.plugins;

import java.util.List;

/**
 * This class  was data asscss object (DAO)
 * @author thinking
 * @since 2016-06-15
 */
public interface TsProjectDTOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TsProjectDTO record);

    int insertSelective(TsProjectDTO record);

    TsProjectDTO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TsProjectDTO record);

    int updateByPrimaryKeyWithBLOBs(TsProjectDTO record);

    int updateByPrimaryKey(TsProjectDTO record);

    List<TsProjectDTO> queryForList(TsProjectDTO entity);
}
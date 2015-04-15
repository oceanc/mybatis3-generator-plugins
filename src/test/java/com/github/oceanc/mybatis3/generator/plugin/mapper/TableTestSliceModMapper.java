package com.github.oceanc.mybatis3.generator.plugin.mapper;

import com.github.oceanc.mybatis3.generator.plugin.model.TableTestSliceMod;
import com.github.oceanc.mybatis3.generator.plugin.model.TableTestSliceModExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface TableTestSliceModMapper {
    int countByExample(TableTestSliceModExample example);

    int deleteByExample(TableTestSliceModExample example);

    int deleteByPrimaryKey(TableTestSliceMod record);

    int insert(TableTestSliceMod record);

    int insertSelective(TableTestSliceMod record);

    List<TableTestSliceMod> selectByExampleWithRowbounds(TableTestSliceModExample example, RowBounds rowBounds);

    List<TableTestSliceMod> selectByExample(TableTestSliceModExample example);

    TableTestSliceMod selectByPrimaryKey(TableTestSliceMod record);

    int updateByExampleSelective(@Param("record") TableTestSliceMod record, @Param("example") TableTestSliceModExample example);

    int updateByExample(@Param("record") TableTestSliceMod record, @Param("example") TableTestSliceModExample example);

    int updateByPrimaryKeySelective(TableTestSliceMod record);

    int updateByPrimaryKey(TableTestSliceMod record);

    Long sumByExample(TableTestSliceModExample example);
}
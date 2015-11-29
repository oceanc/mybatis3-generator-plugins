package com.github.oceanc.mybatis3.generator.plugin.mapper;

import com.github.oceanc.mybatis3.generator.plugin.model.TableTestSliceMonth;
import com.github.oceanc.mybatis3.generator.plugin.model.TableTestSliceMonthExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface TableTestSliceMonthMapper {
    int countByExample(TableTestSliceMonthExample example);

    int deleteByExample(TableTestSliceMonthExample example);

    int deleteByPrimaryKey(TableTestSliceMonth record);

    int insert(TableTestSliceMonth record);

    int insertSelective(TableTestSliceMonth record);

    List<TableTestSliceMonth> selectByExampleWithRowbounds(TableTestSliceMonthExample example, RowBounds rowBounds);

    List<TableTestSliceMonth> selectByExample(TableTestSliceMonthExample example);

    TableTestSliceMonth selectByPrimaryKey(TableTestSliceMonth record);

    int updateByExampleSelective(@Param("record") TableTestSliceMonth record, @Param("example") TableTestSliceMonthExample example);

    int updateByExample(@Param("record") TableTestSliceMonth record, @Param("example") TableTestSliceMonthExample example);

    int updateByPrimaryKeySelective(TableTestSliceMonth record);

    int updateByPrimaryKey(TableTestSliceMonth record);

    Long sumByExample(TableTestSliceMonthExample example);

    void batchInsert(@Param("items") List<TableTestSliceMonth> items);
}
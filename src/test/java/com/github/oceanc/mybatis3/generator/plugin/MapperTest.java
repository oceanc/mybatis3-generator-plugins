package com.github.oceanc.mybatis3.generator.plugin;

import com.github.oceanc.mybatis3.generator.plugin.mapper.TableTestSliceModMapper;
import com.github.oceanc.mybatis3.generator.plugin.model.TableTestSliceMod;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chengyang
 */
public class MapperTest {
    public static void main(String[] args) {
        String resource = "/mybatis_config.xml";

        //使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
        InputStream is = MapperTest.class.getResourceAsStream(resource);
        //构建sqlSession的工厂
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);

        //使用MyBatis提供的Resources类加载mybatis的配置文件（它也加载关联的映射文件）
        //Reader reader = Resources.getResourceAsReader(resource);
        //构建sqlSession的工厂
        //SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);

        //创建能执行映射文件中sql的sqlSession
        SqlSession session = sessionFactory.openSession();

        TableTestSliceModMapper mapper = session.getMapper(TableTestSliceModMapper.class);

        TableTestSliceMod mod = new TableTestSliceMod();
        mod.setSliceModId(83L);
        mod.setCouldSumCol(1);
        mod.setJacksonId1(11L);
        mod.setJacksonId2("jks2");
        mod.setJacksonTime(new Date());

        TableTestSliceMod mod2 = new TableTestSliceMod();
        mod2.setSliceModId(166L);
        mod2.setCouldSumCol(2);
        mod2.setJacksonId1(22L);
        mod2.setJacksonId2("jks2");
        mod2.setJacksonTime(new Date());

        List<TableTestSliceMod> list = new ArrayList<TableTestSliceMod>(1);
        list.add(mod);
        list.add(mod2);

        mapper.batchInsert(list);

        session.commit();
    }
}

package com.github.oceanc.mybatis3.generator.plugin;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.VerboseProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengyang
 */
public class Generator {

    public static void main(String[] args) {
        String path = Generator.class.getClassLoader().getResource("").getPath();
        String root = path.split("target")[0];
        String projectPath = root + "src/test/java";
        String modelPackage = "com.github.oceanc.mybatis3.generator.plugin.model";
        String mapperPackage = "com.github.oceanc.mybatis3.generator.plugin.mapper";
        String xmlProjectPath = root + "src/test/resources";
        String xmlPackage = "mapping";

        try {
            List<String> warnings = new ArrayList<String>();
            File configFile = new File(path + "generatorConfig-mysql.xml");
            ConfigurationParser cp = new ConfigurationParser(warnings);
            Configuration config = cp.parseConfiguration(configFile);

            config.getContexts().get(0).getJavaModelGeneratorConfiguration().setTargetProject(projectPath);
            config.getContexts().get(0).getJavaModelGeneratorConfiguration().setTargetPackage(modelPackage);

            config.getContexts().get(0).getJavaClientGeneratorConfiguration().setTargetProject(projectPath);
            config.getContexts().get(0).getJavaClientGeneratorConfiguration().setTargetPackage(mapperPackage);

            config.getContexts().get(0).getSqlMapGeneratorConfiguration().setTargetProject(xmlProjectPath);
            config.getContexts().get(0).getSqlMapGeneratorConfiguration().setTargetPackage(xmlPackage);

            DefaultShellCallback callback = new DefaultShellCallback(true);
            ProgressCallback progressCallback = new VerboseProgressCallback();
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(progressCallback);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}

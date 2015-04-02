package com.github.oceanc.mybatis3.generator.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengyang
 */
public class LombokAnnotationPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addAnnotation("@Data");
        topLevelClass.addImportedType(new FullyQualifiedJavaType("lombok.Data"));
        List<Method> methods =  topLevelClass.getMethods();
        List<Method> remove = new ArrayList<Method>();
        for (Method method : methods) {
            if (method.getBodyLines().size() < 2) {
                remove.add(method);
                log.debug("{}'s method={} removed cause lombok annotation.", topLevelClass.getType().getShortName(), method.getName());
            }
        }
        methods.removeAll(remove);
        return true;
    }

    private Logger log = LoggerFactory.getLogger(LombokAnnotationPlugin.class);

}

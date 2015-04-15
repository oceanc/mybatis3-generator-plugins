package com.github.oceanc.mybatis3.generator.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

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
                System.out.println("-----------------" + topLevelClass.getType().getShortName() + "'s method=" + method.getName() + " removed cause lombok annotation.");
            }
        }
        methods.removeAll(remove);
        return true;
    }

}

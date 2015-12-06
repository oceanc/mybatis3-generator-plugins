package com.github.oceanc.mybatis3.generator.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;

/**
 * Created by chengyang
 */
public class WhereSqlTextPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        for (InnerClass innerClass : topLevelClass.getInnerClasses()) {
            if (FullyQualifiedJavaType.getGeneratedCriteriaInstance().equals(innerClass.getType())) {
                String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
                Method method = new Method();
                method.setName("addConditionSql");
                method.setVisibility(JavaVisibility.PUBLIC);
                method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());
                method.addBodyLine("addCriterion(conditionSql);");
                method.addBodyLine("return (Criteria) this;");
                method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "conditionSql"));
                PluginUtils.addDoc(this.getContext(), method, tableName);
                innerClass.getMethods().add(method);

                System.out.println("-----------------" + topLevelClass.getType().getShortName() + " add method=addConditionSql for custom sql statement in where clause.");
            }
        }
        return true;
    }
}

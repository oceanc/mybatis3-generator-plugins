package com.github.oceanc.mybatis3.generator.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        String sqlexp = introspectedTable.getTableConfigurationProperty(WHERE_SQL);
        if ("true".equals(sqlexp)) {
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

                    List<Method> methods = topLevelClass.getMethods();
                    for(Method mod:methods){

                    }
                    System.out.println("-----------------" + topLevelClass.getType().getShortName() + " add method=addConditionSql for custom sql statement in where clause.");
                }
            }
        }
        return true;
    }

    private final SimpleDateFormat df = new SimpleDateFormat("EEE MMM ww HH:mm:ss z yyyy", Locale.US);
    private final static String WHERE_SQL = "whereSql";
}

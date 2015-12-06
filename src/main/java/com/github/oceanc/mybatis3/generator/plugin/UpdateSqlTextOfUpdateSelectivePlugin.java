package com.github.oceanc.mybatis3.generator.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

/**
 * Created by chengyang
 */
public class UpdateSqlTextOfUpdateSelectivePlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        Field field = new Field();
        field.setName(UPDATE_SQL);
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(FullyQualifiedJavaType.getStringInstance());
        PluginUtils.addDoc(this.getContext(), field, tableName);

        Method getter = new Method();
        getter.setName("get" + UPDATE_SQL.substring(0, 1).toUpperCase() + UPDATE_SQL.substring(1, UPDATE_SQL.length()));
        getter.setVisibility(JavaVisibility.PUBLIC);
        getter.setReturnType(FullyQualifiedJavaType.getStringInstance());
        getter.addBodyLine("return this." + UPDATE_SQL + ";");
        PluginUtils.addDoc(this.getContext(), getter, tableName);

        Method setter = new Method();
        setter.setName("set" + UPDATE_SQL.substring(0, 1).toUpperCase() + UPDATE_SQL.substring(1, UPDATE_SQL.length()));
        setter.setVisibility(JavaVisibility.PUBLIC);
        setter.setReturnType(null);
        setter.addBodyLine("this." + UPDATE_SQL + " = " + UPDATE_SQL + ";");
        setter.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), UPDATE_SQL));
        PluginUtils.addDoc(this.getContext(), setter, tableName);

        topLevelClass.addField(field);
        topLevelClass.addMethod(getter);
        topLevelClass.addMethod(setter);

        System.out.println("-----------------" + topLevelClass.getType().getShortName() + " add field=" + UPDATE_SQL + " and getter & setter related that is only effective in update selective model.");
        return true;
    }

    @Override
    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        String node = "<if test=\"record.updateSql != null\" >\n" +
                "        ${record.updateSql},\n" +
                "      </if>";
        TextElement ele = new TextElement(node);
        XmlElement set = (XmlElement) element.getElements().get(1);
        set.getElements().add(ele);
        return true;
    }

    private final static String UPDATE_SQL = "updateSql";
}

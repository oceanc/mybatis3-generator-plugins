package com.github.oceanc.mybatis3.generator.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.*;

import java.text.MessageFormat;
import java.util.List;

/**
 * Created by chengyang
 */
public class BatchInsertPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType paramType = FullyQualifiedJavaType.getNewListInstance();
        paramType.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
        Method method = new Method();
        method.setName(methodName);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(paramType, "items", "@Param(\"items\")"));
        interfaze.addMethod(method);
        interfaze.addImportedType(PARAM_ANOTS);
        System.out.println("-----------------" + interfaze.getType().getShortName() + " add method " + methodName + ".");
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        String bind = "";
        if (SliceTablePlugin.needPartition(introspectedTable)) {
            bind = "<bind name=\"tableNameSuffix\" value=\"items.get(0).getTableNameSuffix()\" />\n";
        }

        String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        if(!tableName.contains("_${tableNameSuffix}") && SliceTablePlugin.needPartition(introspectedTable)) {
            tableName = tableName + "_${tableNameSuffix}";
        }

        XmlElement node = findInsertNode(document);
        if (node != null) {
            StringBuilder values = new StringBuilder();
            for (Element el : node.getElements()) {
                if (el.getClass() == TextElement.class) {
                    TextElement tl = (TextElement) el;
                    values.append("      ").append(tl.getContent()).append("\n");
                }
            }

            String[] insertStr = values.toString().split("values \\(");
            String fs = insertStr[0];
            fs = " (" + fs.split(" \\(")[1].trim();

            String xx = "      (" + insertStr[1];
            xx = xx.replaceAll("#\\{", "#\\{item.");

            String xml = MessageFormat.format(template, bind, tableName, fs, xx);
            document.getRootElement().getElements().add(new TextElement(xml));
            return true;
        }
        return false;
    }

    private XmlElement findInsertNode(Document document) {
        for (Element el : document.getRootElement().getElements()) {
            if (el.getClass() == XmlElement.class) {
                XmlElement xl = (XmlElement) el;
                if (xl.getName().equals("insert")) {
                    for (Attribute attr : xl.getAttributes()) {
                        if (attr.getName().equals("id") && attr.getValue().equals("insert")) {
                            return xl;
                        }
                    }
                }
            }
        }
        return null;
    }

    private final static FullyQualifiedJavaType PARAM_ANOTS = new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param");
    private final static String methodName = "batchInsert";
    private final static String template =
            "<insert id=\"" + methodName + "\">\n" +
            "    <if test=\"items.get(0) != null\">\n" +
            "      {0}" +
            "      insert into {1} {2}\n" +
            "      values\n" +
            "      <foreach collection=\"items\" item=\"item\" index=\"index\" separator=\",\">\n{3}" +
            "      </foreach>\n" +
            "    </if>\n" +
            "  </insert>";
}

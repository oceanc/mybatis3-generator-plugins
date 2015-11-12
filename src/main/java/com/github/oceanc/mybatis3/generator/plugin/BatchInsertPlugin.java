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
        method.addParameter(new Parameter(paramType, "items"));
        interfaze.addMethod(method);
        System.out.println("-----------------" + interfaze.getType().getShortName() + " add method " + methodName + ".");
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement node = findInsertNode(document);
        if (node != null) {
            StringBuilder values = new StringBuilder();
            for (Element el : node.getElements()) {
                if (el.getClass() == TextElement.class) {
                    TextElement tl = (TextElement) el;
                    values.append("      ").append(tl.getContent()).append("\n");
                }
            }

            String xx = "      (" + values.toString().split("values \\(")[1];
            xx = xx.replaceAll("#\\{", "#\\{item.");

            String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
            String xml = MessageFormat.format(template, tableName, xx);
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

    private final static String methodName = "batchInsert";
    private final static String template =
            " <insert id=\"" + methodName + "\">\n" +
            "    insert into {0} ( <include refid=\"Base_Column_List\"/> )\n" +
            "    values\n" +
            "    <foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">\n{1}" +
            "    </foreach>\n" +
            "  </insert>";
}

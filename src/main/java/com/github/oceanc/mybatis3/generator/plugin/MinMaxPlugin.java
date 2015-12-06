package com.github.oceanc.mybatis3.generator.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;

import java.text.MessageFormat;
import java.util.List;

/**
 * Created by chengyang
 */
public class MinMaxPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String minColumns = introspectedTable.getTableConfigurationProperty(MIN_COLUMNS);
        String maxColumns = introspectedTable.getTableConfigurationProperty(MAX_COLUMNS);

        if (minColumns != null && !"".equals(minColumns)) {
            for (String column : minColumns.split(DELIMITER)) {
                for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
                    if (column.trim().equals(introspectedColumn.getActualColumnName())) {
                        addMinMethod(interfaze, introspectedTable, introspectedColumn);
                    }
                }
            }
        }

        if (maxColumns != null && !"".equals(maxColumns)) {
            for (String column : maxColumns.split(DELIMITER)) {
                for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
                    if (column.trim().equals(introspectedColumn.getActualColumnName())) {
                        addMaxMethod(interfaze, introspectedTable, introspectedColumn);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        List<Element> els = document.getRootElement().getElements();

        String paramType = introspectedTable.getExampleType();
        String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        if(!tableName.contains("_${tableNameSuffix}") && SliceTablePlugin.needPartition(introspectedTable)) {
            tableName = tableName + "_${tableNameSuffix}";
        }

        String minColumns = introspectedTable.getTableConfigurationProperty(MIN_COLUMNS);
        String maxColumns = introspectedTable.getTableConfigurationProperty(MAX_COLUMNS);

        if (minColumns != null && !"".equals(minColumns)) {
            for (String column : minColumns.split(DELIMITER)) {
                for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
                    if (column.trim().equals(introspectedColumn.getActualColumnName())) {
                        String field = makeFieldName(introspectedColumn);
                        String returnType = introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName();
                        String xml = MessageFormat.format(templateMin, field, paramType, returnType, column, tableName);
                        els.add(new TextElement(xml));
                    }
                }
            }
        }

        if (maxColumns != null && !"".equals(maxColumns)) {
            for (String column : maxColumns.split(DELIMITER)) {
                for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
                    if (column.trim().equals(introspectedColumn.getActualColumnName())) {
                        String field = makeFieldName(introspectedColumn);
                        String returnType = introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName();
                        String xml = MessageFormat.format(templateMax, field, paramType, returnType, column, tableName);
                        els.add(new TextElement(xml));
                    }
                }
            }
        }

        return true;
    }

    private void addMinMethod(Interface interfaze, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        String mName = "min" + makeFieldName(introspectedColumn) + "ByExample";
        FullyQualifiedJavaType paramType = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        FullyQualifiedJavaType returnType = introspectedColumn.getFullyQualifiedJavaType();
        Method method = new Method();
        method.setName(mName);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(paramType, "example"));
        method.setReturnType(returnType);
        interfaze.addMethod(method);
        String importType = returnType.getFullyQualifiedName();
        if (!importType.startsWith("java.lang")) {
            interfaze.addImportedType(returnType);
        }
        System.out.println("-----------------" + interfaze.getType().getShortName() + " add method " + mName + ".");
    }

    private void addMaxMethod(Interface interfaze, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        String mName = "max" + makeFieldName(introspectedColumn) + "ByExample";
        FullyQualifiedJavaType paramType = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        FullyQualifiedJavaType returnType = introspectedColumn.getFullyQualifiedJavaType();
        Method method = new Method();
        method.setName(mName);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(paramType, "example"));
        method.setReturnType(returnType);
        interfaze.addMethod(method);
        String importType = returnType.getFullyQualifiedName();
        if (!importType.startsWith("java.lang")) {
            interfaze.addImportedType(returnType);
        }
        System.out.println("-----------------" + interfaze.getType().getShortName() + " add method " + mName + ".");
    }

    private String makeFieldName(IntrospectedColumn introspectedColumn) {
        String field = introspectedColumn.getJavaProperty();
        field = field.substring(0, 1).toUpperCase() + field.substring(1, field.length() - 1);
        return field;
    }

    private static final String DELIMITER = ",";
    private final static String MIN_COLUMNS = "minColumns";
    private final static String MAX_COLUMNS = "maxColumns";

    private static final String templateMin = "" +
            "<select id=\"min{0}ByExample\" parameterType=\"{1}\" resultType=\"{2}\">\n" +
            "    select min({3}) from {4}\n" +
            "    <if test=\"_parameter != null\" >\n" +
            "      <include refid=\"Example_Where_Clause\" />\n" +
            "    </if>\n" +
            "  </select>";
    private static final String templateMax = "" +
            "<select id=\"max{0}ByExample\" parameterType=\"{1}\"  resultType=\"{2}\">\n" +
            "    select max({3}) from {4}\n" +
            "    <if test=\"_parameter != null\" >\n" +
            "      <include refid=\"Example_Where_Clause\" />\n" +
            "    </if>\n" +
            "  </select>";
}

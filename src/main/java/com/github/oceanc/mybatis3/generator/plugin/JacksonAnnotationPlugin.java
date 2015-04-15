package com.github.oceanc.mybatis3.generator.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * Created by chengyang
 */
public class JacksonAnnotationPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String prop = annotateProperty(introspectedColumn, introspectedTable);
        if (prop != null) {
            String ann = "@JsonProperty(\"" + prop + "\")";
            field.addAnnotation(ann);
            topLevelClass.addImportedType(new FullyQualifiedJavaType("com.fasterxml.jackson.annotation.JsonProperty"));
            System.out.println("-----------------" + topLevelClass.getType().getShortName() + " add field annotation " + ann + " to " + field.getName() + " implement by Jackson2.");
        }
        String format = annotateFormat(introspectedColumn, introspectedTable);
        if (format != null) {
            String ann ="@JsonFormat(pattern = \"" + format + "\")";
            field.addAnnotation(ann);
            topLevelClass.addImportedType(new FullyQualifiedJavaType("com.fasterxml.jackson.annotation.JsonFormat"));
            System.out.println("-----------------" + topLevelClass.getType().getShortName() + " add field annotation " + ann + " to " + field.getName() + " implement by Jackson2.");
        }
        annotateIgnore(field, topLevelClass, introspectedColumn, introspectedTable);
        return true;
    }

    private String annotateProperty(IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable) {
        String cls = introspectedTable.getTableConfigurationProperty(JACKSON_COLUMNS);
        if (cls != null && !"".equals(cls)) {
            String[] columns = cls.split(DELIMITER);
            for (int i = 0; i < columns.length; i++) {
                if (columns[i].trim().equals(introspectedColumn.getActualColumnName())) {
                    String pps = introspectedTable.getTableConfigurationProperty(JACKSON_PROPERTIES);
                    return pps.split(DELIMITER)[i].trim();
                }
            }
        }
        return null;
    }

    private String annotateFormat(IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable) {
        String fmts = introspectedTable.getTableConfigurationProperty(JACKSON_FORMATS);
        if (fmts != null && !"".equals(fmts)) {
            String[] formats = fmts.split(DELIMITER_FORMAT);
            String anno = formats[1];
            String[] columns = formats[0].split(DELIMITER);
            for (String column : columns) {
                if (column.equals(introspectedColumn.getActualColumnName())) {
                    return anno;
                }
            }
        }
        return null;
    }

    private void annotateIgnore(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable) {
        String cls = introspectedTable.getTableConfigurationProperty(JACKSON_IGNORES);
        if (cls != null && !"".equals(cls)) {
            String[] columns = cls.split(DELIMITER);
            for (String column : columns) {
                if (column.trim().equals(introspectedColumn.getActualColumnName())) {
                    field.addAnnotation("@JsonIgnore");
                    topLevelClass.addImportedType(new FullyQualifiedJavaType("com.fasterxml.jackson.annotation.JsonIgnore"));
                    System.out.println("-----------------" + topLevelClass.getType().getShortName() + " add field annotation @JsonIgnore to " + field.getName() + " implement by Jackson2.");
                }
            }
        }
    }

    private static final String DELIMITER = ",";
    private static final String DELIMITER_FORMAT = "@";

    private static final String JACKSON_COLUMNS = "jacksonColumns";
    private static final String JACKSON_PROPERTIES = "jacksonProperties";
    private static final String JACKSON_FORMATS = "jacksonFormats";
    private static final String JACKSON_IGNORES = "jacksonIgnores";
}

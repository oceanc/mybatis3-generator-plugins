package oceanc.generator.mybatis3.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.TableConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by chengyang
 */
public class SliceTablePlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        if (needPartition(introspectedTable)) {
            String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
            String modValue = introspectedTable.getTableConfigurationProperty(MOD_VALUE);
            String month = introspectedTable.getTableConfigurationProperty(TIME_VALUE);
            if ((modValue != null && !"".equals(modValue)) || (month != null && !"".equals(month))) {
                String baseName = tableName.substring(0, tableName.lastIndexOf(UNDERLINE));
                introspectedTable.setSqlMapAliasedFullyQualifiedRuntimeTableName(baseName + SUFFIX);
                introspectedTable.setSqlMapFullyQualifiedRuntimeTableName(baseName + SUFFIX);
            }
        }
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (needPartition(introspectedTable)) {
            String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
            String relColumn = introspectedTable.getTableConfigurationProperty(REL_COLUMN);
            String modValue = introspectedTable.getTableConfigurationProperty(MOD_VALUE);
            String month = introspectedTable.getTableConfigurationProperty(TIME_VALUE);
            if (modValue != null && !"".equals(modValue)) {
                String fieldName = convertColumnName(relColumn, introspectedTable.getTableConfiguration());
                String[] expression = new String[12];
                expression[0] = "if (" + fieldName + " != null ) {";
                expression[1] = "long nan = 0;";
                expression[2] = "StringBuilder sb = new StringBuilder(\"0\");";
                expression[3] = "for (char c : " + fieldName + ".toCharArray()) {";
                expression[4] = "if (Character.isDigit(c)) sb.append(c);";
                expression[5] = "else nan += c;";
                expression[6] = "}";
                expression[7] = "long lid = Long.parseLong(sb.toString());";
                expression[8] = "if(nan > 0) lid += " + modValue + " + nan;";
                expression[9] = "this." + SUFFIX_FIELD + " = (Math.abs(lid) % " + modValue + ") + \"\";";
                expression[10] = "}";
                expression[11] = "return this;";
                Method method = makePartitionMethod(STRING_TYPE, topLevelClass.getType(), fieldName, tableName, expression);
                topLevelClass.addMethod(method);
                log.debug("{} add method {}.", topLevelClass.getType().getShortName(), method.getName());
                addSuffixProperty(topLevelClass, tableName);
            } else if (month != null && !"".equals(month)) {
                String fieldName = convertColumnName(relColumn, introspectedTable.getTableConfiguration());
                String[] expression = new String[7];
                expression[0] = "if (" + fieldName + " != null ) {";
                expression[1] = "Calendar calendar = Calendar.getInstance();";
                expression[2] = "calendar.setTimeInMillis(" + fieldName + ".getTime());";
                expression[3] = "int m = calendar.get(Calendar.MONTH) + 1;";
                expression[4] = "this." + SUFFIX_FIELD + " = calendar.get(Calendar.YEAR) + (m < 10 ? \"0\" + m : \"\" + m);";
                expression[5] = "}";
                expression[6] = "return this;";
                Method method = makePartitionMethod(DATE_TYPE, topLevelClass.getType(), fieldName, tableName, expression);
                topLevelClass.addImportedType(CALENDAR_TYPE);
                topLevelClass.addMethod(method);
                log.debug("{} add method {}.", topLevelClass.getType().getShortName(), method.getName());
                addSuffixProperty(topLevelClass, tableName);
            }
        }
        return true;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        if (needPartition(introspectedTable)) {
            String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
            String relColumn = introspectedTable.getTableConfigurationProperty(REL_COLUMN);

            if (introspectedColumn.getActualColumnName().equals(relColumn)) {
                String modValue = introspectedTable.getTableConfigurationProperty(MOD_VALUE);
                String month = introspectedTable.getTableConfigurationProperty(TIME_VALUE);
                String field = introspectedColumn.getJavaProperty();
                if (modValue != null && !"".equals(modValue)) {
                    if (!isPrime(Long.parseLong(modValue))) {
                        System.err.printf("modValue should be a prime number!!!!!!");
                        throw new IllegalArgumentException("modValue not a prime number!!!!!!");
                    }
                    String[] expression = new String[11];
                    expression[0] = "if (this." + field + " != null ) {";
                    expression[1] = "long nan = 0;";
                    expression[2] = "StringBuilder sb = new StringBuilder(\"0\");";
                    expression[3] = "for (char c : " + field + ".toCharArray()) {";
                    expression[4] = "if (Character.isDigit(c)) sb.append(c);";
                    expression[5] = "else nan += c;";
                    expression[6] = "}";
                    expression[7] = "long lid = Long.parseLong(sb.toString());";
                    expression[8] = "if(nan > 0) lid += " + modValue + " + nan;";
                    expression[9] = "this." + SUFFIX_FIELD + " = (Math.abs(lid) % " + modValue + ") + \"\";";
                    expression[10] = "}";
                    method.addBodyLines(Arrays.asList(expression));
                    log.debug("{} modify method {} for update field {}", topLevelClass.getType().getShortName(), method.getName(), SUFFIX_FIELD);
                    addSuffixProperty(topLevelClass, tableName);
                } else if (month != null && !"".equals(month)) {
                    int mc = Integer.parseInt(month);
                    if (mc < 1 || mc > 12) {
                        System.err.printf("month value should in [1-12]!!!!!!");
                        throw new IllegalArgumentException("month value should in [1-12]!!!!!!");
                    }
                    String[] expression = new String[5];
                    expression[0] = "if (this." + field + " != null ) {";
                    expression[1] = "Calendar calendar = Calendar.getInstance();";
                    expression[2] = "int m = calendar.get(Calendar.MONTH) + 1;";
                    expression[3] = "this." + SUFFIX_FIELD + " = calendar.get(Calendar.YEAR) + (m < 10 ? \"0\" + m : \"\" + m);";
                    expression[4] = "}";
                    method.addBodyLines(Arrays.asList(expression));
                    topLevelClass.addImportedType(CALENDAR_TYPE);
                    log.debug("{} modify method {} for update field {}", topLevelClass.getType().getShortName(), method.getName(), SUFFIX_FIELD);
                    addSuffixProperty(topLevelClass, tableName);
                }
            }
        }
        return true;
    }

    @Override
    public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return this.dynamicTableName(element, introspectedTable);
    }

    @Override
    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return this.dynamicTableName(element, introspectedTable);
    }

    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        log.debug("{} replace parameter type to {} of SelectByPrimaryKey in sql xml", introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime(), introspectedTable.getBaseRecordType());
        return this.replaceParamType(element, introspectedTable);
    }

    @Override
    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        log.debug("{} replace parameter type to {} of DeleteByPrimaryKey in sql xml", introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime(), introspectedTable.getBaseRecordType());
        return this.replaceParamType(element, introspectedTable);
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        log.debug("{} replace parameter type to {} of SelectByPrimaryKey in client class' method {}", interfaze.getType().getShortName(), introspectedTable.getBaseRecordType(), method.getName());
        return this.replaceParamType(method, introspectedTable);
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        log.debug("{} replace parameter type to {} of DeleteByPrimaryKey in client class' method {}", interfaze.getType().getShortName(), introspectedTable.getBaseRecordType(), method.getName());
        return this.replaceParamType(method, introspectedTable);
    }

    private void addSuffixProperty(TopLevelClass topLevelClass, String tableName) {
        for (Method method : topLevelClass.getMethods()) {
            if (method.getName().equals("clear")) {
                method.addBodyLine("this." + SUFFIX_FIELD + " = null;");
            }
        }
        topLevelClass.addField(this.makeStringField(SUFFIX_FIELD, tableName));
        topLevelClass.addMethod(this.makeGetterStringMethod(SUFFIX_FIELD, tableName));
        log.debug("{} add field {} and getter related.", topLevelClass.getType().getShortName(), SUFFIX_FIELD);
    }

    private Field makeStringField(String fieldName, String tableName) {
        Field field = new Field();
        field.setName(fieldName);
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(FullyQualifiedJavaType.getStringInstance());
        this.addDoc(field, tableName);
        return field;
    }

    private Method makeGetterStringMethod(String fieldName, String tableName) {
        String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
        Method method = new Method();
        method.setName(methodName);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.addBodyLine("return this." + fieldName + ";");
        this.addDoc(method, tableName);
        return method;
    }

    private Method makePartitionMethod(FullyQualifiedJavaType paramType, FullyQualifiedJavaType returnType, String fieldName, String tableName, String[] exp) {
        String methodName = PARTITION_FACTOR + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
        Method method = new Method();
        method.setName(methodName);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(returnType);
        method.addBodyLines(Arrays.asList(exp));
        method.addParameter(new Parameter(paramType, fieldName));
        this.addDoc(method, tableName);
        return method;
    }

    private void addDoc(JavaElement element, String tableName) {
        String suppressAllComments = this.getContext().getCommentGeneratorConfiguration().getProperty("suppressAllComments");
        if (!"true".equals(suppressAllComments)) {
            String type = element.getClass() == Field.class ? "field" : "method";
            element.addJavaDocLine("/**");
            element.addJavaDocLine("* This " + type + " was generated by MyBatis Generator.");
            element.addJavaDocLine("* This " + type + " corresponds to the database table " + tableName);
            element.addJavaDocLine("*");
            element.addJavaDocLine("* @mbggenerated " + df.format(new Date()));
            element.addJavaDocLine("*/");
        }
    }

    private boolean needPartition(IntrospectedTable introspectedTable) {
        String relColumn = introspectedTable.getTableConfigurationProperty(REL_COLUMN);
        return relColumn != null && !"".equals(relColumn);
    }

    private String convertColumnName(String column, TableConfiguration configuration) {
        String name = camelcase(column.split(UNDERLINE));
        if (configuration.getColumnOverride(column) != null) {
            name = configuration.getColumnOverride(column).getJavaProperty();
        } else if(configuration.getColumnRenamingRule() != null) {
            String search = configuration.getColumnRenamingRule().getSearchString();
            String replace = configuration.getColumnRenamingRule().getReplaceString();
            name = column.replaceAll(search, replace);
        }
        return name;
    }

    private static String camelcase(String[] words) {
        StringBuilder sb = new StringBuilder(words[0].toLowerCase());
        for (int i = 1; i < words.length; i ++) {
            sb.append(words[i].substring(0, 1));
            if (words[i].length() > 1) {
                sb.append(words[i].substring(1, words[i].length()).toLowerCase());
            }
        }
        return sb.toString();
    }

    private boolean replaceParamType(Method method, IntrospectedTable introspectedTable) {
        if (needPartition(introspectedTable)) {
            for (Parameter parameter : method.getParameters()) {
                try {
                    String classType = introspectedTable.getBaseRecordType();
                    java.lang.reflect.Field field = parameter.getClass().getDeclaredField("name");
                    field.setAccessible(true);
                    field.set(parameter, "record");

                    field = parameter.getClass().getDeclaredField("type");
                    field.setAccessible(true);
                    field.set(parameter, new FullyQualifiedJavaType(classType));
                } catch (NoSuchFieldException e) {
                    log.error("replace parameter type error", e);
                } catch (IllegalAccessException e) {
                    log.error("replace parameter type error", e);
                }
            }
        }
        return true;
    }

    private boolean replaceParamType(XmlElement element, IntrospectedTable introspectedTable) {
        if (needPartition(introspectedTable)) {
            for (Attribute attribute : element.getAttributes()) {
                if (SQL_MAP_PARAMETER_TYPE.equals(attribute.getName())) {
                    try {
                        String classType = introspectedTable.getBaseRecordType();
                        java.lang.reflect.Field field = attribute.getClass().getDeclaredField("value");
                        field.setAccessible(true);
                        field.set(attribute, classType);
                    } catch (NoSuchFieldException e) {
                        log.error("replace parameter type error", e);
                    } catch (IllegalAccessException e) {
                        log.error("replace parameter type error", e);
                    }
                }
            }
        }
        return true;
    }

    private boolean dynamicTableName(XmlElement element, IntrospectedTable introspectedTable) {
        if (needPartition(introspectedTable)) {
            TextElement sqlhead = (TextElement) element.getElements().get(0);
            try {
                String dynamicTableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();
                String baseName = dynamicTableName.substring(0, dynamicTableName.lastIndexOf(UNDERLINE));

                StringBuilder sfx = new StringBuilder("<choose>");
                sfx.append("<when test=\"record.").append(SUFFIX_FIELD).append(" != null\">").append(baseName).append("_${record.").append(SUFFIX_FIELD).append("}</when>");
                sfx.append("<when test=\"example.").append(SUFFIX_FIELD).append(" != null\">").append(baseName).append("_${example.").append(SUFFIX_FIELD).append("}</when>");
                sfx.append("<otherwise>").append(baseName).append("_ </otherwise>");
                sfx.append("</choose>");

                java.lang.reflect.Field field = sqlhead.getClass().getDeclaredField("content");
                field.setAccessible(true);
                field.set(sqlhead, "update " + sfx.toString());

                log.debug("generate dynamic table name base on {} in sql xml", baseName);
            } catch (NoSuchFieldException e) {
                log.error("generate dynamic table name error", e);
            } catch (IllegalAccessException e) {
                log.error("generate dynamic table name error", e);
            }
        }
        return true;
    }

    private static boolean isPrime(long N) {
        if (N < 2) return false;
        for (int i = 2; i * i <= N; i++) {
            if (N % i == 0) return false;
        }
        return true;
    }

    private Logger log = LoggerFactory.getLogger(SliceTablePlugin.class);

    private final SimpleDateFormat df = new SimpleDateFormat("EEE MMM ww HH:mm:ss z yyyy", Locale.US);

    private final static String REL_COLUMN = "sliceColumn";
    private final static String MOD_VALUE = "sliceMod";
    private final static String TIME_VALUE = "sliceMonth";
    private final static String SUFFIX_FIELD = "tableNameSuffix";

    private final static FullyQualifiedJavaType STRING_TYPE = new FullyQualifiedJavaType("java.lang.String");
    private final static FullyQualifiedJavaType DATE_TYPE = new FullyQualifiedJavaType("java.util.Date");
    private final static FullyQualifiedJavaType CALENDAR_TYPE = new FullyQualifiedJavaType("java.util.Calendar");

    private final static String UNDERLINE = "_";
    private final static String PARTITION_FACTOR = "partitionFactor";
    private final static String SUFFIX = "_${" + SUFFIX_FIELD + "}";

    private final static String SQL_MAP_PARAMETER_TYPE = "parameterType";

}

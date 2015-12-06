package com.github.oceanc.mybatis3.generator.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

/**
 * Created by chengyang
 */
public class OptimisticLockAutoIncreasePlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        String column = introspectedTable.getTableConfigurationProperty(OPTIMISTIC_LOCK_COLUMN);
        if (column != null && !"".equals(column)) {
            int index = -1;
            XmlElement sets = null;
            XmlElement setVs = null;
            for (Element el : element.getElements()) {
                if (el.getClass() == XmlElement.class) {
                    XmlElement xl = (XmlElement) el;
                    for (Attribute attr : xl.getAttributes()) {
                        if (attr.getName().equals("prefix") && attr.getValue().equals("(")) {
                            sets = xl;
                        } else if (attr.getName().equals("prefix") && attr.getValue().equals("values (")) {
                            setVs = xl;
                        }
                    }
                }
            }
            if(sets != null && setVs != null) {
                List<Element> fields = sets.getElements();
                for (int i = 0; i < fields.size(); i++) {
                    List<Element> ctnts = ((XmlElement) fields.get(i)).getElements();
                    for (Element ctnt : ctnts) {
                        TextElement tx = (TextElement)ctnt;
                        if (tx.getContent().equals(column + ",")) {
                            index = i;
                            break;
                        }
                    }
                }
                if (index > -1) {
                    fields.remove(index);
                    fields.add(index, new TextElement(column + ","));
                    setVs.getElements().remove(index);
                    setVs.getElements().add(index, new TextElement(("0,")));
                }
            }
        }

        return true;
    }

    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        String column = introspectedTable.getTableConfigurationProperty(OPTIMISTIC_LOCK_COLUMN);
        if (column != null && !"".equals(column)) {
            int nodeIndex = -1;
            int fieldIndex = -1;
            List<Element> elements = element.getElements();
            int start = 0;
            for (int i = 0; i < elements.size(); i++) {
                if (elements.get(i).getClass() == TextElement.class) {
                    start = i;
                    break;
                }
            }
            int middle = (elements.size() - start) / 2;

            for (int i = start; i <= middle; i++) {
                if(fieldIndex > -1) {
                    break;
                }
                Element node = elements.get(i);
                if (node.getClass() == TextElement.class) {
                    TextElement sets = (TextElement) node;
                    String[] fields = sets.getContent().split(",");
                    for (int j = 0; j < fields.length; j++) {
                        if (column.equals(fields[j].trim()) || fields[j].trim().equals(column + ")")) {
                            fieldIndex = j;
                            nodeIndex = i;
                            break;
                        }
                    }
                }
            }
            int index = middle + nodeIndex;
            TextElement values = (TextElement)elements.get(index);
            boolean needComma = values.getContent().trim().lastIndexOf(",") == values.getContent().trim().length() - 1;
            boolean needRightBracket = values.getContent().trim().lastIndexOf(")") == values.getContent().trim().length() - 1;
             String[] temp = values.getContent().trim().split(",");
            String[] vs = new String[temp.length / 2];
            for (int i = 0; i < temp.length; i+=2) {
                vs[i / 2] = temp[i] + "," + temp[i + 1];
            }
            vs[fieldIndex] = "0";
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < vs.length; i++) {
                sb.append(vs[i].trim());
                if (i != vs.length - 1) {
                    sb.append(", ");
                }
            }
            if (needComma) {
                sb.append(", ");
            }
            if (needRightBracket) {
                sb.append(")");
            }
            elements.remove(index);
            elements.add(index, new TextElement(sb.toString()));
        }
        return true;
    }

    @Override
    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        this.replaceSelective(element, introspectedTable);
        return true;
    }

    @Override
    public boolean sqlMapUpdateByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        this.replace(element, introspectedTable);
        return true;
    }

    @Override
    public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        this.replace(element, introspectedTable);
        return true;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        this.replaceSelective(element, introspectedTable);
        return true;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        this.replace(element, introspectedTable);
        return true;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        this.replace(element, introspectedTable);
        return true;
    }

    private void replaceSelective(XmlElement element, IntrospectedTable introspectedTable) {
        String column = introspectedTable.getTableConfigurationProperty(OPTIMISTIC_LOCK_COLUMN);
        if (column != null && !"".equals(column)) {
            Element set = element.getElements().get(1);
            XmlElement setXml = (XmlElement) set;
            List<Element> fields = setXml.getElements();
            int index = -1;
            String newSql = null;
            for (int i = 0; i < fields.size(); i++) {
                if (index > -1) {
                    break;
                }
                if (fields.get(i).getClass() == XmlElement.class) {
                    XmlElement fieldXml = (XmlElement) fields.get(i);

                    List<Element> elements = fieldXml.getElements();
                    for (Element element1 : elements) {
                        if (element1.getClass() == TextElement.class) {
                            TextElement oldSql = (TextElement) element1;
                            if (oldSql.getContent().contains(column + " = ")) {
                                index = i;
                                String comma = oldSql.getContent().lastIndexOf(",") == oldSql.getContent().length() - 1 ? "," : "";
                                String[] sqlArr = oldSql.getContent().split(column);
                                newSql = sqlArr[0] + column + " = " + column + " + 1" + comma;
                                break;
                            }
                        }
                    }
                }
            }
            if (index > -1) {
                fields.remove(index);
                fields.add(index, new TextElement(newSql));
            }
        }
    }

    private void replace(XmlElement element, IntrospectedTable introspectedTable) {
        String column = introspectedTable.getTableConfigurationProperty(OPTIMISTIC_LOCK_COLUMN);
        if (column != null && !"".equals(column)) {
            List<Element> elements = element.getElements();
            int index = -1;
            String newsql = null;
            for (int i = 0; i < elements.size(); i++) {
                if (elements.get(i).getClass() == TextElement.class) {
                    TextElement setSql = (TextElement) elements.get(i);
                    if (setSql.getContent().contains(column + " = ")) {
                        index = i;
                        String comma = setSql.getContent().lastIndexOf(",") == setSql.getContent().length() - 1 ? "," : "";
                        String[] sqlArr = setSql.getContent().split(column);
                        newsql = sqlArr[0] + column + " = " + column + " + 1" + comma;
                        break;
                    }
                }
            }
            if (index > -1) {
                elements.remove(index);
                elements.add(index, new TextElement(newsql));
            }
        }
    }

    private static final String OPTIMISTIC_LOCK_COLUMN = "optimisticLockColumn";
}

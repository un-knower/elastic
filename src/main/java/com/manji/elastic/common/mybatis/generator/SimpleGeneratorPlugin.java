package com.manji.elastic.common.mybatis.generator;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

public class SimpleGeneratorPlugin extends PluginAdapter {

    private static final String EXAMPLE_ROOT_CLASS = "exampleRootClass";

    private static final String CLIENT_ROOT_INTERFACE = "clientRootInterface";

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String rootInterface = context.getJavaClientGeneratorConfiguration().getProperty(CLIENT_ROOT_INTERFACE);
        if (stringHasValue(rootInterface)) {
            rootInterface += "<"
                    + introspectedTable.getBaseRecordType() + ","
                    + introspectedTable.getExampleType() + ","
                    + getPrimaryKeyType(introspectedTable)
                    + ">";
            FullyQualifiedJavaType superInterface = new FullyQualifiedJavaType(rootInterface);
            interfaze.addSuperInterface(superInterface);
            interfaze.addImportedType(superInterface);
        }
        interfaze.getMethods().clear();
        //interfaze.getImportedTypes().remove(new FullyQualifiedJavaType("java.util.List"));
        //interfaze.getImportedTypes().remove(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String rootClass = context.getJavaModelGeneratorConfiguration().getProperty(EXAMPLE_ROOT_CLASS);
        if (stringHasValue(rootClass)) {
            FullyQualifiedJavaType superClass = new FullyQualifiedJavaType(rootClass);
            topLevelClass.setSuperClass(superClass);
            topLevelClass.addImportedType(superClass);
        }
        topLevelClass.getInnerClasses().remove(2);// remove Criterion

        List<Method> methods = topLevelClass.getInnerClasses().get(0).getMethods();
        List<Method> newMethods = new ArrayList<Method>();
        for (Method method : methods) {
            if (!"addCriterion".equals(method.getName())) {
                newMethods.add(method);
            }
        }
        topLevelClass.getInnerClasses().get(0).getMethods().clear();
        addCriterionMethods(topLevelClass.getInnerClasses().get(0));
        topLevelClass.getInnerClasses().get(0).getMethods().addAll(newMethods);

        CommentGenerator commentGenerator = context.getCommentGenerator();

        Method method = createMethod(JavaVisibility.PUBLIC, FullyQualifiedJavaType.getCriteriaInstance(), "getCriteria");
        method.addBodyLine("if (oredCriteria.size() == 0) {createCriteria();}");
        method.addBodyLine("return oredCriteria.get(0);");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        method = createMethod(JavaVisibility.PUBLIC, null, "addCriteria");
        method.addParameter(new Parameter(new FullyQualifiedJavaType("java.util.List<Criterion>"), "criteria"));
        method.addBodyLine("getCriteria().getCriteria().addAll(criteria);");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }

    private void addCriterionMethods(InnerClass innerClass) {

        innerClass.addField(createField(JavaVisibility.PRIVATE, new FullyQualifiedJavaType("boolean"), "ignoreNull"));

        Method method = createMethod(JavaVisibility.PRIVATE, new FullyQualifiedJavaType("boolean"), "isIgnoreNull");
        method.addBodyLine("return ignoreNull;");
        innerClass.addMethod(method);

        method = createMethod(JavaVisibility.PUBLIC, new FullyQualifiedJavaType("Criteria"), "ignoreNull");
        method.addBodyLine("this.ignoreNull = true;");
        method.addBodyLine("return (Criteria) this;");
        innerClass.addMethod(method);

        method = createMethod(JavaVisibility.PROTECTED, null, "addCriterion");
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition"));
        method.addBodyLine("if (condition == null) {");
        method.addBodyLine("if (!isIgnoreNull()) {");
        method.addBodyLine("throw new RuntimeException(\"Value for condition cannot be null\");");
        method.addBodyLine("}} else {");
        method.addBodyLine("criteria.add(new Criterion(condition));}");
        innerClass.addMethod(method);

        method = createMethod(JavaVisibility.PROTECTED, null, "addCriterion");
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "value"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property"));
        method.addBodyLine("if (value == null) {");
        method.addBodyLine("if (!isIgnoreNull()) {");
        method.addBodyLine("throw new RuntimeException(\"Value for \" + property + \" cannot be null\");");
        method.addBodyLine("}} else {");
        method.addBodyLine("criteria.add(new Criterion(condition, value));}");
        innerClass.addMethod(method);

        method = createMethod(JavaVisibility.PROTECTED, null, "addCriterion");
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "value1"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "value2"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property"));
        method.addBodyLine("if (value1 == null || value2 == null) {");
        method.addBodyLine("if (!isIgnoreNull()) {");
        method.addBodyLine("throw new RuntimeException(\"Between values for \" + property + \" cannot be null\");");
        method.addBodyLine("}} else {");
        method.addBodyLine("criteria.add(new Criterion(condition, value1, value2));}");
        innerClass.addMethod(method);
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        topLevelClass.addImportedType("com.wordnik.swagger.annotations.ApiModelProperty");
        method.addAnnotation("@ApiModelProperty(value = \"" + introspectedColumn.getRemarks().replace('"', '\'') + "\")");
        return super.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
    	addQueryExpand(element);
    	addGroupByClause(element);
        addLimit(element);
        return super.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
    }

    private FullyQualifiedJavaType getPrimaryKeyType(IntrospectedTable introspectedTable) {
        return introspectedTable.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType();
    }
    
    private void addQueryExpand(XmlElement element) {
    	int last = element.getElements().size() - 1;
    	Element groupByClause = element.getElements().get(last);
    	element.getElements().remove(last);
    	
    	XmlElement ifElement = new XmlElement("if");
    	ifElement.addAttribute(new Attribute("test", "queryExpand != null"));
    	ifElement.addElement(new TextElement("${queryExpand}"));
    	
    	element.addElement(ifElement);
    	element.addElement(groupByClause);
    }

    private void addLimit(XmlElement element) {
        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "limit gte 1 and offset gte 0"));
        ifElement.addElement(new TextElement("limit #{limit} offset #{offset}"));
        element.addElement(ifElement);
    }

    private void addGroupByClause(XmlElement element) {
        int last = element.getElements().size() - 1;
        Element orderByClause = element.getElements().get(last);
        element.getElements().remove(last);

        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "groupByClause != null"));
        ifElement.addElement(new TextElement("group by ${groupByClause}"));

        element.addElement(ifElement);
        element.addElement(orderByClause);
    }

    private Field createField(JavaVisibility visibility, FullyQualifiedJavaType fieldType, String fieldName) {
        Field field = new Field();
        field.setVisibility(visibility);
        field.setType(fieldType);
        field.setName(fieldName);
        return field;
    }

    private Method createMethod(JavaVisibility visibility, FullyQualifiedJavaType returnType, String methodName) {
        return createMethod(visibility, returnType, methodName, null, null);
    }

    private Method createMethod(JavaVisibility visibility, FullyQualifiedJavaType returnType, String methodName, Parameter parameter, String bodyLine) {
        Method method = new Method();
        method.setVisibility(visibility);
        if (returnType != null) {
            method.setReturnType(returnType);
        }
        method.setName(methodName);
        if (parameter != null) {
            method.addParameter(parameter);
        }
        if (stringHasValue(bodyLine)) {
            method.addBodyLine(bodyLine);
        }
        return method;
    }

}

package com.manji.elastic.common.mybatis.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.internal.DefaultCommentGenerator;

public class SimpleCommentGenerator extends DefaultCommentGenerator {

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        field.addJavaDocLine("/**");
        field.addJavaDocLine(" * " + introspectedColumn.getRemarks());
        addJavadocTag(field, false);
        field.addJavaDocLine(" */");
    }

}

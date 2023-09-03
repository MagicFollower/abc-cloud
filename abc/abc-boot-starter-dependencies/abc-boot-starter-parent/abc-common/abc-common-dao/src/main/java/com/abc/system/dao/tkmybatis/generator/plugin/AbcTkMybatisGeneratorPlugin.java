package com.abc.system.dao.tkmybatis.generator.plugin;

import com.abc.system.dao.tkmybatis.idgenerator.AbcTkGlobalIdGen;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import tk.mybatis.mapper.annotation.KeySql;

import java.util.List;

/**
 * mybatis自定义代码生成器（增强）
 * <pre>
 * 1.自动在实体类上添加lombok中的@FieldNameConstants
 * 2.自动在主键id上添加tok-mapper中的@KeySql(genId = AbcTkGlobalIdGen.class)
 * </pre>
 *
 * @Description -
 * @Author -
 * @Date 2023/9/3 12:43
 * @Version 1.0
 */
public class AbcTkMybatisGeneratorPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addImportedType(FieldNameConstants.class.getCanonicalName());
        topLevelClass.addImportedType(EqualsAndHashCode.class.getCanonicalName());
        topLevelClass.addAnnotation("@FieldNameConstants");
        topLevelClass.addAnnotation("@EqualsAndHashCode(callSuper = true)");
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass,
                                       IntrospectedColumn introspectedColumn,
                                       org.mybatis.generator.api.IntrospectedTable introspectedTable,
                                       ModelClassType modelClassType) {
        if (introspectedColumn.getActualColumnName().equals("id")) {
            topLevelClass.addImportedType(new FullyQualifiedJavaType(KeySql.class.getCanonicalName()));
            topLevelClass.addImportedType(new FullyQualifiedJavaType(AbcTkGlobalIdGen.class.getCanonicalName()));
            field.addAnnotation("@KeySql(genId = AbcTkGlobalIdGen.class)");
        }
        return true;
    }
}

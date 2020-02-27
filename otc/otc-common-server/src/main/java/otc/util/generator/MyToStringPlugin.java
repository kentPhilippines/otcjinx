package otc.util.generator;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class MyToStringPlugin extends PluginAdapter{
	 public MyToStringPlugin() {
	    }
	 
	    @Override
	    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
	        this.generateToString(introspectedTable, topLevelClass);
	        return true;
	    }
	    @Override
	    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
	        this.generateToString(introspectedTable, topLevelClass);
	        return true;
	    }
	    @Override
	    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
	        this.generateToString(introspectedTable, topLevelClass);
	        return true;
	    }
	 
	    private void generateToString(IntrospectedTable introspectedTable, TopLevelClass topLevelClass) {
	        Method method = new Method();
	        method.setVisibility(JavaVisibility.PUBLIC);
	        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
	        method.setName("toString");
	        if (introspectedTable.isJava5Targeted()) {
	            method.addAnnotation("@Override");
	        }
	        
	        //添加方法注释
	        this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
	 
	        //下面两句method.addBodyLine使用其中之一即可
	        
	        //使用自定义toString工具类
	        //method.addBodyLine("return ToStringUtils.toSimpleString(this);");
	        
	        //使用commons-lang3的工具类
	        //添加包
	        topLevelClass.addImportedType("org.apache.commons.lang3.builder.ToStringBuilder");
	        topLevelClass.addImportedType("org.apache.commons.lang3.builder.ToStringStyle");
	        method.addBodyLine("return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);");
	 
	        //添加方法
	        topLevelClass.addMethod(method);
	    }
	 
	 
	    /*
	    private void generateToString(IntrospectedTable introspectedTable, TopLevelClass topLevelClass) {
	        Method method = new Method();
	        method.setVisibility(JavaVisibility.PUBLIC);
	        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
	        method.setName("toString");
	        if (introspectedTable.isJava5Targeted()) {
	            method.addAnnotation("@Override");
	        }
	        this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
	        method.addBodyLine("StringBuilder sb = new StringBuilder();");
	        method.addBodyLine("sb.append(getClass().getSimpleName());");
	        method.addBodyLine("sb.append(\" [\");");
	        StringBuilder sb = new StringBuilder();
	        Iterator i$ = topLevelClass.getFields().iterator();
	        while(i$.hasNext()) {
	            Field field = (Field)i$.next();
	            String property = field.getName();
	            sb.setLength(0);
	            sb.append("sb.append(\"").append(", ").append(property).append("=\")").append(".append(").append(property).append(");");
	            method.addBodyLine(sb.toString());
	        }
	        method.addBodyLine("sb.append(\"]\");");
	        method.addBodyLine("return sb.toString();");
	        topLevelClass.addMethod(method);
	    }
	    */
	 
	    @Override
	    public boolean validate(List<String> warnings) {
	        return true;
	    }

}

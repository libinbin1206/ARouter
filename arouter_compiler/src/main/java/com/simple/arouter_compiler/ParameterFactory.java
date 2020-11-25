package com.simple.arouter_compiler;

import com.simple.arouter_annotation.Parameter;
import com.simple.arouter_compiler.utils.ProcessorConfig;
import com.simple.arouter_compiler.utils.ProcessorUtils;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/*
   目的 生成以下代码：
        @Override
        public void getParameter(Object targetParameter) {
              Personal_MainActivity t = (Personal_MainActivity) targetParameter;
              t.name = t.getIntent().getStringExtra("name");
              t.sex = t.getIntent().getStringExtra("sex");
        }
 */
public class ParameterFactory {


    // 方法的构建
    private MethodSpec.Builder method;

    // 类名，如：MainActivity  /  Personal_MainActivity
    private ClassName className;

    // Messager用来报告错误，警告和其他提示信息
    private Messager messager;

    private Elements elementUtils;

    // type(类信息)工具类，包含用于操作TypeMirror的工具方法
    private Types typeUtils;

    // 不想用户使用此构造函数，必须使用Builder设计模式
    private ParameterFactory(Builder builder) {
        this.messager = builder.messager;
        this.className = builder.className;

        // 生成此方法
        // 通过方法参数体构建方法体：public void getParameter(Object target) {
        method = MethodSpec.methodBuilder(ProcessorConfig.PARAMETER_METHOD_NAME)
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(builder.parameterSpec);
        this.elementUtils = builder.elementUtils;
        this.typeUtils = builder.typeUtils;
    }

    /**
     * 只有一行
     * Personal_MainActivity t = (Personal_MainActivity) targetParameter;
     */
    public void addFirstStatement() {
        method.addStatement("$T t = ($T) " + ProcessorConfig.PARAMETER_NAME, className, className);
    }

    public MethodSpec build() {
        return method.build();
    }

    /**
     * 多行 循环 复杂
     * 构建方体内容，如：t.s = t.getIntent.getStringExtra("s");
     *
     * @param element 被注解的属性元素
     */
    public void buildStatement(Element element) {
        // 遍历注解的属性节点 生成函数体
        TypeMirror typeMirror = element.asType();

        // 获取 TypeKind 枚举类型的序列号
        int type = typeMirror.getKind().ordinal();

        // 获取属性名  name  age  sex
        String fieldName = element.getSimpleName().toString();

        // 获取注解的值
        String annotationValue = element.getAnnotation(Parameter.class).name();

        // 配合： t.age = t.getIntent().getBooleanExtra("age", t.age ==  9);
        // 判断注解的值为空的情况下的处理（注解中有name值就用注解值）
        annotationValue = ProcessorUtils.isEmpty(annotationValue) ? fieldName : annotationValue;

        // TODO 最终拼接的前缀：
        String finalValue = "t." + fieldName;

        // t.s = t.getIntent().
        // TODO t.name = t.getIntent().getStringExtra("name");
        String methodContent = finalValue + " = t.getIntent().";

        // TypeKind 枚举类型不包含String
        if (type == TypeKind.BOOLEAN.ordinal()) {
            methodContent += "getBooleanExtra($S, " + finalValue + ")";
        } else if (type == TypeKind.BYTE.ordinal()) {
            methodContent += "getByteExtra($S, " + finalValue + ")";
        } else if (type == TypeKind.SHORT.ordinal()) {
            methodContent += "getShortExtra($S, " + finalValue + ")";
        } else if (type == TypeKind.INT.ordinal()) {
            methodContent += "getIntExtra($S, " + finalValue + ")";
        } else if (type == TypeKind.LONG.ordinal()) {
            methodContent += "getLongExtra($S, " + finalValue + ")";
        } else if (type == TypeKind.CHAR.ordinal()) {
            methodContent += "getCharExtra($S, " + finalValue + ")";
        } else if (type == TypeKind.FLOAT.ordinal()) {
            methodContent += "getFloatExtra($S, " + finalValue + ")";
        } else if (type == TypeKind.DOUBLE.ordinal()) {
            methodContent += "getDoubleExtra($S, " + finalValue + ")";
        } else {
            // 数组
            if (type == TypeKind.ARRAY.ordinal()) {
                methodContent = generateArrayContent(element);
            } else {
                //其他对象
                methodContent = generateObjectStatement(element);
            }
        }

        if (methodContent.contains("getParcelableArrayExtra($S)")) {

            //object数组 componentType获得object类型
            ArrayTypeName arrayTypeName = (ArrayTypeName) ClassName.get(typeMirror);
            TypeElement typeElement = elementUtils.getTypeElement(arrayTypeName
                .componentType.toString());

            method.addStatement(methodContent, ClassName.get(typeElement), annotationValue);
        } else if (methodContent.contains("Extra($S")) {
            // 参数二 9 赋值进去了
            // t.age = t.getIntent().getBooleanExtra("age", t.age ==  9);
            method.addStatement(methodContent, annotationValue);
        } else if (methodContent.contains("getInstance().build($S).navigation")) {
            method.addStatement(methodContent,
                TypeName.get(typeMirror),
                ClassName.get(ProcessorConfig.AROUTER_API_PACKAGE, ProcessorConfig.ROUTER_MANAGER),
                annotationValue);
        }
    }


    private String generateArrayContent(Element element) {
        TypeMirror typeMirror = element.asType();
        String fieldName = element.getSimpleName().toString();
        String finalValue = "t." + fieldName;

        // t.s = t.getIntent().
        // TODO t.name = t.getIntent().getStringExtra("name");
        String statement = finalValue + " = t.getIntent().";
        messager.printMessage(Diagnostic.Kind.NOTE, "typeMirror="+typeMirror.toString()+",,,");
        //数组
        switch (typeMirror.toString()) {
            case ProcessorConfig.BOOLEANARRAY:
                statement += "getBooleanArrayExtra($S)";
                break;
            case ProcessorConfig.INTARRAY:
                statement += "getIntArrayExtra($S)";
                break;
            case ProcessorConfig.SHORTARRAY:
                statement += "getShortArrayExtra($S)";
                break;
            case ProcessorConfig.FLOATARRAY:
                statement += "getFloatArrayExtra($S)";
                break;
            case ProcessorConfig.DOUBLEARRAY:
                statement += "getDoubleArrayExtra($S)";
                break;
            case ProcessorConfig.BYTEARRAY:
                statement += "getByteArrayExtra($S)";
                break;
            case ProcessorConfig.CHARARRAY:
                statement += "getCharArrayExtra($S)";
                break;
            case ProcessorConfig.LONGARRAY:
                statement += "getLongArrayExtra($S)";
                break;
            case ProcessorConfig.STRINGARRAY:
                statement += "getStringArrayExtra($S)";
                break;
            default:
                // t.testParcelables = (TestParcelable[]) t.getIntent().getParcelableArrayExtra("testParcelables");
                TypeMirror parcelableMirror = elementUtils.getTypeElement(ProcessorConfig.PARCELABLE).asType();
                //Parcelable 数组
                //object数组 componentType获得object类型
                ArrayTypeName arrayTypeName = (ArrayTypeName) ClassName.get(typeMirror);
                TypeElement typeElement = elementUtils.getTypeElement(arrayTypeName
                    .componentType.toString());
                //是否为 Parcelable 类型
                if (!typeUtils.isSubtype(typeElement.asType(), parcelableMirror)) {
                    throw new RuntimeException("Not Support Extra Type:" + typeMirror + " " +
                        element);
                }
                statement = "t." + fieldName + " = ($T[])" + " t.getIntent()" + ".getParcelableArrayExtra($S)";
        }
        return statement;
    }

    private String generateObjectStatement(Element element) {
        TypeMirror typeMirror = element.asType();
        String fieldName = element.getSimpleName().toString();
        String finalValue = "t." + fieldName;
        String methodContent = finalValue + " = t.getIntent().";
        TypeMirror callMirror = elementUtils.getTypeElement(ProcessorConfig.CALL).asType();
        TypeMirror serializableMirror = elementUtils.getTypeElement(Serializable.class.getName()).asType();
        TypeMirror parcelableMirror = elementUtils.getTypeElement(ProcessorConfig.PARCELABLE).asType();

        TypeName typeName = ClassName.get(typeMirror);

        if (typeMirror.toString().equalsIgnoreCase(ProcessorConfig.STRING)) {
            // String类型
            methodContent += "getStringExtra($S)"; // 没有默认值
        } else if (typeName instanceof ParameterizedTypeName) {
            //泛型
            //list 或 arraylist
            ClassName rawType = ((ParameterizedTypeName) typeName).rawType;
            //泛型类型
            List<TypeName> typeArguments = ((ParameterizedTypeName) typeName)
                .typeArguments;
            if (!rawType.toString().equals(ArrayList.class.getName()) && !rawType.toString()
                .equals(List.class.getName())) {
                throw new RuntimeException("Not Support Inject Type:" + typeMirror + " " +
                    element);
            }
            TypeName typeArgumentName = typeArguments.get(0);
            TypeElement typeElement = elementUtils.getTypeElement(typeArgumentName.toString());
            // Parcelable 类型
            if (typeUtils.isSubtype(typeElement.asType(), parcelableMirror)) {
                methodContent += "getParcelableArrayListExtra($S)";
            } else if (typeElement.asType().toString().equals(String.class.getName())) {
                methodContent += "getStringArrayListExtra($S)";
            } else if (typeElement.asType().toString().equals(Integer.class.getName())) {
                methodContent += "getIntegerArrayListExtra($S)";
            } else {
                throw new RuntimeException("Not Support Generic Type : " + typeMirror + " " +
                    element);
            }
        } else if (isSubType(typeMirror, serializableMirror)) {
            methodContent += "getSerializableExtra($S)";
        } else if (isSubType(typeMirror, parcelableMirror)) {
            methodContent += "getParcelableExtra($S)";
        } else if (isSubType(typeMirror, callMirror)) {
            // t.orderDrawable = (OrderDrawable) RouterManager.getInstance().build("/order/getDrawable").navigation(t);
            methodContent = "t." + fieldName + " = ($T) $T.getInstance().build($S).navigation(t)";
        }
        return methodContent;
    }

    private boolean isSubType(TypeMirror tm1, TypeMirror tm2) {
        return typeUtils.isSubtype(tm1, tm2);
    }

    /**
     * 为了完成Builder构建者设计模式
     */
    public static class Builder {

        // Messager用来报告错误，警告和其他提示信息
        private Messager messager;

        // 类名，如：MainActivity
        private ClassName className;

        // 方法参数体
        private ParameterSpec parameterSpec;

        // 操作Element工具类 (类、函数、属性都是Element)
        private Elements elementUtils;

        // type(类信息)工具类，包含用于操作TypeMirror的工具方法
        private Types typeUtils;

        public Builder(ParameterSpec parameterSpec) {
            this.parameterSpec = parameterSpec;
        }

        public Builder setMessager(Messager messager) {
            this.messager = messager;
            return this;
        }

        public Builder setElementUtils(Elements elementUtils) {
            this.elementUtils = elementUtils;
            return this;
        }

        public Builder setTypeUtils(Types typeUtils) {
            this.typeUtils = typeUtils;
            return this;
        }

        public Builder setClassName(ClassName className) {
            this.className = className;
            return this;
        }

        public ParameterFactory build() {
            if (parameterSpec == null) {
                throw new IllegalArgumentException("parameterSpec方法参数体为空");
            }

            if (className == null) {
                throw new IllegalArgumentException("方法内容中的className为空");
            }

            if (messager == null) {
                throw new IllegalArgumentException("messager为空，Messager用来报告错误、警告和其他提示信息");
            }

            return new ParameterFactory(this);
        }
    }
}

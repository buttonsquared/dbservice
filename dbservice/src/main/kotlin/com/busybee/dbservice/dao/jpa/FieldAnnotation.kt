package com.busybee.dbservice.dao.jpa

import com.busybee.dbservice.annotation.NotNullLookup
import com.busybee.dbservice.model.AbstractDatedModel
import com.busybee.dbservice.model.DatedModel
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

data class FieldAnnotation(
        val analyzedClass: Class<*>,
        var fieldMap: Map<String, Field> = mapOf<String, Field>(),
        var getMethodMap: Map<String, Method> = mapOf<String, Method>(),
        var setMethodMap: Map<String, Method> = mapOf<String, Method>(),
        var datedNbcModel: Map<String, Field> = mapOf<String, Field>(),
        var datedNbcModelCollection: Map<String, Field> = mapOf<String, Field>(),
        var datedNotNullNbcModelCollection: Map<String, Field> = mapOf<String, Field>(),
        var beanValidations: Map<String, List<Annotation>> = mapOf<String, List<Annotation>>(),
        var searchable: Map<String, Field> = mapOf<String, Field>()
) {

    init {
        datedNotNullNbcModelCollection = initDatedNotNullNbcModelCollection()
        datedNbcModelCollection = initNbcModelCollections()
        datedNbcModel = initNbcModels(analyzedClass)
        fieldMap = initRecursiveFieldInfo(analyzedClass)
        getMethodMap = initRecursiveGetMethodInfo(analyzedClass)
        setMethodMap = initRecursiveSetMethodInfo(analyzedClass)
        beanValidations = initBeanValidationMap(analyzedClass)
        searchable = initSearchableFields(analyzedClass)
    }

    private fun initNbcModelCollections(): Map<String, Field> {
        val result: MutableMap<String, Field> = HashMap()
        for (f in analyzedClass.declaredFields) {
            if (MutableCollection::class.java.isAssignableFrom(f.type) && f.genericType is ParameterizedType
                    && DatedModel::class.java.isAssignableFrom((f.genericType as ParameterizedType).actualTypeArguments[0] as Class<*>)) {
                result[f.name] = f
            }
        }
        return result
    }

    private fun initDatedNotNullNbcModelCollection(): Map<String, Field> {
        val result: MutableMap<String, Field> = java.util.HashMap()
        for (f in analyzedClass.declaredFields) {
            if (MutableCollection::class.java.isAssignableFrom(f.type) && f.genericType is ParameterizedType
                    && DatedModel::class.java.isAssignableFrom((f.genericType as ParameterizedType).actualTypeArguments[0] as Class<*>)
                    && f.getAnnotation(NotNullLookup::class.java) != null) {
                result[f.name] = f
            }
        }
        return result
    }

    private fun initNbcModels(classObj: Class<*>): Map<String, Field> {
        val result: MutableMap<String, Field> = java.util.HashMap()
        if (AbstractDatedModel::class.java.name != classObj.name && classObj.superclass != null) {
            result.putAll(initNbcModels(classObj.superclass))
        }
        for (f in classObj.declaredFields) {
            if (DatedModel::class.java.isAssignableFrom(f.type)) {
                result[f.name] = f
            }
        }
        return result
    }

    private fun initRecursiveFieldInfo(classObj: Class<*>): Map<String, Field> {
        val result: MutableMap<String, Field> = java.util.HashMap()
        if (AbstractDatedModel::class.java.name != classObj.name && classObj.superclass != null) {
            result.putAll(initRecursiveFieldInfo(classObj.superclass))
        }

        // Do it this way, so that the child classes' fields will 'hide' superclasses'
        // fields...
        for (f in classObj.declaredFields) {
            result[f.name] = f
        }
        return result
    }

    private fun initRecursiveGetMethodInfo(classObj: Class<*>): Map<String, Method> {
        val result: MutableMap<String, Method> = java.util.HashMap()
        if (AbstractDatedModel::class.java.name != classObj.name && classObj.superclass != null) {
            result.putAll(initRecursiveGetMethodInfo(classObj.superclass))
        }

        // Do it this way, so that the child classes' fields will 'hide' superclasses'
        // fields...
        for (m in classObj.declaredMethods) {
            if (m.name.startsWith("get")) {
                result[m.name.substring(3)] = m
            }
        }
        return result
    }

    private fun initRecursiveSetMethodInfo(classObj: Class<*>): Map<String, Method> {
        val result: MutableMap<String, Method> = java.util.HashMap()
        if (AbstractDatedModel::class.java.name != classObj.name && classObj.superclass != null) {
            result.putAll(initRecursiveSetMethodInfo(classObj.superclass))
        }

        // Do it this way, so that the child classes' fields will 'hide' superclasses'
        // fields...
        for (m in classObj.declaredMethods) {
            if (m.name.startsWith("set")) {
                result[m.name.substring(3)] = m
            }
        }
        return result
    }

    private fun initBeanValidationMap(classObj: Class<*>): Map<String, List<Annotation>> {
        val result: MutableMap<String, List<Annotation>> = java.util.HashMap()
        if (AbstractDatedModel::class.java.name != classObj.name && classObj.superclass != null) {
            result.putAll(initBeanValidationMap(classObj.superclass))
        }

        // Do it this way, so that the child classes' fields will 'hide' superclasses'
        // fields...
        for (f in classObj.declaredFields) {
            val validators: MutableList<Annotation> = ArrayList()
            for (annotation in f.annotations) {
                // Is this a bean validation class
                annotation.annotationClass.qualifiedName?.let {
                    if (it.startsWith("javax.validation.constraints")
                            || it.startsWith("org.hibernate.validator.constraints")) {
                        validators.add(annotation)
                    }
                }
            }
            result[f.name] = validators
        }
        return result
    }

    private fun initSearchableFields(classObj: Class<*>): Map<String, Field> {
        val result: MutableMap<String, Field> = java.util.HashMap()
        if (AbstractDatedModel::class.java.name != classObj.name && classObj.superclass != null) {
            result.putAll(initSearchableFields(classObj.superclass))
        }

        // Do it this way, so that the child classes' fields will 'hide' superclasses'
        // fields...
        for (f in classObj.declaredFields) {
            for (annotation in f.annotations) {
                // Is this a bean validation class
                if (annotation.annotationClass.qualifiedName == "com.commonlib.annotation.Searchable") {
                    result[f.name] = f
                    break
                }
            }
        }
        return result
    }

}
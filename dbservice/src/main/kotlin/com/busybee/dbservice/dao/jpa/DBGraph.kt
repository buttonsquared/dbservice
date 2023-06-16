package com.busybee.dbservice.dao.jpa

import jakarta.annotation.PostConstruct
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Service

@Service
class DBGraph(val em: EntityManager) {
    val entityMap = mutableMapOf<String, FieldAnnotation>()
    @PostConstruct
    fun init() {
        em.metamodel.entities.forEach {
            entityMap[it.bindableJavaType.name] = FieldAnnotation(it.bindableJavaType)
        }
    }

    fun getEntity(entityName: String): FieldAnnotation {
        return entityMap[entityName]!!
    }
}
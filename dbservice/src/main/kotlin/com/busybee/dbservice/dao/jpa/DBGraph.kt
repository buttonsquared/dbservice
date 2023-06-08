package com.busybee.dbservice.dao.jpa

import jakarta.annotation.PostConstruct
import jakarta.persistence.EntityManager
import org.hibernate.metamodel.model.domain.internal.EntityTypeImpl
import org.springframework.stereotype.Repository

@Repository
class DBGraph(val em: EntityManager) {
    @PostConstruct
    fun init() {
        em.metamodel.entities.forEach {
            println((it as EntityTypeImpl).pathName)
            println(it.javaClass)
        }
    }
}
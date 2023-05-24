package com.busybee.dbservice.dao.jpa

import com.busybee.dbservice.dao.Dao
import com.busybee.dbservice.dao.OrderBy
import com.busybee.dbservice.model.AppModel
import com.busybee.dbservice.model.DatedModel
import com.busybee.dbservice.model.SystemUser
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JpaDao(val em: EntityManager) : Dao {
    override fun <E : DatedModel> save(model: E, user: SystemUser, saveChildren: Boolean): E? {
        return saveModel(model, user)
    }

    private fun <E : DatedModel> saveModel(model: E, user: SystemUser): E {
        beforeUpdateOrCreate(model, user)
        return if (model.getId() == null) {
            em.persist(model)
            model
        } else {
            em.merge(model)
        }
    }

    private fun <E : DatedModel> beforeUpdateOrCreate(model: E, user: SystemUser) {
        model.setModifiedDate(Date().time)
        model.setModifiedBy(user)
        if (model.getId() == null) {
            model.setCreatedDate(Date().time)
            model.setCreatedBy(user)
        }
    }

    override fun <E : DatedModel> save(models: List<E>, user: SystemUser, saveChildren: Boolean): List<E> {
        TODO("Not yet implemented")
    }

    override fun permanentlyDelete(model: AppModel) {
        TODO("Not yet implemented")
    }

    override fun <E : AppModel> findById(type: Class<E>, id: Long): E? {
        TODO("Not yet implemented")
    }

    override fun <E : AppModel> findByPropertyKey(type: Class<E>, propertyKeyMap: Map<String, Any>): List<E> {
        TODO("Not yet implemented")
    }

    override fun <E : AppModel> findByPropertyKey(type: Class<E>, propertyKeyMap: Map<String, Any>, offset: Int?, maxResults: Int?, orderBy: Map<String, OrderBy>, distinctColumns: List<String>): List<E> {
        TODO("Not yet implemented")
    }

    override fun <E : AppModel> countByPropertyKey(type: Class<E>, propertyKeyMap: Map<String, Any>, distinct: List<String>): Int {
        TODO("Not yet implemented")
    }

    override fun <E : AppModel> findByPropertyKeyUnique(type: Class<E>, propertyKeyMap: Map<String, Any>, cacheResult: Boolean): E? {
        TODO("Not yet implemented")
    }

    override fun <E : AppModel> findEntityBySQLQuery(sql: String, params: Map<String, Any>, type: Class<E>): List<E> {
        TODO("Not yet implemented")
    }

    override fun findBySQLQuery(sql: String, params: Map<String, Any>, scalars: List<String>): List<Any> {
        TODO("Not yet implemented")
    }
}
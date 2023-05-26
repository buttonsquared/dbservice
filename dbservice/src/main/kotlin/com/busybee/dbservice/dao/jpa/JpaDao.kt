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
    override fun <E : AppModel> save(model: E, saveChildren: Boolean): E? {
        return saveModel(model)
    }
    override fun <E : DatedModel> save(model: E, user: SystemUser, saveChildren: Boolean): E? {
        return saveDatedModel(model, user)
    }

    private fun <E : DatedModel> saveDatedModel(model: E, user: SystemUser): E {
        beforeUpdateOrCreate(model, user)
        return saveModel(model)
    }

    private fun <E : AppModel> saveModel(model: E): E {
        return if (model.getModelId() == null) {
            em.persist(model)
            model
        } else {
            em.merge(model)
        }
    }

    private fun <E : DatedModel> beforeUpdateOrCreate(model: E, user: SystemUser) {
        model.setModelModifiedDate(Date().time)
        model.setModelModifiedBy(user.getUsername())
        if (model.getModelId() == null) {
            model.setModelCreatedBy(user.getUsername())
        }
    }

    override fun <E : DatedModel> save(models: List<E>, user: SystemUser, saveChildren: Boolean): List<E> {
        TODO("Not yet implemented")
    }

    override fun permanentlyDelete(model: AppModel) {
        TODO("Not yet implemented")
    }

    override fun <E : AppModel> findById(type: Class<E>, id: Long): E? {
        return em.find(type, id)
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
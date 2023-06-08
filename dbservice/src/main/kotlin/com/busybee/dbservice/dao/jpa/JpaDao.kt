package com.busybee.dbservice.dao.jpa

import com.busybee.dbservice.dao.Dao
import com.busybee.dbservice.dao.OrderBy
import com.busybee.dbservice.model.AppModel
import com.busybee.dbservice.model.DatedModel
import com.busybee.dbservice.model.SystemUser
import jakarta.persistence.EntityManager
import jakarta.persistence.TypedQuery
import org.springframework.stereotype.Repository
import java.util.*
import java.util.function.Predicate


@Repository
class JpaDao(val em: EntityManager) : Dao {
    override fun <E : AppModel> save(model: E): E {
        return saveModel(model)
    }
    override fun <E : DatedModel> save(model: E, user: SystemUser): E {
        return saveDatedModel(model, user)
    }

    override fun <E : DatedModel> save(models: List<E>, user: SystemUser): List<E> {
        return models.map { saveModel(it) }
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

    override fun permanentlyDelete(model: AppModel) {
        em.remove(model)
    }

    override fun <E : AppModel> findById(type: Class<E>, id: Long): E? {
        return em.find(type, id)
    }

    override fun <E : AppModel> findByPropertyKey(type: Class<E>, propertyKeyMap: Map<String, Any>, offset: Int?, maxResults: Int?, orderBy: Map<String, OrderBy>, distinctColumns: List<String>): List<E> {
        val query: TypedQuery<E> = generateQueryForPropertyKeyList(type, propertyKeyMap, offset, maxResults)
        return query.resultList
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

    private fun <E : AppModel> generateQueryForPropertyKeyList(type: Class<E>, propertyKeyMap: Map<String, Any>, offset: Int?, maxResults: Int?): TypedQuery<E> {
        val criteriaBuilder = em.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(type)
        val root = criteriaQuery.from(type)
        val m = em.metamodel
        val entity_ = m.entity(type)
        criteriaQuery.select(root)

        val preds = propertyKeyMap.entries.map { (k, v) ->
            criteriaBuilder.equal(root.get(entity_.getDeclaredSingularAttribute(k)), v)
        }

        criteriaQuery.where(
            *preds.toTypedArray()
        )
        offset?.let {o ->
            maxResults?.let {mr ->
                return em.createQuery(criteriaQuery).setFirstResult(offset).setMaxResults(maxResults)
            }
        }
        return em.createQuery(criteriaQuery)
    }


}
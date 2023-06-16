package com.busybee.dbservice.dao.jpa

import com.busybee.dbservice.dao.Dao
import com.busybee.dbservice.dao.OrderBy
import com.busybee.dbservice.model.AppModel
import com.busybee.dbservice.model.DatedModel
import com.busybee.dbservice.model.SystemUser
import jakarta.persistence.EntityManager
import jakarta.persistence.TypedQuery
import jakarta.persistence.criteria.Join
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.stereotype.Repository
import java.util.*


@Repository
class JpaDao(val em: EntityManager, val dbGraph: DBGraph) : Dao {
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
        val fieldAnnotation = dbGraph.getEntity(type.name)
        val criteriaBuilder = em.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(type)
        val root = criteriaQuery.from(type)
        val metaModel = em.metamodel

        val predicateMap = mapOf<String, Predicate>()
        propertyKeyMap.entries.map { (key, value) ->
            val keys = key.split("\\.")
            var joins = mutableListOf<Any>()
            var types = mutableListOf<Any>()
            keys.forEachIndexed { index, key ->
                if (fieldAnnotation.datedNbcModelCollection.containsKey(key)) {
                     val keyType = fieldAnnotation.datedNbcModelCollection[key]!!.genericType.javaClass
                    val joinFieldAnnotation = dbGraph.getEntity(keyType.name)
                    if (joins.isEmpty()) {
                        joins.add(doJoin(root, type, keyType, key))
                    } else {
                        joins.add(doJoinFromJoin(getJoin(joins.last(), type , keyType), type, keyType, key))
                    }

                    types.add(keyType)
                }
            }
        }

        val entity_ = metaModel.entity(type)
        criteriaQuery.select(root)

        val preds = propertyKeyMap.entries.map { (k, v) ->
            criteriaBuilder.equal(root.get(entity_.getDeclaredSingularAttribute(k)), v)
        }

        criteriaQuery.where(
                *preds.toTypedArray()
        )
        offset?.let { o ->
            maxResults?.let { mr ->
                return em.createQuery(criteriaQuery).setFirstResult(offset).setMaxResults(maxResults)
            }
        }
        return em.createQuery(criteriaQuery)
    }

    fun <E> processKey(root: Root<E>, fieldAnnotation: FieldAnnotation, keys: List<String>, index: Int, from: Class<E>) {
        if (fieldAnnotation.datedNbcModelCollection.containsKey(keys[index])) {
            val keyType = fieldAnnotation.datedNbcModelCollection[keys[index]]!!.javaClass
            val join = doJoin(root, from, keyType, keys[index])

            val increment = index + 1
            val joinFieldAnnotation = dbGraph.getEntity(keyType.name)
            return processKey(join, joinFieldAnnotation, keys, increment, index, from, keyType)
        }
    }

    fun <E, T> processKey(join: Join<E, T>, fieldAnnotation: FieldAnnotation, keys: List<String>, index: Int, from: Class<E>, to: Class<T>): Join<E, T> {
        if (fieldAnnotation.datedNbcModelCollection.containsKey(keys[index])) {
            val keyType = fieldAnnotation.datedNbcModelCollection[keys[index]]!!.genericType.javaClass
            val newJoin = doJoinFromJoin(join, from, keyType, keys[index])

            val increment = index + 1
            val joinFieldAnnotation = dbGraph.getEntity(keyType.name)
            return processKey(newJoin, joinFieldAnnotation, keys, increment, index, keyType)
        }
        return join
    }

    fun <E, T> getJoin(join: Any, from: Class<E>, to: Class<T>): Join<E, T> {
        return join as Join<E,T>
    }

    fun <E, T> doJoin(root: Root<E>, from: Class<E>, to: Class<T>, property: String, joinType: JoinType = JoinType.INNER): Join<E, T> =
        root.join(property, joinType)


    fun <E, T, X, Y> doJoinFromJoin(join: Join<E, T>, from: Class<X>, to: Class<Y>, property: String, joinType: JoinType = JoinType.INNER): Join<E, T> =
        join.join(property, joinType)


}
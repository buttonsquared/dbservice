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
import jakarta.persistence.metamodel.EntityType
import jakarta.persistence.metamodel.SingularAttribute
import org.springframework.stereotype.Repository
import java.lang.reflect.ParameterizedType;
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
        var fieldAnnotation = dbGraph.getEntity(type.name)
        val criteriaBuilder = em.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(type)
        val root = criteriaQuery.from(type)
        val metaModel = em.metamodel
        val predicates = mutableListOf<Predicate>()

        propertyKeyMap.entries.map { (key, value) ->
            val keys = key.split(".")
            val joins = mutableListOf<Join<*, *>>()
            val types = mutableListOf<Class<*>>()
            val entities = mutableMapOf<String, EntityType<*>>()

            types.add(type)
            entities[type.name] = metaModel.entity(type)

            keys.forEachIndexed { index, k ->
                if (fieldAnnotation.datedNbcModelCollection.containsKey(k)) {
                    val keyType = (fieldAnnotation.datedNbcModelCollection[k]!!.genericType as ParameterizedType).actualTypeArguments[0] as Class<*>
                    entities[keyType.name] = metaModel.entity(getTypeX(keyType))
                    fieldAnnotation = dbGraph.getEntity(keyType.name)
                    types.add(keyType)

                    if (joins.isEmpty()) {
                        joins.add(doJoin(root, getTypeE(types[0]), getTypeT(types[1]), k))
                    } else {
                        joins.add(doJoinFromJoin(getJoin(joins.last(), getTypeE(getFromTypeFromList(types)), getTypeT(getToTypeFromList(types))), type, getTypeX(getNewToTypeFromList(types)), k))
                    }
                } else if (joins.isEmpty()) {
                    predicates.add(criteriaBuilder.equal(root.get(getSingularAttribute(entities, keys.last(), getTypeX(getNewToTypeFromList(types)), getTypeY(value::class.java))), value))
                } else {
                    predicates.add(criteriaBuilder.equal(getJoin(joins.last(), getTypeE(getToTypeFromList(types)), getTypeT(getNewToTypeFromList(types))).get(getSingularAttribute(entities, keys.last(), getTypeX(types.last()), getTypeY(value::class.java) )), value))
                }
            }
        }

        criteriaQuery.select(root)

        criteriaQuery.where(
                *predicates.toTypedArray()
        )
        offset?.let { o ->
            maxResults?.let { mr ->
                return em.createQuery(criteriaQuery).setFirstResult(offset).setMaxResults(maxResults)
            }
        }
        return em.createQuery(criteriaQuery)
    }

    fun <X, Y> getSingularAttribute(entities: MutableMap<String, EntityType<*>>, key: String, first: Class<X>, second: Class<Y>): SingularAttribute<X, Y> {
        val entity = getEntityTypeE(entities[first.name]!!, second)
        return entity.getDeclaredSingularAttribute(key) as SingularAttribute<X, Y>
    }

    fun <E: AppModel, T: AppModel> getJoin(join: Any, from: Class<E>, to: Class<T>): Join<E, T> = join as Join<E,T>

    fun getFromTypeFromList(types: MutableList<Class<*>>) = types[types.size - 3]
    fun getToTypeFromList(types: MutableList<Class<*>>) = types[types.size - 2]
    fun getNewToTypeFromList(types: MutableList<Class<*>>) = types.last()
    fun <E> getEntityTypeE(entity: Any, clazz: Class<E>): EntityType<E> = entity as EntityType<E>
    fun <E: AppModel> getTypeE(clazz: Any): Class<E> = clazz as Class<E>
    fun <T: AppModel> getTypeT(clazz: Any): Class<T> = clazz as Class<T>
    fun <X: AppModel> getTypeX(clazz: Any): Class<X> = clazz as Class<X>
    fun <Y: AppModel> getTypeY(clazz: Any): Class<Y> = clazz as Class<Y>

    fun <E, T> doJoin(root: Root<E>, from: Class<E>, to: Class<T>, property: String, joinType: JoinType = JoinType.INNER): Join<E, T> =
        root.join(property, joinType)


    fun <E, T, X, Y> doJoinFromJoin(join: Join<E, T>, from: Class<X>, to: Class<Y>, property: String, joinType: JoinType = JoinType.INNER): Join<X, Y> =
        join.join(property, joinType)


}
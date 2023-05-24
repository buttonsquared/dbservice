package com.busybee.dbservice.dao

import com.busybee.dbservice.model.AppModel
import com.busybee.dbservice.model.DatedModel
import com.busybee.dbservice.model.SystemUser

interface Dao {
    fun <E : DatedModel> save(model: E, user: SystemUser, saveChildren: Boolean = false): E?

    fun <E : DatedModel> save(models: List<E>, user: SystemUser, saveChildren: Boolean = false): List<E>

    fun permanentlyDelete(model: AppModel)

    fun <E : AppModel> findById(type: Class<E>, id: Long): E?

    fun <E : AppModel> findByPropertyKey(type: Class<E>, propertyKeyMap: Map<String, Any>): List<E>

    fun <E : AppModel> findByPropertyKey(type: Class<E>, propertyKeyMap: Map<String, Any>, offset: Int?, maxResults: Int?, orderBy: Map<String, OrderBy> = mapOf(), distinctColumns: List<String> = listOf()): List<E>

    fun <E : AppModel> countByPropertyKey(type: Class<E>, propertyKeyMap: Map<String, Any>, distinct: List<String> = listOf()): Int

    fun <E : AppModel> findByPropertyKeyUnique(type: Class<E>, propertyKeyMap: Map<String, Any>, cacheResult: Boolean = false): E?

    fun <E : AppModel> findEntityBySQLQuery(sql: String, params: Map<String, Any>, type: Class<E>): List<E>

    fun findBySQLQuery(sql: String, params: Map<String, Any>, scalars: List<String>): List<Any>
}
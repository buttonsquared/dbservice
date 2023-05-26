package com.busybee.dbservice.model

import java.io.Serializable

interface AppModel : Serializable {
    fun getModelId(): Long?
}

interface DatedModel : AppModel {
    fun setModelModifiedDate(modifiedDate: Long)
    fun setModelCreatedBy(createdBy: String)
    fun setModelModifiedBy(modifiedBy: String)
    fun uniquePropertyKeyMap(): Map<String, Any>
}

interface SystemUser {

    fun getFullName(): String?

    fun getSystemRoles(): List<SystemRole>

    fun getUsername(): String
}

interface SystemRole {
    fun getModelRoleName(): String

}


interface Deletable {
    fun setModelActive(active: Boolean)
}

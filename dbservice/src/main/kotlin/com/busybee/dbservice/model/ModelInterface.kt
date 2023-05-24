package com.busybee.dbservice.model

import java.io.Serializable

interface AppModel : Serializable {
    fun deepCopy(): AppModel
    fun getId(): Long?
}

interface DatedModel : AppModel {
    fun setCreatedDate(createdDate: Long?)
    fun setModifiedDate(modifiedDate: Long?)
    fun setCreatedBy(createdBy: SystemUser?)
    fun setModifiedBy(modifiedBy: SystemUser?)

}

interface SystemUser {
    fun getId(): Long?

    fun getFullName(): String?

    fun getUserName(): String?

    fun getSystemRoles(): List<SystemRole>?
}

interface SystemRole {
    fun getRoleName(): String?

    fun isActive(): Boolean = true
}
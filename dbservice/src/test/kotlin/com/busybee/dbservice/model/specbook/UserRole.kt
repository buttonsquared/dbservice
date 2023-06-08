package com.busybee.dbservice.model.specbook

import com.busybee.dbservice.model.AbstractDatedModel
import com.busybee.dbservice.model.DatedModel
import com.busybee.dbservice.model.SystemRole
import jakarta.persistence.*

//@Entity
//@Table(name = "SECURITYROLE")
class UserRole(
    @Id
    @SequenceGenerator(name = "userRoleIdGen", sequenceName = "SECURITYROLE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userRoleIdGen")
    @Column(name = "SECURITYROLEID")
    var id: Long? = null,

    @Column(name = "ROLENAME")
    var role: String = "",

    @Column(name = "DESCR")
    val description: String? = null
) : AbstractDatedModel(), DatedModel, SystemRole {
    override fun getModelId(): Long? {
        return id
    }

    override fun getModelRoleName() = role
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserRole

        if (id == null && other.id == null) {
            if (role != other.role) return false
            if (description != other.description) return false
        } else {
            return id == other.id
        }

        return true
    }

    override fun hashCode(): Int {
        if (id == null) {
            var result = role.hashCode() ?: 0
            result = 31 * result + (description?.hashCode() ?: 0)
            return result
        } else {
            return id.hashCode()
        }
    }
}
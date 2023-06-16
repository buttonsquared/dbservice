package com.busybee.dbservice.model.specbook

import com.busybee.dbservice.model.AbstractDatedModel
import com.busybee.dbservice.model.DatedModel
import com.busybee.dbservice.model.SystemRole
import com.busybee.dbservice.model.SystemUser
import com.busybee.dbservice.model.specbook.enums.Status
import jakarta.persistence.*

//@Entity
//@Table(name = "SPECUSER")
class User (
    @Id
    @SequenceGenerator(name = "userIdGen", sequenceName = "SPECUSER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userIdGen")
    @Column(name = "USERID")
    val id: Long? = null,

    @Column(name = "FIRSTNAME")
    val firstname: String? = null,

    @Column(name = "LASTNAME")
    val lastname: String? = null,

    @Column(name = "USERNAME", unique = true)
    val login: String = "",

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    val status: Status? = null,

    @Column(unique = true, name = "EMAIL")
    val emailAddress: String? = null,

    @ManyToMany
    @JoinTable(
        name = "SPECUSER_SECURITYROLE",
        joinColumns = [JoinColumn(name = "USERID", referencedColumnName = "USERID")],
        inverseJoinColumns = [JoinColumn(name = "SECURITYROLEID", referencedColumnName = "SECURITYROLEID")]
    )
    val roles: List<UserRole> = ArrayList()
) : AbstractDatedModel(), DatedModel, SystemUser {
    override fun getModelId(): Long? {
        return id
    }

    override fun getFullName(): String {
        return "$firstname $lastname"
    }

    override fun getSystemRoles(): List<SystemRole> = roles.map { it }

    override fun getUsername(): String {
        return login
    }

}
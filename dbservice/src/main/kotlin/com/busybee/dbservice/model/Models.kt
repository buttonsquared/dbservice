package com.busybee.dbservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


@Entity
@Table(name = "APPLICATION_USER")
data class UserImpl(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeq")
        @SequenceGenerator(name = "userSeq", sequenceName = "APPLICATION_USER_SEQ", allocationSize = 1)
        var id: Long? = null,

        @Column(name = "username", unique = true, nullable = false, length = 45)
        var login: String,

        @Column(name = "firstname")
        val firstName: String? = null,

        @Column(name = "lastname")
        var lastName: String? = null,

        @Column(name = "active", length = 1, nullable = false)
        var active: Boolean = true,

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "USER_ROLE", joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")], inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id", unique = false)])
        @Fetch(FetchMode.SELECT)
        var roles: List<RoleImpl> = ArrayList<RoleImpl>(),


        ) : AbstractDatedModel(), SystemUser, DatedModel, Deletable, UserDetails {


    override fun getFullName(): String? {
        firstName?.let {
            lastName?.let {
                return "$firstName $lastName"
            }
            return firstName
        }
        return null
    }

    override fun getUsername(): String = login

    @JsonIgnore
    override fun uniquePropertyKeyMap(): Map<String, Any> = mapOf("userName" to login)
    override fun getModelId(): Long? = id

    @JsonIgnore
    override fun getAuthorities(): Collection<GrantedAuthority> = roles.map { SimpleGrantedAuthority(it.roleName) }
    override fun getPassword(): String? = null


    override fun isAccountNonExpired() = false

    override fun isAccountNonLocked() = active

    override fun isCredentialsNonExpired() = false

    override fun isEnabled() = active

    @JsonIgnore
    override fun getSystemRoles(): List<SystemRole> = roles.map { it }


    override fun setModelActive(active: Boolean) {
        this.active = active
    }
}

@Entity
@Table(name = "ROLE")
data class RoleImpl(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq.id")
        @SequenceGenerator(name = "seq.id", sequenceName = "ROLE_SEQ", allocationSize = 1)
        var id: Long? = null,

        @Column(name = "ROLE")
        val roleName: String,

        @Column(name = "active", length = 1, nullable = false)
        var active: Boolean = true,
) : SystemRole, AppModel, Deletable {
    override fun getModelId(): Long? = id

    override fun getModelRoleName() = roleName

    override fun setModelActive(active: Boolean) {
        this.active = active
    }
}

package com.busybee.dbservice.model.specbook

import com.busybee.dbservice.model.AbstractDatedModel
import com.busybee.dbservice.model.DatedModel
import com.busybee.dbservice.model.specbook.enums.SpecState
import com.busybee.dbservice.model.specbook.enums.Status
import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode

@Entity
@Table(name = "SPEC")
data class Spec(
    @Id
    @SequenceGenerator(name = "specIdGen", sequenceName = "SPEC_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "specIdGen")
    @Column(name = "SPECID")
    var id: Long? = null,

    @OneToMany(orphanRemoval = true)
    @OrderColumn(name = "ORDERRANK")
    @JoinTable(
        name = "SPEC_SPECSTEP",
        joinColumns = [JoinColumn(name = "SPECID", referencedColumnName = "SPECID")],
        inverseJoinColumns = [JoinColumn(name = "SPECSTEPID", referencedColumnName = "SPECSTEPID")]
    )
    var steps: List<SpecStep> = listOf(),

    @OneToMany(mappedBy = "spec")
    @Fetch(value = FetchMode.SELECT)
    @OrderColumn(name = "ORDERRANK")
    var clients: List<SpecClient> = listOf(),

    @Column(name = "SPECNAME")
    var name: String? = null,

    @Column(name = "DESCRIPTION")
    val description: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    val status: Status? = null,

    @Column(name = "REVISION")
    val revision: Int = 0,

    @Column(name = "SPECCOMMENT")
    val comment: String? = null,

    @Column(name = "PARENTSPECID")
    val parentSpecId: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "SPECSTATE")
    var specState: SpecState? = null,

    @Column(name = "LOCKED")
    val locked: Boolean? = null,

    ) : AbstractDatedModel(), DatedModel {
    override fun getModelId(): Long? {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Spec

        if (id == null && other.id == null) {
            if (steps != other.steps) return false
            if (clients != other.clients) return false
            if (name != other.name) return false
            if (description != other.description) return false
            if (status != other.status) return false
            if (revision != other.revision) return false
            if (comment != other.comment) return false
            if (parentSpecId != other.parentSpecId) return false
            if (specState != other.specState) return false
            if (locked != other.locked) return false
        } else {
            return id == other.id
        }

        return true
    }

    override fun hashCode(): Int {
        return if (id == null) {
            var result = steps.hashCode()
            result = 31 * result + clients.hashCode()
            result = 31 * result + (name?.hashCode() ?: 0)
            result = 31 * result + (description?.hashCode() ?: 0)
            result = 31 * result + (status?.hashCode() ?: 0)
            result = 31 * result + revision
            result = 31 * result + (comment?.hashCode() ?: 0)
            result = 31 * result + (parentSpecId?.hashCode() ?: 0)
            result = 31 * result + (specState?.hashCode() ?: 0)
            result = 31 * result + (locked?.hashCode() ?: 0)
            result
        } else {
            id.hashCode()
        }
    }
}
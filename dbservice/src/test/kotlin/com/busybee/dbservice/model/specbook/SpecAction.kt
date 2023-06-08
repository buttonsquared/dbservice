package com.busybee.dbservice.model.specbook

import com.busybee.dbservice.model.AbstractDatedModel
import com.busybee.dbservice.model.DatedModel
import com.busybee.dbservice.model.specbook.enums.Status
import jakarta.persistence.*

//@Entity
//@Table(name = "ACTIONTABLE")
class SpecAction(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actionIdGen")
    @SequenceGenerator(name = "actionIdGen", sequenceName = "ACTIONTABLE_SEQ", allocationSize = 1)
    @Column(name = "ACTIONTABLEID")
    var id: Long? = null,

    @Column(name = "ACTIONNAME")
    var name: String? = null,

    @Column(name = "DESCR")
    val description: String? = null,

    @OrderColumn(name = "ORDERRANK")
    @ManyToMany
    @JoinTable(
        name = "ACTIONTABLE_CUSTOMFIELD",
        joinColumns = [JoinColumn(name = "ACTIONTABLEID", referencedColumnName = "ACTIONTABLEID")],
        inverseJoinColumns = [JoinColumn(name = "CUSTOMFIELDID", referencedColumnName = "CUSTOMFIELDID")]
    )
    var fields: List<CustomField> = ArrayList(),

    @Enumerated(EnumType.STRING) @Column(name = "ACTIONSTATUS")
    var status: Status? = null

) : AbstractDatedModel(), DatedModel {
    override fun getModelId(): Long? {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpecAction

        if (id == null && other.id == null) {
            if (name != other.name) return false
            if (description != other.description) return false
            if (fields != other.fields) return false
            if (status != other.status) return false
        } else {
            return id == other.id
        }

        return true
    }

    override fun hashCode(): Int {
        if (id == null) {
            var result = name?.hashCode() ?: 0
            result = 31 * result + (description?.hashCode() ?: 0)
            result = 31 * result + fields.hashCode()
            result = 31 * result + (status?.hashCode() ?: 0)
            return result
        } else {
            return id.hashCode()
        }
    }
}
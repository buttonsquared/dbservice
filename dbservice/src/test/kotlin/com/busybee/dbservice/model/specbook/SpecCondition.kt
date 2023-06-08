package com.busybee.dbservice.model.specbook

import com.busybee.dbservice.model.AbstractDatedModel
import com.busybee.dbservice.model.DatedModel
import jakarta.persistence.*

//@Entity
//@Table(name = "CONDITION")
class SpecCondition(
    @Id
    @SequenceGenerator(name = "conditionIdGen", sequenceName = "CONDITION_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conditionIdGen")
    @Column(name = "CONDITIONID")
    var id: Long? = null,

    @Column(name = "CONDITIONNAME")
    var name: String? = null,

    @Column(name = "DESCR")
    val description: String? = null,

    @OrderColumn(name = "ORDERRANK")
    @ManyToMany
    @JoinTable(
        name = "CONDITION_CUSTOMFIELD",
        joinColumns = [JoinColumn(name = "CONDITIONID", referencedColumnName = "CONDITIONID")],
        inverseJoinColumns = [JoinColumn(name = "CUSTOMFIELDID", referencedColumnName = "CUSTOMFIELDID")]
    )
    var fields: List<CustomField> = ArrayList()
) : AbstractDatedModel(), DatedModel {
    override fun getModelId(): Long? {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpecCondition

        if (id == null && other.id == null) {
            if (name != other.name) return false
            if (description != other.description) return false
            if (fields != other.fields) return false
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
            return result
        } else {
            return id.hashCode()
        }
    }
}
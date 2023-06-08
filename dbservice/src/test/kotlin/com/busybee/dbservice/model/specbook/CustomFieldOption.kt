package com.busybee.dbservice.model.specbook

import com.busybee.dbservice.model.AbstractDatedModel
import com.busybee.dbservice.model.DatedModel
import jakarta.persistence.*

//@Entity
//@Table(name = "FIELDOPTION")
class CustomFieldOption (
    @Id
    @SequenceGenerator(name = "optionIdGen", sequenceName = "FIELDOPTION_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "optionIdGen")
    @Column(name = "FIELDOPTIONID")
    val id: Long? = null,

    @Column(name = "FIELDOPTIONNAME")
    val name: String? = null,

    @Column(name = "FIELDOPTIONVALUE")
    val value: String? = null
) : AbstractDatedModel(), DatedModel {
    override fun getModelId(): Long? {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CustomFieldOption

        if (id == null && other.id == null) {
            if (name != other.name) return false
            if (value != other.value) return false
        } else {
            return id == other.id
        }

        return true
    }

    override fun hashCode(): Int {
        if (id == null) {
            var result = name?.hashCode() ?: 0
            result = 31 * result + (value?.hashCode() ?: 0)
            return result
        } else {
            return id.hashCode()
        }
    }

}
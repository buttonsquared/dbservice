package com.busybee.dbservice.model.specbook

import com.busybee.dbservice.model.AbstractDatedModel
import com.busybee.dbservice.model.DatedModel
import jakarta.persistence.*

@Entity
@Table(name = "CUSTOMFIELDANSWER")
data class CustomFieldAnswer(
    @Id
    @SequenceGenerator(name = "answerIdGen", sequenceName = "FIELDANSWER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "answerIdGen") @Column(name = "FIELDANSWERID")
    var id: Long? = null,

    @Column(name = "CUSTOMFIELDID")
    val customFieldID: Long? = null,

    @Column(name = "CUSTOMOPTIONVALUE")
    val optionValue: String? = null,

    @Column(name = "CUSTOMFIELDNAME")
    val fieldName: String? = null,

    @Column(name = "CUSTOMFIELDOPTIONNAME")
    val optionName: String? = null,

    @Transient
    val customField: CustomField? = null
) : AbstractDatedModel(), DatedModel {
    override fun getModelId(): Long? {
        return id;
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CustomFieldAnswer

        if (id == null && other.id == null) {
            if (customFieldID != other.customFieldID) return false
            if (optionValue != other.optionValue) return false
            if (fieldName != other.fieldName) return false
            if (optionName != other.optionName) return false
            if (customField != other.customField) return false
        } else {
            return id == other.id
        }

        return true
    }

    override fun hashCode(): Int {
        if (id == null) {
            var result = customFieldID?.hashCode() ?: 0
            result = 31 * result + (optionValue?.hashCode() ?: 0)
            result = 31 * result + (fieldName?.hashCode() ?: 0)
            result = 31 * result + (optionName?.hashCode() ?: 0)
            result = 31 * result + (customField?.hashCode() ?: 0)
            return result
        } else {
            return id.hashCode()
        }
    }
}
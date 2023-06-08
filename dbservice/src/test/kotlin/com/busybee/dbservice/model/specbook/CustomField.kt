package com.busybee.dbservice.model.specbook

import com.busybee.dbservice.model.AbstractDatedModel
import com.busybee.dbservice.model.DatedModel
import com.busybee.dbservice.model.specbook.enums.CustomFieldType
import com.busybee.dbservice.model.specbook.enums.Status
import jakarta.persistence.*

@Entity
@Table(name = "CUSTOMFIELD")
class CustomField(
    @Id
    @SequenceGenerator(name = "fieldIdGen", sequenceName = "CUSTOMFIELD_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fieldIdGen")
    @Column(name = "CUSTOMFIELDID")
    var id: Long? = null,

    @Column(name = "CUSTOMFIELDNAME")
    val name: String? = null,

    @Column(name = "CUSTOMFIELDCODE")
    val code: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "CUSTOMFIELDTYPE")
    val type: CustomFieldType? = null,

    @Column(name = "DESCR")
    val description: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    val status: Status? = null,

    @Column(name = "DEFAULTVALUE")
    val defaultValue: String? = null,

    @Column(name = "ISREQUIRED")
    val required: Boolean? = null,

    @Column(name = "ISREFERENCE")
    val reference: Boolean? = null,

    @Column(name = "ISPROTECTED")
    val protectedField: Boolean? = null
) : AbstractDatedModel(), DatedModel {
    override fun getModelId(): Long? {
        return id;
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CustomField

        if (id == null && other.id == null) {
            if (name != other.name) return false
            if (code != other.code) return false
            if (type != other.type) return false
            if (description != other.description) return false
            if (status != other.status) return false
            if (defaultValue != other.defaultValue) return false
            if (required != other.required) return false
            if (reference != other.reference) return false
            if (protectedField != other.protectedField) return false
        } else {
            return id == other.id
        }

        return true
    }

    override fun hashCode(): Int {
        if (id == null) {
            var result = name?.hashCode() ?: 0
            result = 31 * result + (code?.hashCode() ?: 0)
            result = 31 * result + (type?.hashCode() ?: 0)
            result = 31 * result + (description?.hashCode() ?: 0)
            result = 31 * result + (status?.hashCode() ?: 0)
            result = 31 * result + (defaultValue?.hashCode() ?: 0)
            result = 31 * result + (required?.hashCode() ?: 0)
            result = 31 * result + (reference?.hashCode() ?: 0)
            result = 31 * result + (protectedField?.hashCode() ?: 0)
            return result
        } else {
            return id.hashCode()
        }
    }
}
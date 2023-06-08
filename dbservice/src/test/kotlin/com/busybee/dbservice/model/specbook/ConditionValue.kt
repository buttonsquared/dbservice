package com.busybee.dbservice.model.specbook

import com.busybee.dbservice.model.AbstractDatedModel
import com.busybee.dbservice.model.DatedModel
import jakarta.persistence.*

@Entity
@Table(name = "CONDITIONVALUE")
class ConditionValue(
    @Id
    @SequenceGenerator(name = "condValueIdGen", sequenceName = "CONDITIONVALUE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "condValueIdGen")
    @Column(name = "CONDITIONVALUEID")
    var id: Long? = null,

    @Column(name = "CONDITIONID")
    val conditionId: Long? = null,

    @Column(name = "CONDITIONNAME")
    val conditionName: String? = null,

    @OneToMany(orphanRemoval = true)
    @OrderColumn(name = "ORDERRANK")
    @JoinTable(
        name = "CONDITIONVALUE_FIELDANSWER",
        joinColumns = [JoinColumn(name = "CONDITIONVALUEID", referencedColumnName = "CONDITIONVALUEID")],
        inverseJoinColumns = [JoinColumn(name = "FIELDANSWERID", referencedColumnName = "FIELDANSWERID")]
    )
    val answers: List<CustomFieldAnswer> = ArrayList()
) : AbstractDatedModel(), DatedModel {
    override fun getModelId(): Long? {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConditionValue

        if (id != null && other.id != null) {
            if (conditionId != other.conditionId) return false
            if (conditionName != other.conditionName) return false
            if (answers != other.answers) return false
        } else {
            return id == id
        }

        return true
    }

    override fun hashCode(): Int {
        if (id == null) {
            var result = conditionId?.hashCode() ?: 0
            result = 31 * result + (conditionName?.hashCode() ?: 0)
            result = 31 * result + answers.hashCode()
            return result
        } else {
            return id.hashCode()
        }

    }

}
package com.busybee.dbservice.model.specbook

import com.busybee.dbservice.model.AbstractDatedModel
import com.busybee.dbservice.model.DatedModel
import jakarta.persistence.*

@Entity
@Table(name = "SPECSTEP")
class SpecStep(
    @Id
    @SequenceGenerator(name = "stepIdGen", sequenceName = "SPECSTEP_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stepIdGen")
    @Column(name = "SPECSTEPID")
    var id: Long? = null,

    @OneToOne(optional = true) @JoinColumn(
        name = "CONDITIONID",
        referencedColumnName = "CONDITIONVALUEID",
        nullable = true
    )
    var condition: ConditionValue? = null,

    @OneToMany(orphanRemoval = true)
    @OrderColumn(name = "ORDERRANK")
    @JoinTable(
        name = "SPECSTEP_ACTIONVALUE",
        joinColumns = [JoinColumn(name = "SPECSTEPID", referencedColumnName = "SPECSTEPID")],
        inverseJoinColumns = [JoinColumn(name = "ACTIONVALUEID", referencedColumnName = "ACTIONVALUEID")]
    )
    val actions: List<ActionValue> = ArrayList(),

    @Column(name = "STEPTYPEID")
    val typeId: Long? = null
) : AbstractDatedModel(), DatedModel {
    override fun getModelId(): Long? {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpecStep

        if (id == null && other.id == null) {
            if (condition != other.condition) return false
            if (actions != other.actions) return false
            if (typeId != other.typeId) return false
        } else {
            return id == other.id
        }

        return true
    }

    override fun hashCode(): Int {
        if (id == null) {
            var result = condition?.hashCode() ?: 0
            result = 31 * result + actions.hashCode()
            result = 31 * result + (typeId?.hashCode() ?: 0)
            return result
        } else {
            return id.hashCode()
        }
    }
}
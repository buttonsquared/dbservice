package com.busybee.dbservice.model.specbook

import com.busybee.dbservice.model.AbstractDatedModel
import com.busybee.dbservice.model.DatedModel
import jakarta.persistence.*

@Entity
@Table(name = "ACTIONVALUE")
data class ActionValue (
    @Id
    @SequenceGenerator(name = "actionValueIdGen", sequenceName = "ACTIONVALUE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actionValueIdGen")
    @Column(name = "ACTIONVALUEID")
    var id: Long? = null,

    @Column(name = "ACTIONNAME")
    val actionName: String? = null,

    @Column(name = "ACTIONID")
    val actionId: Long? = null,

    @OneToMany(orphanRemoval = true)
    @OrderColumn(name = "ORDERRANK")
    @JoinTable(
        name = "ACTIONVALUE_FIELDANSWER",
        joinColumns = [JoinColumn(name = "ACTIONVALUEID", referencedColumnName = "ACTIONVALUEID")],
        inverseJoinColumns = [JoinColumn(name = "FIELDANSWERID", referencedColumnName = "FIELDANSWERID")]
    )
    val answers: List<CustomFieldAnswer> = ArrayList<CustomFieldAnswer>()

) : AbstractDatedModel(), DatedModel {
    override fun getModelId(): Long? {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ActionValue

        if (this.id == null && other.id == null) {
            if (actionName != other.actionName) return false
            if (actionId != other.actionId) return false
            if (answers != other.answers) return false
        } else {
            return this.id == other.id
        }

        return true
    }

    override fun hashCode(): Int {
        return if (id == null) {
            var result = actionName?.hashCode() ?: 0
            result = 31 * result + answers.hashCode()
            result
        } else {
            id.hashCode()
        }
    }

}
package com.busybee.dbservice.model.specbook

import com.busybee.dbservice.model.AbstractDatedModel
import com.busybee.dbservice.model.DatedModel
import com.busybee.dbservice.model.specbook.enums.Status
import jakarta.persistence.*

//@Entity
//@Table(name = "STEPTYPE")
class StepType (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "typeIdGen")
    @SequenceGenerator(name = "typeIdGen", sequenceName = "STEPTYPE_SEQ", allocationSize = 1)
    @Column(name = "STEPTYPEID")
    val id: Long? = null,

    @Column(name = "TYPENAME")
    val name: String? = null,

    @Column(name = "DESCR")
    val description: String? = null,

    @Column(name = "MODEWF")
    val modeWF: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "STEPSTATUS")
    val status: Status? = null,

    @Column(name = "CONFIGURABLEFLAG")
    val configurable: Boolean? = null,

    @OrderColumn(name = "ORDERRANK")
    @ManyToMany
    @JoinTable(
        name = "STEPTYPE_ACTIONTABLE",
        joinColumns = [JoinColumn(name = "STEPTYPEID", referencedColumnName = "STEPTYPEID")],
        inverseJoinColumns = [JoinColumn(name = "ACTIONTABLEID", referencedColumnName = "ACTIONTABLEID")]
    )
    val actions: List<SpecAction> = ArrayList()
) : AbstractDatedModel(), DatedModel {
    override fun getModelId(): Long? {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StepType

        if (id == null && other.id == null) {
            if (name != other.name) return false
            if (description != other.description) return false
            if (modeWF != other.modeWF) return false
            if (status != other.status) return false
            if (configurable != other.configurable) return false
            if (actions != other.actions) return false
        } else {
            return id == other.id
        }

        return true
    }

    override fun hashCode(): Int {
        if (id == null) {
            var result = name?.hashCode() ?: 0
            result = 31 * result + (description?.hashCode() ?: 0)
            result = 31 * result + (modeWF?.hashCode() ?: 0)
            result = 31 * result + (status?.hashCode() ?: 0)
            result = 31 * result + (configurable?.hashCode() ?: 0)
            result = 31 * result + actions.hashCode()
            return result
        } else {
            return id.hashCode()
        }
    }

}
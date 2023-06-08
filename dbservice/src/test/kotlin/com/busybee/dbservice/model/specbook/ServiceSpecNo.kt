package com.busybee.dbservice.model.specbook

import com.busybee.dbservice.model.AbstractDatedModel
import com.busybee.dbservice.model.DatedModel
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

//@Entity
//@Table(name = "SERVICE_SPEC_NO")
class ServiceSpecNo(
    @Id @SequenceGenerator(name = "serviceSpecNoIdGen", sequenceName = "SERVICE_SPEC_NO_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "serviceSpecNoIdGen")
    @Column(name = "SERVICESPECNOID")
    var id: Long? = null,

    @Column(name = "XYTECHSERVICEID")
    var xytechServiceId: Int? = null,

    @Column(name = "XYTECHSERVICESPECNO")
    val xytechServiceSpecNo: Int? = null,

    @Column(name = "ACTIVEYN", length = 1)
    val active: Boolean? = null,

    @ManyToOne
    @JoinColumn(name = "SPECCLIENTID")
    @JsonIgnore
    var specClient: SpecClient? = null
) : AbstractDatedModel(), DatedModel {
    override fun getModelId(): Long? {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ServiceSpecNo

        if (id == null && other.id == null) {
            if (xytechServiceId != other.xytechServiceId) return false
            if (xytechServiceSpecNo != other.xytechServiceSpecNo) return false
            if (active != other.active) return false
            if (specClient != other.specClient) return false
        } else {
            return id == other.id
        }

        return true
    }

    override fun hashCode(): Int {
        if (id == null) {
            var result = xytechServiceId ?: 0
            result = 31 * result + (xytechServiceSpecNo ?: 0)
            result = 31 * result + (active?.hashCode() ?: 0)
            result = 31 * result + (specClient?.hashCode() ?: 0)
            return result
        } else {
            return id.hashCode()
        }
    }
}
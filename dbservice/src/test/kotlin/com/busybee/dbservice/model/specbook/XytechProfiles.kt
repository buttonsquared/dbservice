package com.busybee.dbservice.model.specbook

import com.busybee.dbservice.model.AbstractDatedModel
import com.busybee.dbservice.model.DatedModel
import jakarta.persistence.*

@Entity
@Table(name = "CLIENT_PROFILE")
class XytechProfiles(
    @Id
    @SequenceGenerator(name = "clientIdGen", sequenceName = "CLIENT_SERVICE_PROFILE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clientIdGen")
    @Column(name = "CLIENTPROFILEID")
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "CLIENTID", nullable = false)
    var client: SpecbookClient? = null,

    @Column(name = "SERVICEID")
    val xytechServiceId: Long? = null,

    @Column(name = "PROFILEID")
    val xytechProfileId: Long? = null
) : AbstractDatedModel(), DatedModel {
    override fun getModelId(): Long? {
       return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as XytechProfiles

        if (id == null && other.id == null) {
            if (client != other.client) return false
            if (xytechServiceId != other.xytechServiceId) return false
            if (xytechProfileId != other.xytechProfileId) return false
        } else {
            return id == other.id
        }

        return true
    }

    override fun hashCode(): Int {
        if (id == null) {
            var result = client?.hashCode() ?: 0
            result = 31 * result + (xytechServiceId?.hashCode() ?: 0)
            result = 31 * result + (xytechProfileId?.hashCode() ?: 0)
            return result
        } else {
            return id.hashCode()
        }
    }
}
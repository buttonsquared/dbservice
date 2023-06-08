package com.busybee.dbservice.model.specbook

import com.busybee.dbservice.model.AbstractDatedModel
import com.busybee.dbservice.model.DatedModel
import jakarta.persistence.*

@Entity
@Table(name = "TESTINGSTATUS_LU")
class TestingStatus(
    @Id
    @SequenceGenerator(name = "testingStatusIdGen", sequenceName = "TESTING_STATUS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "testingStatusIdGen")
    @Column(name = "TESTINGSTATUSID")
    var id: Long? = null,

    @Column(name = "STATUS")
    val status: String? = null,

    @Column(name = "GROUPKEY")
    val key: String? = null,

    @Column(name = "XYTECHSERVICEID")
    val xytechServiceId: Int? = null,

    @Column(name = "XYTECHAUTOSERVICEID")
    val xytechAutoServiceId: Int? = null
) : AbstractDatedModel(), DatedModel {
    override fun getModelId(): Long? {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TestingStatus

        if (id == null && other.id == null) {
            if (status != other.status) return false
            if (key != other.key) return false
            if (xytechServiceId != other.xytechServiceId) return false
            if (xytechAutoServiceId != other.xytechAutoServiceId) return false
        } else {
            return id == other.id
        }

        return true
    }

    override fun hashCode(): Int {
        if (id == null) {
            var result = status?.hashCode() ?: 0
            result = 31 * result + (key?.hashCode() ?: 0)
            result = 31 * result + (xytechServiceId ?: 0)
            result = 31 * result + (xytechAutoServiceId ?: 0)
            return result
        } else {
            return id.hashCode()
        }
    }
}
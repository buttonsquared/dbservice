package com.busybee.dbservice.model.specbook

import com.busybee.dbservice.model.AbstractDatedModel
import com.busybee.dbservice.model.DatedModel
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode

@Entity
@Table(name = "TESTINGSTATUSAUDIT")
class TestingStatusAudit(
    @Id
    @SequenceGenerator(name = "testingStatusAuditIdGen", sequenceName = "TESTINGSTATUSAUDIT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "testingStatusAuditIdGen")
    @Column(name = "TESTINGSTATUSAUDITID")
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "SPECCLIENTID", referencedColumnName = "SPECCLIENTID")
    @JsonIgnore
    var specClient: SpecClient? = null,

    @ManyToOne
    @JoinColumn(name = "TESTINGSTATUSID")
    @Fetch(value = FetchMode.JOIN)
    val testingStatus: TestingStatus? = null
) : AbstractDatedModel(), DatedModel {
    override fun getModelId(): Long? {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TestingStatusAudit

        if (id == null && other.id == null) {
            if (specClient != other.specClient) return false
            if (testingStatus != other.testingStatus) return false
        } else {
            return id == other.id
        }

        return true
    }

    override fun hashCode(): Int {
        if (id == null) {
            var result = specClient?.hashCode() ?: 0
            result = 31 * result + (testingStatus?.hashCode() ?: 0)
            return result
        } else {
            return id.hashCode()
        }
    }
}
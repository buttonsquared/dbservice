package com.busybee.dbservice.model.specbook

import com.busybee.dbservice.model.AbstractDatedModel
import com.busybee.dbservice.model.DatedModel
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "SPEC_CLIENT")
class SpecClient(
    @Id
    @SequenceGenerator(name = "specIdGen", sequenceName = "SPECCLIENT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "specIdGen")
    @Column(name = "SPECCLIENTID")
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "SPECID", nullable = false)
    @JsonIgnore
    var spec: Spec? = null,

    @ManyToOne
    @JoinColumn(name = "CLIENTID", nullable = false)
    val specbookClient: SpecbookClient? = null,


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "specClient")
    var testingStatusAuditList: List<TestingStatusAudit> = java.util.ArrayList(),

    @Column(name = "ORDERRANK")
    val orderRank: Int = 0
) : AbstractDatedModel(), DatedModel {
    override fun getModelId(): Long? {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpecClient

        if (id == null && other.id == null) {
            if (spec != other.spec) return false
            if (specbookClient != other.specbookClient) return false
            if (testingStatusAuditList != other.testingStatusAuditList) return false
            if (orderRank != other.orderRank) return false
        } else {
            return id == other.id
        }

        return true
    }

    override fun hashCode(): Int {
        if (id == null) {
            var result = spec?.hashCode() ?: 0
            result = 31 * result + (specbookClient?.hashCode() ?: 0)
            result = 31 * result + testingStatusAuditList.hashCode()
            result = 31 * result + orderRank
            return result
        } else {
            return id.hashCode()
        }
    }
}
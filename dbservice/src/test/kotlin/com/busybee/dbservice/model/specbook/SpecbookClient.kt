package com.busybee.dbservice.model.specbook

import com.busybee.dbservice.model.AbstractDatedModel
import com.busybee.dbservice.model.DatedModel
import com.busybee.dbservice.model.specbook.enums.AddressBookEntityTypes
import com.busybee.dbservice.model.specbook.enums.Status
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode

@Entity
@Table(name = "CLIENT")
class SpecbookClient (
    @Id
    @SequenceGenerator(name = "clientIdGen", sequenceName = "CLIENT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clientIdGen")
    @Column(name = "CLIENTID")
    var id: Long? = null,

    @Column(name = "CLIENTNAME")
    var name: String? = null,

    @Column(name = "ADDRBOOKPARTYID")
    val addressBookPartyId: Long? = null,

    @Column(name = "ADDRBOOKSUBTYPEID")
    val addressBookSubtypeId: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "CLIENTSTATUS")
    val status: Status? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "ADDRBOOKPARTYTYPE")
    val partyType: AddressBookEntityTypes? = null,

    @OneToMany(mappedBy = "specbookClient")
    @Fetch(value = FetchMode.SELECT)
    @JsonIgnore
    val specs: Set<SpecClient> = HashSet(),

    @OneToMany(orphanRemoval = true)
    @OrderColumn(name = "ORDERRANK")
    @JoinTable(
        name = "CLIENT_FIELDANSWER",
        joinColumns = [JoinColumn(name = "CLIENTID", referencedColumnName = "CLIENTID")],
        inverseJoinColumns = [JoinColumn(name = "FIELDANSWERID", referencedColumnName = "FIELDANSWERID")]
    )
    var answers: List<CustomFieldAnswer> = ArrayList(),

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client", orphanRemoval = true)
    //@Fetch(value = FetchMode.SELECT)
    @JsonIgnore
    var profileIds: Set<XytechProfiles> = java.util.HashSet()
) : AbstractDatedModel(), DatedModel {
    override fun getModelId(): Long? {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpecbookClient

        if (id == null && other.id == null) {
            if (name != other.name) return false
            if (addressBookPartyId != other.addressBookPartyId) return false
            if (addressBookSubtypeId != other.addressBookSubtypeId) return false
            if (status != other.status) return false
            if (partyType != other.partyType) return false
            if (specs != other.specs) return false
            if (answers != other.answers) return false
            if (profileIds != other.profileIds) return false
        } else {
            return id == other.id
        }

        return true
    }

    override fun hashCode(): Int {
        if (id == null) {
            var result = name?.hashCode() ?: 0
            result = 31 * result + (addressBookPartyId?.hashCode() ?: 0)
            result = 31 * result + (addressBookSubtypeId?.hashCode() ?: 0)
            result = 31 * result + (status?.hashCode() ?: 0)
            result = 31 * result + (partyType?.hashCode() ?: 0)
            result = 31 * result + specs.hashCode()
            result = 31 * result + answers.hashCode()
            result = 31 * result + profileIds.hashCode()
            return result
        } else {
            return id.hashCode()
        }
    }
}
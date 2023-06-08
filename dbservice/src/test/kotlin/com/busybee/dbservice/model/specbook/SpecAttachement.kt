package com.busybee.dbservice.model.specbook

import com.busybee.dbservice.model.AbstractDatedModel
import com.busybee.dbservice.model.DatedModel
import jakarta.persistence.*

//@Entity
//@Table(name = "SPEC_ATTACHMENT")
class SpecAttachement(
    @Id
    @SequenceGenerator(name = "specAttachmentIdGen", sequenceName = "SPEC_ATTACHMENT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "specAttachmentIdGen")
    @Column(name = "SPECATTACHMENTID")
    val id: Long? = null,

    @Column(name = "ORIGINAL_FILE_NAME")
    val originalFileName: String? = null,

    @Column(name = "ATTACHMENT")
    val attachment: ByteArray? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SPECID", nullable = true)
    var spec: Spec? = null
) : AbstractDatedModel(), DatedModel {
    override fun getModelId(): Long? {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpecAttachement

        if (id == null && other.id == null) {
            if (originalFileName != other.originalFileName) return false
            if (attachment != null) {
                if (other.attachment == null) return false
                if (!attachment.contentEquals(other.attachment)) return false
            } else if (other.attachment != null) return false
            if (spec != other.spec) return false
        } else {
            return id == other.id
        }

        return true
    }

    override fun hashCode(): Int {
        if (id == null) {
            var result = originalFileName?.hashCode() ?: 0
            result = 31 * result + (attachment?.contentHashCode() ?: 0)
            result = 31 * result + (spec?.hashCode() ?: 0)
            return result
        } else {
            return id.hashCode()
        }
    }

}
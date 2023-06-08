package com.busybee.dbservice.model

import com.fasterxml.jackson.annotation.JsonTypeInfo
import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "className")
@MappedSuperclass
abstract class AbstractDatedModel : DatedModel {
    @Column(name = "created_date")
    val createdDate: Long = System.currentTimeMillis()

    @Column(name = "modified_date")
    var modifiedDate: Long? = null

    @Column(name = "created_by")
    var createdBy: String? = null

    @Column(name = "modified_by")
    var modifiedBy: String? = null

    override fun setModelModifiedDate(modifiedDate: Long) {
        this.modifiedDate = modifiedDate
    }

    override fun setModelCreatedBy(createdBy: String) {
        this.createdBy = createdBy
    }

    override fun setModelModifiedBy(modifiedBy: String) {
        this.modifiedBy = modifiedBy
    }

    override fun uniquePropertyKeyMap(): Map<String, Any> {
        return mapOf()
    }
}


package com.busybee.dbservice.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonTypeInfo
import jakarta.persistence.MappedSuperclass
import org.springframework.util.SerializationUtils
import java.io.Serializable

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "className")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class AbstractModel : Serializable {
    fun deepCopy(): AbstractModel {
        return SerializationUtils.clone(this)
    }

    open fun getId(): Long? {
        return null
    }
}

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "className")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@MappedSuperclass
abstract class AbstractDatedModel {
}


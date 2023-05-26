package com.busybee.dbservice.dao.jpa

import com.busybee.dbservice.model.RoleImpl
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JpaDaoTest {

    @Autowired
    lateinit var dao: JpaDao

    @Test
    @Transactional
    fun save() {
        val admin = RoleImpl(roleName = "ADMIN")
        val model = dao.save(admin)
        val savedModel = dao.findById(RoleImpl::class.java, model?.getModelId()!!)
        println(savedModel)
    }
}
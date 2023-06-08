package com.busybee.dbservice.dao.jpa

import com.busybee.dbservice.model.DatedModel
import com.busybee.dbservice.model.RoleImpl
import com.busybee.dbservice.model.UserImpl
import com.busybee.dbservice.model.specbook.Spec
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.functions
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.typeOf

@SpringBootTest
class JpaDaoTest {

    @Autowired
    lateinit var dao: JpaDao

    @Test
    @Transactional
    fun save() {
        val role = RoleImpl(roleName = "USER")
        val userRole = dao.save(role)
        val savedUserModel = dao.findById(RoleImpl::class.java, userRole?.getModelId()!!)
        println(savedUserModel)

        val user = UserImpl(login = "login", firstName = "firstName", lastName = "lastName", roles = listOf(userRole))
        val savedUser = dao.save(user)
        val foundUser = dao.findById(UserImpl::class.java, savedUser?.getModelId()!!)
        assertThat(foundUser?.roles?.size).isEqualTo(1)
        assertThat(foundUser?.roles?.get(0)?.roleName).isEqualTo("USER")
        assertThat(foundUser?.username).isEqualTo("login")
        assertThat(foundUser?.firstName).isEqualTo("firstName")
        assertThat(foundUser?.lastName).isEqualTo("lastName")
    }

    @Test
    @Transactional
    fun `should return not found`() {
        val model = dao.findById(RoleImpl::class.java, 100L)
        assertThat(model).isNull()
    }

    @Test
    @Transactional
    fun `should return model searching by firstname and login`() {
        val role = RoleImpl(roleName = "USER")
        val userRole = dao.save(role)
        val savedUserModel = dao.findById(RoleImpl::class.java, userRole?.getModelId()!!)
        println(savedUserModel)

        val user = UserImpl(login = "login", firstName = "firstName", lastName = "lastName", roles = listOf(userRole))
        val savedUser = dao.save(user)
        val foundUser = dao.findById(UserImpl::class.java, savedUser?.getModelId()!!)

        val model = dao.findByPropertyKey(UserImpl::class.java, mapOf("firstName" to "firstName", "login" to "login"))
        assertThat(model.get(0)).isEqualTo(user)
    }

    @Test
    @Transactional
    fun `should paginate result`() {
        val role = RoleImpl(roleName = "USER")
        val userRole = dao.save(role)
        val savedUserModel = dao.findById(RoleImpl::class.java, userRole?.getModelId()!!)!!


        val user = UserImpl(login = "login", firstName = "firstName", lastName = "lastName", roles = listOf(savedUserModel))
        val savedUser = dao.save(user)
        val user1 = UserImpl(login = "login1", firstName = "firstName1", lastName = "lastName1", roles = listOf(savedUserModel))
        val savedUser1 = dao.save(user1)
        val user2 = UserImpl(login = "login2", firstName = "firstName2", lastName = "lastName2", roles = listOf(savedUserModel))
        val savedUser2 = dao.save(user2)

        val user3 = UserImpl(login = "login3", firstName = "firstName3", lastName = "lastName", roles = listOf(savedUserModel))
        val savedUser3 = dao.save(user3)
        val user4 = UserImpl(login = "login4", firstName = "firstName1", lastName = "lastName1", roles = listOf(savedUserModel))
        val savedUser4 = dao.save(user4)
        val user5 = UserImpl(login = "login5", firstName = "firstName2", lastName = "lastName2", roles = listOf(savedUserModel))
        val savedUser5 = dao.save(user5)

        val model = dao.findByPropertyKey(UserImpl::class.java, mapOf(), 0, 3)
        assertThat(model.size).isEqualTo(3)
        assertThat(model.get(0)).isEqualTo(savedUser)

        val model1 = dao.findByPropertyKey(UserImpl::class.java, mapOf(), 3, 3)
        assertThat(model1.size).isEqualTo(3)
        assertThat(model1.get(0)).isEqualTo(savedUser3)
    }


    @Test
    @Transactional
    fun `reflection test`() {
        val specClass = Spec::class
        specClass.functions.forEach {
            it.returnType.isSubtypeOf(typeOf<DatedModel>())
            println("${it.name} ${it.returnType}")
        }
    }
}
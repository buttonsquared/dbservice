package com.busybee.dbservice.dao.jpa

import com.busybee.dbservice.model.specbook.*
import com.busybee.dbservice.model.specbook.enums.AddressBookEntityTypes
import com.busybee.dbservice.model.specbook.enums.SpecState
import com.busybee.dbservice.model.specbook.enums.Status
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.Join
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Root
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SpecJpaDaoTest {

    @Autowired
    lateinit var em: EntityManager

    @Autowired
    lateinit var dao: JpaDao

    @Autowired
    lateinit var graph: DBGraph

    @Test
    @Transactional
    fun save() {
        val answer1 = CustomFieldAnswer(optionValue = "optionValue1", fieldName = "fieldName1", optionName = "optionName1")
        val savedAnswer1 = dao.save(answer1)
        val answer2 = CustomFieldAnswer(optionValue = "optionValue2", fieldName = "fieldName2", optionName = "optionName2")
        val savedAnswer2 = dao.save(answer2)
        val answer3 = CustomFieldAnswer(optionValue = "optionValue3", fieldName = "fieldName3", optionName = "optionName3")
        val savedAnswer3 = dao.save(answer3)
        val specClientAnswer = CustomFieldAnswer(optionValue = "optionValue4", fieldName = "fieldName4", optionName = "optionName4")
        val savedSpecClientAnswer = dao.save(specClientAnswer)

        val actionValue = ActionValue(actionName = "actionName1", actionId = 1L, answers = listOf(savedAnswer1, savedAnswer2))
        val savedActionValue = dao.save(actionValue)

        val conditionValue = ConditionValue(conditionName = "conditionName1", conditionId = 1L, answers = listOf(savedAnswer3))
        val savedConditionValue = dao.save(conditionValue)

        val specStep = SpecStep(typeId = 1L, actions = listOf(savedActionValue), condition = savedConditionValue)
        val savedSpecStep = dao.save(specStep)

        val testStatus = TestingStatus(key = "key", xytechServiceId = 1, xytechAutoServiceId = 1, status = "status")
        val savedTestStatus = dao.save(testStatus)

        val testingStatusAudit = TestingStatusAudit(testingStatus = savedTestStatus)
        val savedTestingStatusAudit = dao.save(testingStatusAudit)

        val specbookClient = SpecbookClient(name = "name", addressBookPartyId = 1L, addressBookSubtypeId = 1L, status = Status.ACTIVE, partyType = AddressBookEntityTypes.CLIENT, answers = listOf(savedSpecClientAnswer))
        val savedSpecbookClient = dao.save(specbookClient)

        val specClient = SpecClient(specbookClient = savedSpecbookClient, testingStatusAuditList = listOf(savedTestingStatusAudit))
        val savedSpecClient = dao.save(specClient)

        val spec = Spec(steps = listOf(savedSpecStep), clients = listOf(savedSpecClient), name = "name", description = "descrption", status = Status.ACTIVE, specState = SpecState.DRAFT, locked = false)
        val savedSpec = dao.save(spec)

        em.flush()

        val saved = dao.findById(Spec::class.java, savedSpec.id!!)


        val results = dao.findByPropertyKey(ActionValue::class.java, mapOf("actionName" to "actionName1"))
        assertThat(results.size).isEqualTo(1)




        val criteriaBuilder = em.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(SpecStep::class.java)
        val root = criteriaQuery.from(SpecStep::class.java)
        val actionValueJoin = root.join<SpecStep, ActionValue>("actions", JoinType.INNER)
        val answerJoin = actionValueJoin.join<ActionValue, CustomFieldAnswer>("answers", JoinType.INNER)
        val m = em.metamodel
        val specStepEntity = m.entity(SpecStep::class.java)
        val actionValueEntity = m.entity(ActionValue::class.java)
        val entity_ = m.entity(CustomFieldAnswer::class.java)
        criteriaQuery.select(root)

        val preds = criteriaBuilder.equal(answerJoin.get(entity_.getDeclaredSingularAttribute("optionValue")), "optionValue1")
        val preds1 = criteriaBuilder.equal(actionValueJoin.get(actionValueEntity.getDeclaredSingularAttribute("actionName")), "actionName1")

        criteriaQuery.where(preds, preds1)
        val result = em.createQuery(criteriaQuery).singleResult
        assertThat(result.id).isEqualTo(savedActionValue.id)
    }

    @Test
    @Transactional
    fun save1() {
        val answer1 = CustomFieldAnswer(optionValue = "optionValue1", fieldName = "fieldName1", optionName = "optionName1")
        val savedAnswer1 = dao.save(answer1)
        val answer2 = CustomFieldAnswer(optionValue = "optionValue2", fieldName = "fieldName2", optionName = "optionName2")
        val savedAnswer2 = dao.save(answer2)
        val answer3 = CustomFieldAnswer(optionValue = "optionValue3", fieldName = "fieldName3", optionName = "optionName3")
        val savedAnswer3 = dao.save(answer3)
        val specClientAnswer = CustomFieldAnswer(optionValue = "optionValue4", fieldName = "fieldName4", optionName = "optionName4")
        val savedSpecClientAnswer = dao.save(specClientAnswer)

        val actionValue = ActionValue(actionName = "actionName1", actionId = 1L, answers = listOf(savedAnswer1, savedAnswer2))
        val savedActionValue = dao.save(actionValue)

        val conditionValue = ConditionValue(conditionName = "conditionName1", conditionId = 1L, answers = listOf(savedAnswer3))
        val savedConditionValue = dao.save(conditionValue)

        val specStep = SpecStep(typeId = 1L, actions = listOf(savedActionValue), condition = savedConditionValue)
        val savedSpecStep = dao.save(specStep)

        val testStatus = TestingStatus(key = "key", xytechServiceId = 1, xytechAutoServiceId = 1, status = "status")
        val savedTestStatus = dao.save(testStatus)

        val testingStatusAudit = TestingStatusAudit(testingStatus = savedTestStatus)
        val savedTestingStatusAudit = dao.save(testingStatusAudit)

        val specbookClient = SpecbookClient(name = "name", addressBookPartyId = 1L, addressBookSubtypeId = 1L, status = Status.ACTIVE, partyType = AddressBookEntityTypes.CLIENT, answers = listOf(savedSpecClientAnswer))
        val savedSpecbookClient = dao.save(specbookClient)

        val specClient = SpecClient(specbookClient = savedSpecbookClient, testingStatusAuditList = listOf(savedTestingStatusAudit))
        val savedSpecClient = dao.save(specClient)

        val spec = Spec(steps = listOf(savedSpecStep), clients = listOf(savedSpecClient), name = "name", description = "descrption", status = Status.ACTIVE, specState = SpecState.DRAFT, locked = false)
        val savedSpec = dao.save(spec)

        em.flush()

        val saved = dao.findById(Spec::class.java, savedSpec.id!!)


        val results = dao.findByPropertyKey(ActionValue::class.java, mapOf("actionName" to "actionName1"))
        assertThat(results.size).isEqualTo(1)




        val criteriaBuilder = em.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(SpecClient::class.java)
        val root = criteriaQuery.from(SpecClient::class.java)
        val m = em.metamodel
        val specClientEntity = m.entity(SpecClient::class.java)
        val test = specClientEntity.bindableJavaType
        val specbookClientEntity = m.entity(SpecbookClient::class.java)
        val alias = root.alias("specClient")
        val join2 = root.join<SpecClient, SpecbookClient>("specbookClient", JoinType.INNER)


        val specStepEntity = m.entity(SpecStep::class.java)
        val actionValueEntity = m.entity(ActionValue::class.java)
        val entity_ = m.entity(SpecbookClient::class.java)
        criteriaQuery.select(root)

        val preds = criteriaBuilder.equal(join2.get(entity_.getDeclaredSingularAttribute("name")), "name")
        val preds1 = criteriaBuilder.equal(root.get(specClientEntity.getDeclaredSingularAttribute("name")), "name")

        criteriaQuery.where(preds)
        val result = em.createQuery(criteriaQuery).singleResult
        assertThat(result.id).isEqualTo(savedActionValue.id)
    }

    @Test
    @Transactional
    fun save2() {
        val answer1 = CustomFieldAnswer(optionValue = "optionValue1", fieldName = "fieldName1", optionName = "optionName1")
        val savedAnswer1 = dao.save(answer1)
        val answer2 = CustomFieldAnswer(optionValue = "optionValue2", fieldName = "fieldName2", optionName = "optionName2")
        val savedAnswer2 = dao.save(answer2)
        val answer3 = CustomFieldAnswer(optionValue = "optionValue3", fieldName = "fieldName3", optionName = "optionName3")
        val savedAnswer3 = dao.save(answer3)
        val specClientAnswer = CustomFieldAnswer(optionValue = "optionValue4", fieldName = "fieldName4", optionName = "optionName4")
        val savedSpecClientAnswer = dao.save(specClientAnswer)

        val actionValue = ActionValue(actionName = "actionName1", actionId = 1L, answers = listOf(savedAnswer1, savedAnswer2))
        val savedActionValue = dao.save(actionValue)

        val conditionValue = ConditionValue(conditionName = "conditionName1", conditionId = 1L, answers = listOf(savedAnswer3))
        val savedConditionValue = dao.save(conditionValue)

        val specStep = SpecStep(typeId = 1L, actions = listOf(savedActionValue), condition = savedConditionValue)
        val savedSpecStep = dao.save(specStep)

        val testStatus = TestingStatus(key = "key", xytechServiceId = 1, xytechAutoServiceId = 1, status = "status")
        val savedTestStatus = dao.save(testStatus)

        val testingStatusAudit = TestingStatusAudit(testingStatus = savedTestStatus)
        val savedTestingStatusAudit = dao.save(testingStatusAudit)

        val specbookClient = SpecbookClient(name = "name", addressBookPartyId = 1L, addressBookSubtypeId = 1L, status = Status.ACTIVE, partyType = AddressBookEntityTypes.CLIENT, answers = listOf(savedSpecClientAnswer))
        val savedSpecbookClient = dao.save(specbookClient)

        val specClient = SpecClient(specbookClient = savedSpecbookClient, testingStatusAuditList = listOf(savedTestingStatusAudit))
        val savedSpecClient = dao.save(specClient)

        val spec = Spec(steps = listOf(savedSpecStep), clients = listOf(savedSpecClient), name = "name", description = "descrption", status = Status.ACTIVE, specState = SpecState.DRAFT, locked = false)
        val savedSpec = dao.save(spec)

        em.flush()

        val saved = dao.findById(Spec::class.java, savedSpec.id!!)


        val results = dao.findByPropertyKey(ActionValue::class.java, mapOf("actionName" to "actionName1"))
        assertThat(results.size).isEqualTo(1)




        val criteriaBuilder = em.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(SpecClient::class.java)
        val root = criteriaQuery.from(SpecClient::class.java)
        val m = em.metamodel
        val specClientEntity = m.entity(SpecClient::class.java)
        val test = specClientEntity.bindableJavaType
        val specbookClientEntity = m.entity(SpecbookClient::class.java)
        val alias = root.alias("specClient")
        val join2 = test(root, SpecClient::class.java, SpecbookClient::class.java)//root.join<SpecClient, SpecbookClient>("specbookClient", JoinType.INNER)


        val specStepEntity = m.entity(SpecStep::class.java)
        val actionValueEntity = m.entity(ActionValue::class.java)
        val entity_ = m.entity(SpecbookClient::class.java)
        criteriaQuery.select(root)

        val preds = criteriaBuilder.equal(join2.get(entity_.getDeclaredSingularAttribute("name")), "name")
        val preds1 = criteriaBuilder.equal(root.get(specClientEntity.getDeclaredSingularAttribute("name")), "name")

        criteriaQuery.where(preds)
        val result = em.createQuery(criteriaQuery).singleResult
        assertThat(result.id).isEqualTo(savedActionValue.id)



    }

    fun <E, T> test(root: Root<E>, from: Class<E>, to: Class<T>): Join<E, T> {
        return root.join("specbookClient", JoinType.INNER)
    }

//    @Test
//    @Transactional
//    fun test() {
//        val propertyKeyMap = mutableMapOf("clients.specbookClient.CLIENTNAME" to "name")
//        val fieldAnnotation = graph.getEntity(Spec::class.java.name)
//        propertyKeyMap.entries.map { (key, value) ->
//
//            val keys = key.split(".")
//            keys.forEachIndexed { index, key ->
//                if (fieldAnnotation.datedNbcModelCollection.containsKey(key)) {
//                    val keyType = fieldAnnotation.datedNbcModelCollection[key]!!.genericType.javaClass
////                    root.join(key, JoinType.INNER)
//                }
//            }
//        }
//    }
}
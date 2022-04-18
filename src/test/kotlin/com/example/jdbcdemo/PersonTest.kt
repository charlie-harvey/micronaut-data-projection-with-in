package com.example.jdbcdemo

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking

@MicronautTest(environments = ["test"], transactional = false, startApplication = false)
class PersonTest(
    private val personRepository: PersonRepository
) : AnnotationSpec() {

    private lateinit var person: Person

    @BeforeAll
    fun beforeAll() = runBlocking {
        person = personRepository.save(
            Person(
                firstName = "p1FName",
                lastName = "p1LName",
                password = "abcd1234",
                email = "p1Email@example.com"
            )
        )
        personRepository.save(
            Person(
                firstName = "p2FName",
                lastName = "p2LName",
                password = "abcd1234",
                email = "p2Email@example.com"
            )
        )
    }

    @Test
    suspend fun findFirstNameById() {
        personRepository.findFirstNameById(person.id!!).shouldBe("p1FName")
    }

    @Test
    suspend fun findLastNameById() {
        personRepository.findLastNameById(person.id!!).shouldBe("p1LName")
    }

    @Test
    suspend fun findFirstNameAndLastNameById() {
        personRepository.findFirstNameAndLastNameById(person.id!!).shouldBe(
            FirstNameAndLastName(
                firstName = "p1FName",
                lastName = "p1LName"
            )
        )
    }

    @Test
    suspend fun findFirstNameAndLastNameByIdAndEmail() {
        personRepository.findFirstNameAndLastNameByIdAndEmail(person.id!!, "p1Email@example.com").shouldBe(
            FirstNameAndLastName(
                firstName = "p1FName",
                lastName = "p1LName"
            )
        )
    }

    @Test
    suspend fun findIdAndFirstNameByLastName() {
        personRepository.findIdAndFirstNameByLastName("p1LName").shouldBe(
            IdAndFirstName(
                id = person.id!!,
                firstName = "p1FName"
            )
        )
    }

    @Test
    suspend fun findFirstNameAndLastNameByIdAndEmailIn() {
        val people =
            personRepository.findFirstNameAndLastNameByIdAndEmailIn(person.id!!, listOf("p1Email@example.com")).toList()
        people.size.shouldBe(1)
        people[0].shouldBe(
            FirstNameAndLastName(
                firstName = "p1FName",
                lastName = "p1LName"
            )
        )
    }
}

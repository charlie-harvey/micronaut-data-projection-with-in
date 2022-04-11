package com.example.jdbcdemo

import io.micronaut.core.annotation.Introspected
import io.micronaut.data.annotation.*
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.naming.NamingStrategies
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.jpa.kotlin.CoroutineJpaSpecificationExecutor
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository
import kotlinx.coroutines.flow.Flow

@MappedEntity(value = "person", namingStrategy = NamingStrategies.Raw::class)
data class Person(
    @field:Id @GeneratedValue
    val id: Int?,
    val firstName: String,
    val lastName: String,
    val password: String,
    val email: String
) {
    constructor(
        firstName: String,
        lastName: String,
        password: String,
        email: String
    ) : this(null, firstName, lastName, password, email)
}

@Introspected
@NamingStrategy(NamingStrategies.Raw::class)
data class FirstNameAndLastName(
    val firstName: String,
    val lastName: String
)

@Introspected
@NamingStrategy(NamingStrategies.Raw::class)
data class IdAndFirstName(
    val id: Int,
    val firstName: String
)

@Repository
@JdbcRepository(dialect = Dialect.H2)
interface PersonRepository : CoroutineCrudRepository<Person, Int>, CoroutineJpaSpecificationExecutor<Person> {

    suspend fun findFirstNameById(id: Int): String?

    suspend fun findLastNameById(id: Int): String?

    suspend fun findFirstNameAndLastNameById(id: Int): FirstNameAndLastName?

    suspend fun findFirstNameAndLastNameByIdAndEmail(id: Int, email: String): FirstNameAndLastName?

    suspend fun findFirstNameAndLastNameByIdAndEmailIn(id: Int, email: Collection<String>): Flow<FirstNameAndLastName>

    suspend fun findIdAndFirstNameByLastName(lastName: String): IdAndFirstName?
}

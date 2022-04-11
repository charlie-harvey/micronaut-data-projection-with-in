# Two projections with an IN query fails to compile

```Kotlin
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

@Repository
@JdbcRepository(dialect = Dialect.H2)
interface PersonRepository : CoroutineCrudRepository<Person, Int>, CoroutineJpaSpecificationExecutor<Person> {

    // All good
    suspend fun findFirstNameById(id: Int): String?

    // All good
    suspend fun findLastNameById(id: Int): String?

    // All good
    suspend fun findFirstNameAndLastNameById(id: Int): FirstNameAndLastName?

    // All good
    suspend fun findFirstNameAndLastNameByIdAndEmail(id: Int, email: String): FirstNameAndLastName?

    // This one is sad
    suspend fun findFirstNameAndLastNameByIdAndEmailIn(id: Int, email: Collection<String>): Flow<FirstNameAndLastName>
}
```

## Run It
```bash
./gradlew build
```

## Log
```
> Task :kaptKotlin
/Users/charlieharvey/workspace/samples/jdbcdemo/build/tmp/kapt3/stubs/main/com/example/jdbcdemo/PersonRepository.java:28: error: Unable to implement Repository method: PersonRepository.findFirstNameAndLastNameByIdAndEmailIn(int id,Collection email). Cannot project on non-existent property: firstNameAndLastName
    public abstract java.lang.Object findFirstNameAndLastNameByIdAndEmailIn(int id, @org.jetbrains.annotations.NotNull()
                                     ^
Note: Creating bean classes for 4 type elements

> Task :kaptKotlin FAILED

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':kaptKotlin'.
> A failure occurred while executing org.jetbrains.kotlin.gradle.internal.KaptWithoutKotlincTask$KaptExecutionWorkAction
   > java.lang.reflect.InvocationTargetException (no error message)

* Try:
> Run with --stacktrace option to get the stack trace.
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights.

* Get more help at https://help.gradle.org

BUILD FAILED in 6s
4 actionable tasks: 4 executed
```
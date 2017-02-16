package com.example

import au.com.console.jpaspecificationdsl.equal
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.*
import java.io.FileInputStream
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@SpringBootApplication
class DemoApplication {
    val firebaseConfigPath = "path/to/firebase/config.json"
    val firebaseDatabseUrl = "UrlOfFirebase.com"


    //Spring will automatically call this function and inject the return value into other places where it's needed
    //We don't need the return value, so we are just using the @Bean annotation to automatically populate the database
    //Spring will determine where it needs to find the person repository and the databaseReference
    @Bean
    fun init(repository: PersonRepository, fb: DatabaseReference) = CommandLineRunner {
        repository.save(Person("Jon", "Snow"))
        repository.save(Person("Daenerys", "Targaryen"))
        repository.save(Person("Tyrion", "Lannister"))
        repository.save(Person("Cersei", "Lannister", "javascript"))
        repository.save(Person("Sansa", "Stark", "java"))

        repository.findAll().forEach { fb.child(it.id.toString()).setValue(it) }
    }

    //Spring will automatically call this function and inject the return value into other places where it's needed
    @Bean
    fun initFirebase(): DatabaseReference {
        val options = FirebaseOptions.Builder()
                .setServiceAccount(
                        FileInputStream(firebaseConfigPath))
                .setDatabaseUrl(firebaseDatabseUrl)
                .build()

        FirebaseApp.initializeApp(options)
        return FirebaseDatabase.getInstance().getReferenceFromUrl(firebaseDatabseUrl)
    }
}


//Top level functions allow one to define a spring application without wrapping it in a class.
fun main(args: Array<String>) {
    SpringApplication.run(DemoApplication::class.java, *args)
}

@Entity
data class Person(
        val firstName: String,
        val lastName: String?, //Spring will use the nullable information to infer that this field is not required when making a POST request
        val favoriteLanguage: String = "kotlin", //Default values will also be used
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0 //A default value for this is set so we don't have to supply an id when initializing the repository in the init function
)

//Spring will generate an implementation for this interface and make it available for injection at other locations.
//The JpaSpecificationExecutor interface is needed so we can use the jpa specification dsl to define custom methods.
interface PersonRepository : JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

    //Spring has a set of key words that can be used to write queries in plain english that translate to sql queries.
    //The @Param annotation will be exposed as a query parameter using Spring Data Rest
    fun findByLastNameIgnoreCase(@Param("lastName") name: String): List<Person>
}


//If one does not want the 'automatic magical rest api' then one can define a controller the traditional way. This class defines a base path of /persons with sub paths of /persons/{id} and /persons/kotlin/
//The personRepository and databse reference are automatically injected from springs object graph. No need to annotate with @autowire.
@RestController
@RequestMapping("persons")
class PersonsController(val personRepository: PersonRepository, val databaseReference: DatabaseReference) {
    @GetMapping
    fun getAllPeople() = personRepository.findAll()

    @GetMapping("{id}")
    fun getAllPeople(@PathVariable id: Long) = personRepository.findOne(id)


    @PostMapping
    fun createPerson(@RequestBody person: Person) = databaseReference.child(UUID.randomUUID().toString()).setValue(person)

    @GetMapping("kotlin")
    fun GetAllKotlinLovers() = personRepository.findAllKotlinLovers()
}

//Kotlin extension functions provide an easy way to make custom query methods
private fun PersonRepository.findAllKotlinLovers(): List<Person> =
        //The kotlin jpa specification dsl provices an easy idiomatic way to define complex queries
        findAll(Person::favoriteLanguage.equal("kotlin"))


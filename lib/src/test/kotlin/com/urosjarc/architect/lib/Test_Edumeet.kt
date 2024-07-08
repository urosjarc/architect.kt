package com.urosjarc.architect.lib

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.annotations.Identifier
import kotlin.reflect.*
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.test.Test
import kotlin.test.assertEquals

@JvmInline
@Identifier
value class Id<T>(val value: Int = 1) {
    override fun toString(): String {
        return this.value.toString()
    }
}


@DomainEntity
@Identifier
data class Person(
    val name: String,
    val surname: String,
    val age: Int,
    val owns: Id<Product>,
    val id: Id<Person> = Id()
)

@DomainEntity
@Identifier
data class Product(
    val name: String,
    val id: Id<Product> = Id()
)

class Test_Edumeet {
    @Test
    fun `test signature`() {
        val kclass: KClass<Person> = Person::class
        val kclass2: KClass<*> = Person::class
        assertEquals(actual = kclass.toString(), expected = "class com.urosjarc.architect.lib.Person")
        assertEquals(actual = kclass2.toString(), expected = "class com.urosjarc.architect.lib.Person")
    }

    @Test
    fun `test class import`() {
        assertEquals(actual = Person::class.qualifiedName, expected = "com.urosjarc.architect.lib.Person")
    }

    @Test
    fun `test class name`() {
        assertEquals(actual = Person::class.simpleName, expected = "Person")
    }

    @Test
    fun `test class `() {
        assertEquals(
            actual = Person::class.annotations.map { it.annotationClass.simpleName },
            expected = listOf("DomainEntity", "Identifier")
        )
    }

    @Test
    fun `test class state`() {
        assertEquals(actual = Person::class.isData, expected = true)
        assertEquals(actual = Person::class.isOpen, expected = false)
        assertEquals(actual = Person::class.isAbstract, expected = false)
        assertEquals(actual = Person::class.isCompanion, expected = false)
        assertEquals(actual = Person::class.isFinal, expected = false)
        assertEquals(actual = Person::class.isSealed, expected = false)
        assertEquals(actual = Person::class.isValue, expected = false)
    }


    @Test
    fun `test class visibility`() {
        val kvisibility: KVisibility = Person::class.visibility!!
        assertEquals(actual = kvisibility, expected = KVisibility.PUBLIC)
    }

    @Test
    fun `test class field`() {
        val kprop: KProperty1<Person, String> = Person::name
        val kprop2: KProperty1<*, *> = Person::name
        assertEquals(actual = Person::name.name, expected = "name")
        assertEquals(actual = Person::surname.name, expected = "surname")
        assertEquals(actual = Person::age.name, expected = "age")
    }

    @Test
    fun `test class field types`() {
        assertEquals(actual = Person::owns.returnType.toString(), expected = "com.urosjarc.architect.lib.Id<com.urosjarc.architect.lib.Product>")
        assertEquals(actual = Person::owns.returnType.arguments.map { it.type.toString() }, expected = listOf("com.urosjarc.architect.lib.Product"))
    }

    @Test
    fun `test class fields`() {
        val props = Person::class.memberProperties.map { it.name }.sorted()
        assertEquals(actual = props, expected = listOf("age", "name", "surname"))
    }

    @Test
    fun `test construct properties`() {
        val kparams: List<KParameter> = Person::class.primaryConstructor!!.parameters
        val params = kparams.map { "${it.name}: ${it.type.toString().split("<").first().split(".").last()}" }
        assertEquals(
            actual = params, expected = listOf(
                "name: String",
                "surname: String",
                "age: Int",
                "owns: Id",
                "id: Id",
            )
        )
    }

    @Test
    fun `test class methods`() {
        val funNames = Person::class.memberFunctions.map { it.name }
        assertEquals(
            actual = funNames,
            expected = listOf("component1", "component2", "component3", "component4", "component5", "copy", "equals", "hashCode", "toString")
        )
    }

    @Test
    fun `test deserialization`() {
        val const: KFunction<*> = Person::class.primaryConstructor!!
        val person = const.call(*arrayOf("Uros", "Jarc", 12, Id<Product>(value = 1), Id<Person>(value = 1))) as Person
        assertEquals(actual = person, expected = Person(name = "Uros", surname = "Jarc", age = 12, owns = Id()))
    }

    @Test
    fun `test serialization`() {
        val person = Person(name = "Uros", surname = "Jarc", age = 12, owns = Id())
        val obj = Person::class.memberProperties.associate { it.name to it.get(person) }
        assertEquals(
            actual = obj, expected = mapOf(
                "name" to "Uros",
                "surname" to "Jarc",
                "age" to 12,
                "owns" to Id<Product>(value = 1),
                "id" to Id<Person>(value = 1)
            )
        )
    }

    @Test
    fun `test SQL generation`() {
        val mapping = mapOf(
            "kotlin.String" to "varchar(255)",
            "kotlin.Int" to "int",
            "com.urosjarc.architect.lib.Id" to "uuid"
        )
        val person = Person(name = "Uros", surname = "Jarc", age = 12, owns = Id())
        val fields = Person::class.memberProperties.map { "\t\t\t${it.name} ${mapping[it.returnType.toString().split("<").first()]}" }

        println("""
        CREATE TABLE ${Person::class.simpleName} (
${fields.joinToString(",\n")} 
        )
        """)
    }
}

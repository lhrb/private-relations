package com.github.lhrb.prvrel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

interface PersonRepository {
    fun insertPerson(person : Person)
    fun getPersons() : Flow<List<Person>>
    fun deletePerson(person: Person)
}

class PersonRepositoryImpl(val dao: Dao) : PersonRepository {

    override fun insertPerson(person : Person) {
        CoroutineScope(Dispatchers.IO).launch { dao.insertPerson(person = person) }
    }

    override fun getPersons() : Flow<List<Person>> {
        return dao.getPersons()
    }

    override fun deletePerson(person: Person) {
        CoroutineScope(Dispatchers.IO).launch { dao.deletePerson(person = person) }
    }
}
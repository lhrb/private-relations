package com.github.lhrb.prvrel

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    suspend fun insertPerson(person : Person)

    @Query("Select * from Person")
    fun getPersons(): Flow<List<Person>>

    @Delete
    suspend fun deletePerson(person: Person)

    @Insert
    suspend fun insertItem(item : Item)

    @Query("Select * from Item where person_id = :personId")
    fun getItems(personId: Int): Flow<List<Item>>
}
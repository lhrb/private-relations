package com.github.lhrb.prvrel

import androidx.room.*
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

@Entity(tableName = "Person")
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "birthday") val birthday: String?
) {
    // needed for room
    constructor() : this(0, "", null, null)
    constructor(firstName: String, lastName: String?, birthday: String?) : this(
        0,
        firstName,
        lastName,
        birthday
    )
}

@Entity(tableName = "Item")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "person_id") val personId: Int,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "created_at") val createdAt: String
) {
    // needed for room
    constructor() : this(0, 0, "", "")
    constructor(personId: Int, content: String) : this(
        0, personId, content, now()
    )
}

fun now(): String {
    val date = Date()
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    return format.format(date)
}

@Database(entities = [Person::class, Item::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): Dao
}
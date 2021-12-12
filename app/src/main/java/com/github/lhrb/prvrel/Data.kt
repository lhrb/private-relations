package com.github.lhrb.prvrel

import androidx.room.*
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
val dateFormat = SimpleDateFormat("yyyy-MM-dd")

@Entity(tableName = "Person")
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "birthday") val birthday: String?
) {
    constructor(firstName: String, lastName: String?, birthday: String?) : this(
        0,
        firstName,
        lastName,
        birthday
    )
}

@Entity(
    tableName = "Item",
    foreignKeys = [ForeignKey(
        entity = Person::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("person_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "person_id", index = true) val personId: Int,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "created_at") val createdAt: String
) {
    constructor(personId: Int, content: String) : this(
        0, personId, content, now()
    )
}

fun now(): String {
    val date = Date()
    return dateTimeFormat.format(date)
}

@Database(entities = [Person::class, Item::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): Dao
}
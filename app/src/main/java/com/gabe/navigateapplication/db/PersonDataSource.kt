package com.gabe.navigateapplication.db

import com.gabe.navigateapplication.PersonEntity
import kotlinx.coroutines.flow.Flow

interface PersonDataSource {
    suspend fun getPersonById(id:Long):PersonEntity

    fun getAllPersons(): Flow<List<PersonEntity>>

    suspend  fun deletePersonById(id:Long)

    suspend fun insertPerson(firstName:String,lastName:String,id:Long?=null)
}
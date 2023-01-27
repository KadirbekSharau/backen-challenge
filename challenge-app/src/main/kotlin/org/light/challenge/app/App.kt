package org.light.challenge.app

import org.jetbrains.exposed.sql.Database
import org.light.challenge.data.DatabaseInit

fun main() {
   val db = Database.connect("jdbc:sqlite::memory:", "org.sqlite.JDBC")
    DatabaseInit.CreateSchema()
    val inv = getEmployeeById(1)
    println(inv)
}
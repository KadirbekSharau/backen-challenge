package org.light.challenge.app

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

import org.light.challenge.data.Company
import org.light.challenge.data.Department
import org.light.challenge.data.Employee
import org.light.challenge.data.Invoice
import org.light.challenge.data.Rule
import org.light.challenge.data.Workflow
import org.light.challenge.data.Approval
fun main() {
    val db = Database.connect("jdbc:sqlite::memory:", "org.sqlite.JDBC")
    transaction{
        SchemaUtils.create(Company, Department, Employee, Invoice, Rule, Workflow, Approval)   
    }
}
package org.light.challenge.app

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.light.challenge.data.DatabaseInit
import org.light.challenge.logic.core.getAllWorkflowData
import org.light.challenge.logic.core.simulateWorkflowforInvoices

fun main() {
   val db = Database.connect("jdbc:sqlite::memory:", "org.sqlite.JDBC")
    transaction(db) {
        try {
            DatabaseInit(db)
            println("Database was successfully initialized.")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
        val workflows = getAllWorkflowData()
        for (workflow in workflows) {
            println("Checking all invoices in workflow ${workflow.name} of company with ID: ${workflow.companyId}")
            simulateWorkflowforInvoices(workflow.id)
        }
    }
}
package org.light.challenge.app

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.light.challenge.data.DatabaseInit


fun main() {
   val db = Database.connect("jdbc:sqlite::memory:", "org.sqlite.JDBC")
    transaction(db) {
        DatabaseInit(db)
        val workflows = getAllWorkflowData()
        for (workflow in workflows) {
            simulateWorkflowforInvoices(workflow.id)
        }
    }
}
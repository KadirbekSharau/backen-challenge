package org.light.challenge.data

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Initialize the database with creating tables and adding data migration
 * @param db the instance of the database to be initialized
 */
fun DatabaseInit(db: Database) {
    // Start transaction
    transaction(db) {
        // Create all the tables defined in the schema
        SchemaUtils.create(Company, Department, Employee, Invoice, Rule, Workflow, Approval)
        // Add data migration
        addDataMigration()
        try {
            // Create invoice from terminal
            createInvoiceFromTerminal()
            println("Your invoice was added successfully.")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
}

/**
 * Create invoice from terminal by asking for user input
 */
fun createInvoiceFromTerminal() {
    println("Give me amount of money for invoice (in USD): (Type in big decimal with scale 2)")
    val amountString = readLine()
    val amount = amountString?.toBigDecimal() ?: return
    println("Is manager approval required for this invoice? (Type Yes or No)")
    val approvalString = readLine()
    var approval: Boolean
    if (approvalString == "Yes") {
        approval = true
    } else if (approvalString == "No") {
        approval = false
    } else {return}
    println("Give me id of company: (Type integer number)")
    val companyIdString = readLine()
    val companyId = companyIdString?.toInt() ?: return
    println("Give me id of related department: (Type integer number)")
    val relatedIdString = readLine()
    val relatedId = relatedIdString?.toInt() ?: return
    println("Give me id of related workflow in that company: (Type integer number)")
    val workflowIdString = readLine()
    val workflowId = workflowIdString?.toInt() ?: return
    // Start transaction
    transaction {
        // Insert the invoice
        Invoice.insert {
            it[Invoice.amount] = amount
            it[is_manager_approval_required] = approval
            it[related_department_id] = relatedId
            it[workflow_id] = workflowId
            it[company_id] = companyId
        }
    }
}
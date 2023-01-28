package org.light.challenge.logic.core

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.light.challenge.data.Approval

/**
 * This function creates an Approval object and inserts it into the database
 * @param invoiceId the id of the invoice that is being approved
 * @param employeeId the id of the employee who is approving the invoice
 * @param approvalMethod the method of approval (email or slack)
 */
fun createApproval(invoiceId: Int, employeeId: Int, approvalMethod: Int) {
    //start a database transaction
    transaction {
        //Insert the Approval object into the Approval table
        Approval.insert {
            it[Approval.invoice_id] = invoiceId
            it[Approval.employee_id] = employeeId
            it[Approval.status_approved] = true
            it[Approval.approval_method] = approvalMethod
            it[Approval.date] = DateTime.now()
        }
    }
}

/**
 * This function sends an approval request for an invoice
 * @param invoice the invoice object that is being approved
 * @param employeeId the id of the employee who will approve the invoice
 */
fun sendRequest(invoice: InvoiceData, employeeId: Int) {
    val empl = getEmployeeById(employeeId)
    var approvalMethod: String = "Email"
    //Check if the employee has selected slack as their approval method
    if (empl?.approvalMethod == 1) {
        approvalMethod = "Slack"
    }
    println("Approval request sent for Invoice with ID: ${invoice.id} and amount: ${invoice.amount} \n" +
            "Approver: Employee ${empl?.name}, role ${empl?.role} \n" +
            "Approval method: $approvalMethod")
    if (empl != null) {
        try {
            //create the approval
            createApproval(invoice.id, empl.id, empl.approvalMethod)
            println("Invoice was approved!")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
}
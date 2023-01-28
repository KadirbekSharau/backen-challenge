package org.light.challenge.logic.core

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.light.challenge.data.Invoice
import java.math.BigDecimal

// Data class for holding invoice data
data class InvoiceData(
    val id: Int,
    val amount: BigDecimal,
    val isManagerApprovalRequired: Boolean,
    val relatedDepartmentId: Int,
    val companyId: Int,
    val workflowId: Int
)

// Extension function to convert ResultRow to InvoiceData
fun ResultRow.toInvoice(): InvoiceData {
    return InvoiceData(
        id = this[Invoice.id],
        amount = this[Invoice.amount],
        isManagerApprovalRequired = this[Invoice.is_manager_approval_required],
        relatedDepartmentId = this[Invoice.related_department_id],
        companyId = this[Invoice.company_id],
        workflowId = this[Invoice.workflow_id]
    )
}

// Validate an invoice against a set of rules
fun validateInvoice(invoice: InvoiceData, rules: List<RuleData>): Int {
    println("Applying rules for invoice with ID: ${invoice.id}")
    for (rule in rules) {
        if (rule.isManagerApprovalRequired != null && rule.isManagerApprovalRequired != invoice.isManagerApprovalRequired) {
            continue
        }
        if (rule.minAmount != null && rule.minAmount > invoice.amount) {
            continue
        }
        if (rule.maxAmount != null && rule.maxAmount!! < invoice.amount) {
            continue
        }
        if (rule.relatedDepartmentId != null && rule.relatedDepartmentId != invoice.relatedDepartmentId) {
            continue
        }
        return rule.employeeId
    }
    return 0
}

// Function to get all invoice data
fun getAllInvoiceData(): List<InvoiceData> {
    return transaction {
        // Select all invoices from the database and map them to InvoiceData objects
        Invoice.selectAll().map { it.toInvoice() }.toList()
    }
}

// Function to get invoice data by workflow Id
fun getInvoiceDataByWorkflowId(workflowId: Int): List<InvoiceData> {
    return transaction {
        // Select invoices by workflowId from the database and map them to InvoiceData objects
        Invoice.select {Invoice.workflow_id eq workflowId}.map { it.toInvoice() }.toList()
    }
}

// Function to get a single invoice by id
fun getInvoiceById(invoiceId: Int): InvoiceData? {
    return transaction {
        // Select a single invoice by id from the database and map it to an InvoiceData object
        Invoice.select { Invoice.id eq invoiceId }.firstOrNull()?.toInvoice()
    }
}

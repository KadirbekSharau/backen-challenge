package org.light.challenge.app

import java.math.BigDecimal
import org.light.challenge.data.Invoice
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.*
import org.light.challenge.data.Rule


data class InvoiceData(
  val id: Int,
  val amount: BigDecimal,
  val isManagerApprovalRequired: Boolean,
  val relatedDepartmentId: Int,
  val companyId: Int,
  val workflowId: Int
)

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

fun validateInvoice(invoice: InvoiceData, rules: List<RuleData>): Int {
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

fun getAllInvoiceData(): List<InvoiceData> {
    return transaction {
        Invoice.selectAll().map { it.toInvoice() }.toList()
    }
}

fun getInvoiceDataByWorkflowId(workflowId: Int): List<InvoiceData> {
    return transaction {
        Invoice.select {Invoice.workflow_id eq workflowId}.map { it.toInvoice() }.toList()
    }
}

fun getInvoiceById(invoiceId: Int): InvoiceData? {
    return transaction {
        Invoice.select { Invoice.id eq invoiceId }.firstOrNull()?.toInvoice()
    }
}

// fun createInvoice(amount: BigDecimal, isManagerApprovalRequired: Boolean, relatedDepartmentId: Int, companyId: Int, workflowId: Int) {
//     transaction {
//         val invoice = Invoice.insert {row ->
//             row[amount] = amount
//             row[is_manager_approval_required] = isManagerApprovalRequired
//             row[related_department_id] = relatedDepartmentId
//             row[company_id] = companyId
//             row[workflow_id] = workflowId
//         }
//     }
// }

// fun updateInvoiceStatus(invoiceId: Int, status: Boolean) {
//     transaction {
//         Invoice.update({ Invoice.id eq invoiceId }) {row ->
//             row[status] = status
//         }
//     }
// }

//fun deleteInvoice(invoiceId: Int): Boolean {
//    return transaction {
//        val rowsAffected = Invoice.deleteWhere { Invoice.id eq invoiceId }
//        rowsAffected > 0
//    }
//}

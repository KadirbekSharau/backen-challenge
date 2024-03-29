package org.light.challenge.logic.core

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.light.challenge.data.Rule
import org.light.challenge.data.Workflow
import java.math.BigDecimal

//Data class representing a workflow with id, name, and companyId properties
data class WorkflowData(
    val id: Int,
    val name: String,
    val companyId: Int
)

/**
 * Extension function to convert a ResultRow to a WorkflowData object
 * @param this the ResultRow to convert
 * @return the converted WorkflowData object
 */
fun ResultRow.toWorkflow(): WorkflowData {
    return WorkflowData(
        id = this[Workflow.id],
        name = this[Workflow.name],
        companyId = this[Workflow.company_id]
    )
}

 fun checkAllInvoices() {
     val workflows = getAllWorkflowData()
     for (wf in workflows) {
         simulateWorkflowforInvoices(wf.id)
     }
 }

 fun simulateWorkflowforInvoices(workflowId: Int) {
     println("Getting all rules and invoices referenced to workflow:")
     val rules = getRulesDataByWorkflowId(workflowId)
     val invoices = getInvoiceDataByWorkflowId(workflowId)

     // Iterate over invoices and apply the rules
     for (invoice in invoices) {
         val employeeId = validateInvoice(invoice, rules)
         sendRequest(invoice, employeeId)
     }
 }

 fun getAllWorkflowData(): List<WorkflowData> {
     println("Retrieving all workflows from database.")
     return transaction {
         Workflow.selectAll().map { it.toWorkflow() }.toList() ?: throw IllegalArgumentException("Workflows are not found")
     }
 }

 fun getWorkflowById(workflowId: Int): WorkflowData? {
     return transaction {
         Workflow.select { Workflow.id eq workflowId }
         .singleOrNull()?.toWorkflow() ?: throw IllegalArgumentException("Workflow with id $workflowId not found")
     }
 }

data class RuleData(
    val id: Int,
    val minAmount: BigDecimal?,
    val maxAmount: BigDecimal?,
    val isManagerApprovalRequired: Boolean?,
    val relatedDepartmentId: Int?,
    val employeeId: Int,
    val workFlowId: Int
)

fun ResultRow.toRule(): RuleData {
    return RuleData(
        id = this[Rule.id],
        minAmount = this[Rule.min_amount],
        maxAmount = this[Rule.max_amount],
        isManagerApprovalRequired = this[Rule.is_manager_approval_required],
        relatedDepartmentId = this[Rule.related_department_id],
        employeeId = this[Rule.employee_id],
        workFlowId = this[Rule.workflow_id]
    )
}

 fun getRulesDataByWorkflowId(workflowId: Int): List<RuleData> {
     return transaction {
         Rule.select { Rule.workflow_id eq workflowId }.map { it.toRule() }.toList()
     }
 }
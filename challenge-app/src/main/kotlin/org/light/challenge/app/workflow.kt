package org.light.challenge.app

 import org.light.challenge.data.Workflow
 import org.light.challenge.data.Rule
 import org.jetbrains.exposed.sql.transactions.transaction
 import org.jetbrains.exposed.sql.*
 import java.math.BigDecimal

data class WorkflowData(
    val id: Int,
    val name: String,
    val companyId: Int
)

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

     val rules = getRulesDataByWorkflowId(workflowId)

     val invoices = getInvoiceDataByWorkflowId(workflowId)

     // Iterate over invoices and apply the rules
     for (invoice in invoices) {
         val employeeId = validateInvoice(invoice, rules)
         sendRequestToSlack(invoice, employeeId)
     }
 }

 fun getAllWorkflowData(): List<WorkflowData> {
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
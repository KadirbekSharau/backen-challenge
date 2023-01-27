package org.light.challenge.app

// import org.light.challenge.data.Workflow
// import org.light.challenge.data.Rule
// import org.light.challenge.data.Invoice
// import org.jetbrains.exposed.sql.transactions.transaction
// import org.jetbrains.exposed.sql.*

// fun checkAllInvoices() {
//     val workflows = getAllWorkflowData()
//     for (wf in workflows) {
//         val invoices = getInvoiceDataByWorkflowId(wf.id)

//     }    
// }

// fun simulateWorkflowforInvoices(workflowId: Int) {

//     val rules = getRulesDataByWorkflowId(workflowId)

//     val invoices = getInvoiceDataByWorkflowId(workflowId)

//     // Iterate over invoices and apply the rules
//     for (invoice in invoices) {
//         val employeeId = validateInvoice(invoice, rules)
//         sendRequestToSlack()
//     }
// }

// fun validateInvoice(invoice: Invoice, rules: List<Rule>): Int {
//     for (rule in rules) {
//         if (!rule.is_manager_approval_required && rule.is_manager_approval_required != invoice.is_manager_approval_required) {
//             continue
//         }
//         if (!rule.min_amount && rule.min_amount > invoice.amount) {
//             continue
//         }
//         if (!rule.max_amount && rule.max_amount < invoice.amount) {
//             continue
//         }
//         if (!rule.related_department_id && rule.related_department_id != invoice.related_department_id) {
//             continue
//         }
//         return rule.employee_id
//     }
// }

// fun sendRequestToSlack(invoice: Invoice, employeeId: Int) {
//     val empl = getEmployeeById(employeeId)
//     println("Employee with name ${empl.name} approved it!")
// }

// fun sendRequestToEmail(invoice: Invoice, employeeId: Int) {
//     val empl = getEmployeeById(employeeId)
//     println("Employee with name ${empl.name} approved it!")
// }

// fun getAllWorkflowData(): List<Workflow> {
//     return transaction {
//         Workflow.selectAll() ?: throw IllegalArgumentException("Workflows are not found")
//     }
// }

// fun getWorkflowById(workflowId: Int): Workflow? {
//     return transaction {
//         Workflow.select { Workflow.id eq workflowId }
//         .singleOrNull() ?: throw IllegalArgumentException("Workflow with id $workflowId not found")
//     }
// }

// fun getRulesDataByWorkflowId(workflowId: Int): List<Rule> {
//     return transaction {
//         Rule.select { Rule.workflow_id eq workflowId }
//     }
// }
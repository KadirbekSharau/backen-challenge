package org.light.challenge.data

import org.jetbrains.exposed.sql.Table

// You can find visual structure of tables in README file
object Company : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 50)
}

object Department : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 50)
    val company_id = (integer("company_id") references Company.id)
}

object Employee : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 50)
    val is_manager = bool("is_manager")
    val role = varchar("role", 50).default("Member")
    val department_id = (integer("department_id") references Department.id)
    val approval_method = integer("approval_method") // 1: slack, 2: email
}

object Invoice : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val amount = decimal("amount", precision = 10, scale = 2)
    val is_manager_approval_required = bool("is_manager_approval_required")
    val related_department_id = (integer("related_department_id") references Department.id)
    val company_id = (integer("company_id") references Company.id)
    val workflow_id = (integer("workflow_id") references Workflow.id)
}

object Rule : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val min_amount = decimal("min_amount", precision = 10, scale = 2).nullable()
    val max_amount = decimal("max_amount", precision = 10, scale = 2).nullable()
    val is_manager_approval_required = bool("is_manager_approval_required").nullable()
    val related_department_id = (integer("related_department_id") references Department.id).nullable()
    val employee_id = (integer("employee_id") references Employee.id)
    val workflow_id = (integer("workflow_id") references Workflow.id)
}

object Workflow : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 50)
    val company_id = (integer("company_id") references Company.id)
}

object Approval : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val invoice_id = (integer("invoice_id") references Invoice.id)
    val employee_id = (integer("employee_id") references Employee.id)
    val status_approved = bool("status_approved")
    val date = date("date")
    val approval_method = integer("approval_method") // 1: slack, 2: email
}


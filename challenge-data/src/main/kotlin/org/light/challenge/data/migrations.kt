package org.light.challenge.data

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

// Adding workflow from fig.1 and some invoice mock data
fun addDataMigration() {
    transaction {
        Company.insert {
            it[name] = "Light"
        }

        Department.insert {
            it[name] = "Finance"
            it[company_id] = 1
        }
        Department.insert {
            it[name] = "Marketing"
            it[company_id] = 1
        }

        Employee.insert {
            it[name] = "Mark"
            it[is_manager] = true
            it[role] = "CFO"
            it[department_id] = 1
            it[approval_method] = 1
        }
        Employee.insert {
            it[name] = "Bob"
            it[is_manager] = false
            it[role] = "Member"
            it[department_id] = 1
            it[approval_method] = 2
        }
        Employee.insert {
            it[name] = "Steven"
            it[is_manager] = true
            it[role] = "CMO"
            it[department_id] = 2
            it[approval_method] = 1
        }
        Employee.insert {
            it[name] = "John"
            it[is_manager] = false
            it[role] = "Member"
            it[department_id] = 2
            it[approval_method] = 2
        }
        Employee.insert {
            it[name] = "Ray"
            it[is_manager] = true
            it[role] = "Manager"
            it[department_id] = 1
            it[approval_method] = 1
        }

        Workflow.insert {
            it[name] = "Example"
            it[company_id] = 1
        }

        Rule.insert {
            it[workflow_id] = 1
            it[min_amount] = BigDecimal(10000)
            it[related_department_id] = 2
            it[employee_id] = 1
        }
        Rule.insert {
            it[workflow_id] = 1
            it[min_amount] = BigDecimal(10000)
            it[employee_id] = 3
        }
        Rule.insert {
            it[workflow_id] = 1
            it[min_amount] = BigDecimal(5000)
            it[max_amount] = BigDecimal(10000)
            it[is_manager_approval_required] = true
            it[employee_id] = 5
        }
        Rule.insert {
            it[workflow_id] = 1
            it[min_amount] = BigDecimal(5000)
            it[max_amount] = BigDecimal(10000)
            it[is_manager_approval_required] = false
            it[employee_id] = 2
        }
        Rule.insert {
            it[workflow_id] = 1
            it[max_amount] = BigDecimal(5000)
            it[employee_id] = 2
        }

        Invoice.insert {
            it[amount] = BigDecimal(4000)
            it[is_manager_approval_required] = false
            it[related_department_id] = 1
            it[workflow_id] = 1
            it[company_id] = 1
        }
        Invoice.insert {
            it[amount] = BigDecimal(14000)
            it[is_manager_approval_required] = false
            it[related_department_id] = 1
            it[workflow_id] = 1
            it[company_id] = 1
        }
    }
}
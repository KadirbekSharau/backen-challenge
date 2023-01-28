package org.light.challenge.logic.core

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.light.challenge.data.Company
import org.light.challenge.data.Employee

/**
 * Data class representing the company information
 *
 * @param id: Int - company id
 * @param name: String - company name
 */
data class CompanyData(
    val id: Int,
    val name: String,
)

/**
 * Extension function for converting ResultRow to CompanyData
 *
 * @return CompanyData - the converted company data
 */
fun ResultRow.toCompany(): CompanyData {
    return CompanyData(
        id = this[Company.id],
        name = this[Company.name],
    )
}

/**
 * Data class representing the employee information
 *
 * @param id: Int - employee id
 * @param name: String - employee name
 * @param isManager: Boolean - whether the employee is a manager or not
 * @param role: String - employee role
 * @param departmentId: Int - employee department id
 * @param approvalMethod: Int - employee approval method
 */
data class EmployeeData(
    val id: Int,
    val name: String,
    val isManager: Boolean,
    val role: String,
    val departmentId: Int,
    val approvalMethod: Int
)

/**
 * Extension function for converting ResultRow to EmployeeData
 *
 * @return EmployeeData - the converted employee data
 */
fun ResultRow.toEmployee(): EmployeeData {
    return EmployeeData(
        id = this[Employee.id],
        name = this[Employee.name],
        isManager = this[Employee.is_manager],
        role = this[Employee.role],
        departmentId = this[Employee.department_id],
        approvalMethod = this[Employee.approval_method]
    )
}

/**
 * Get employee information by employee id
 *
 * @param employeeId: Int - employee id
 * @return EmployeeData? - employee data or null if not found
 */
fun getEmployeeById(employeeId: Int): EmployeeData? {
    return transaction {
        Employee.select { Employee.id eq employeeId }
            .singleOrNull()?.toEmployee()
    }
}
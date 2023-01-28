package org.light.challenge.app

import org.light.challenge.data.Employee
import org.light.challenge.data.Company
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.*
import org.light.challenge.data.toEmployee

data class CompanyData(
  val id: Int,
  val name: String,
)

fun ResultRow.toCompany(): CompanyData {
    return CompanyData(
        id = this[Company.id],
        name = this[Company.name],
    )
}

data class EmployeeData(
  val id: Int,
  val name: String,
  val is_manager: Boolean,
  val role: String,
  val department_id: Int
)

 fun ResultRow.toEmployee(): EmployeeData {
     return EmployeeData(
         id = this[Employee.id],
         name = this[Employee.name],
         is_manager = this[Employee.is_manager],
         role = this[Employee.role],
         department_id = this[Employee.department_id]
     )
 }

fun getEmployeeById(employeeId: Int): org.light.challenge.data.EmployeeData? {
    return transaction {
        Employee.select { Employee.id eq employeeId }
            .singleOrNull()?.toEmployee()
    }

}
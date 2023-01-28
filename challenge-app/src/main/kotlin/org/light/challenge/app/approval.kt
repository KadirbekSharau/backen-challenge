package org.light.challenge.app

import org.light.challenge.data.Invoice

fun sendRequestToSlack(invoice: InvoiceData, employeeId: Int) {
    val empl = getEmployeeById(employeeId)
    println("Employee with name ${empl?.name} approved it!")
}

fun sendRequestToEmail(invoice: InvoiceData, employeeId: Int) {
    val empl = getEmployeeById(employeeId)
    println("Employee with name ${empl?.name} approved it!")
}
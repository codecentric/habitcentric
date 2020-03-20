package de.codecentric.hc.report

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReportApplication

fun main(args: Array<String>) {
	runApplication<ReportApplication>(*args)
}

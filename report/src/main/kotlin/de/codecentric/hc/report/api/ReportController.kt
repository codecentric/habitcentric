package de.codecentric.hc.report.api

import de.codecentric.hc.report.api.model.Report
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ReportController : ReportsApi {

    override fun reportGet(): ResponseEntity<Report> {
        return ResponseEntity.ok().build()
    }
}
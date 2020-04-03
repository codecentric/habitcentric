package de.codecentric.hc.report.api

import de.codecentric.hc.report.api.model.Report
import de.codecentric.hc.report.api.model.Stats
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ReportController : ReportsApi {

    override fun reportGet(): ResponseEntity<Report> {
        val report = Report(Stats(42), Stats(42), Stats(42), Stats(42))
        return ResponseEntity.ok(report)
    }
}
package de.codecentric.hc.report.date

import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DateService {
    fun today(): LocalDate = LocalDate.now()
}
package dianna_monsalve.com.example.registros_app.domain

import java.time.LocalDate
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField


class FormatDate {
    val formatter = DateTimeFormatterBuilder()
        .appendValue(ChronoField.DAY_OF_MONTH)
        .appendLiteral("-")
        .appendValue(ChronoField.MONTH_OF_YEAR)
        .appendLiteral("-")
        .appendValue(ChronoField.YEAR)
        .toFormatter()

    operator fun invoke(fecha: LocalDate):String {
        return fecha.format(formatter)
    }
}
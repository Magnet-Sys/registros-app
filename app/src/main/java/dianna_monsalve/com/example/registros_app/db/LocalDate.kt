package dianna_monsalve.com.example.registros_app.db

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDate {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
}
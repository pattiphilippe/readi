package patti.philippe.read_i.db

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun disasterTypeToString(type: DisasterType) = type.name

    @TypeConverter
    fun stringToDisasterType(name: String) = DisasterType.valueOf(name)

    @TypeConverter
    fun timestampToDate(value: Long) = Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date) = date.time

    @TypeConverter
    fun disasterGravityToInt(gravity: DisasterGravity) = when(gravity){
        DisasterGravity.INFO -> 1
        DisasterGravity.WARNING -> 2
        DisasterGravity.CRITICAL -> 3
    }

    @TypeConverter
    fun intToDisasterGravity(gravity: Int) = when(gravity){
        1 -> DisasterGravity.INFO
        2 -> DisasterGravity.WARNING
        else -> DisasterGravity.CRITICAL
    }
}
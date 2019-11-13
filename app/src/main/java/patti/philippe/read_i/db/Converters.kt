package patti.philippe.read_i.db

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun fromDisasterType(type: DisasterType) = type.name

    @TypeConverter
    fun fromDisasterTypeName(name: String) = DisasterType.valueOf(name)

    @TypeConverter
    fun fromTimestamp(value: Long) = Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date) = date.time
}
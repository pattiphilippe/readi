package patti.philippe.read_i.db

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromDisasterType(type: DisasterType?): String? = type?.name

    @TypeConverter
    fun fromDisasterTypeName(name: String?): DisasterType? =
        name?.let { DisasterType.valueOf(name) }
}
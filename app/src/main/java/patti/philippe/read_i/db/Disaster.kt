package patti.philippe.read_i.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.Comparator


@Entity(tableName = "disaster_table")
data class Disaster(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: DisasterType,
    val latitude: Double,
    val longitude: Double,
    val date: Date,
    val gravity: DisasterGravity = DisasterGravity.INFO
)

data class Coordinate(val latitude: Double, val longitude: Double)


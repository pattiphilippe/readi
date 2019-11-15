package patti.philippe.read_i.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

//TODO check if table name necessary
@Entity(tableName = "disaster_table")
data class Disaster(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: DisasterType,
    @Embedded
    val location:Coordinate,
    val date:Date,
    val gravity: DisasterGravity = DisasterGravity.INFO)

data class Coordinate(val longitude: Double, val latitude: Double)
package patti.philippe.read_i.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "disaster_table")
class Disaster(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "type") val type: DisasterType,
    @ColumnInfo(name = "location") val location:String,
    @ColumnInfo(name = "date") val date:Date,
    @ColumnInfo(name = "gravity") val gravity: DisasterGravity = DisasterGravity.INFO){

    override fun toString(): String = type.toString()
}
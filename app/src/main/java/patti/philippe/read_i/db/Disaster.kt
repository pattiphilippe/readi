package patti.philippe.read_i.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "disaster_table")
class Disaster(@PrimaryKey @ColumnInfo(name = "type") val type: DisasterType){

    override fun toString(): String = type.toString()

}
package patti.philippe.read_i.db

import android.location.Location

data class Alert(val disaster: Disaster, var myLocation : Location){

    val distanceToMe : Float

    init {
        val disasterLocation = Location("").apply {
        latitude = disaster.location.latitude
        longitude = disaster.location.longitude
        }
        distanceToMe = disasterLocation.distanceTo(myLocation)
    }
}
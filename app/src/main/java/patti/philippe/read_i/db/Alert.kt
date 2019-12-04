package patti.philippe.read_i.db

import android.location.Location

data class Alert(val disaster: Disaster){

    var myLocation : Location? = null
    set(value) {
        field = value
        computeDistanceToMe()
    }
    var distanceToMe : Float? = null
    private set

            private fun computeDistanceToMe(){
                myLocation?.let {
                    val disasterLocation = Location("").apply {
                        latitude = disaster.latitude
                        longitude = disaster.longitude
                    }
                    distanceToMe = myLocation!!.distanceTo(disasterLocation)
        }
    }

}
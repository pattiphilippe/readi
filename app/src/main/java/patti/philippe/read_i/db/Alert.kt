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

    constructor(disaster: Disaster, myLocation: Location) : this(disaster){
        this.myLocation = myLocation
        computeDistanceToMe()
    }

    private fun computeDistanceToMe(){
        myLocation?.let {
            val disasterLocation = Location("").apply {
                latitude = disaster.location.latitude
                longitude = disaster.location.longitude
            }
            distanceToMe = disasterLocation.distanceTo(myLocation)
        }
    }

}
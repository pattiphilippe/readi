package patti.philippe.read_i.db

import android.location.Location

data class Alert(val disaster: Disaster) {

    private val mDisasterLocation = Location("").apply {
        latitude = disaster.latitude
        longitude = disaster.longitude
    }


    var mDistanceToMe: Float? = null

    constructor(disaster: Disaster, myLocation: Location) : this(disaster){
        setDistanceToMe(myLocation)
    }

    fun setDistanceToMe(myLocation: Location) {
        mDistanceToMe = myLocation.distanceTo(mDisasterLocation)
    }

}
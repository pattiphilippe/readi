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

class ComparatorGravity : Comparator<Alert> {
    override fun compare(o1: Alert?, o2: Alert?): Int {
        if (o1?.disaster?.gravity == o2?.disaster?.gravity) return 0
        if (o1 == null && o2 != null) return 1
        if (o1 != null && o2 == null) return -1
        if (o1!!.disaster.gravity == DisasterGravity.CRITICAL) return -1
        if (o2!!.disaster.gravity == DisasterGravity.CRITICAL) return 1
        if (o1.disaster.gravity == DisasterGravity.WARNING) {
            return -1
        }
        return 1
    }
}
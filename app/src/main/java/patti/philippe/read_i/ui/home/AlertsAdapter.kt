package patti.philippe.read_i.ui.home

import android.content.Context
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import patti.philippe.read_i.R
import patti.philippe.read_i.db.*
import patti.philippe.read_i.db.DisasterType.*
import patti.philippe.read_i.ui.home.HomeFragmentDirections.Companion.actionNavHomeToFragmentExtremeHeat
import patti.philippe.read_i.ui.home.HomeFragmentDirections.Companion.actionNavHomeToFragmentFlood
import patti.philippe.read_i.ui.home.HomeFragmentDirections.Companion.actionNavHomeToFragmentHurricane
import java.text.DateFormat
import java.util.*
import kotlin.math.roundToInt

class AlertsAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<AlertsAdapter.AlertViewHolder>() {

    private val _tag = "AlertsAdapter"

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var mAlerts = mutableListOf<Alert>()
    private lateinit var mLocation: Location
    private val dateFormatter = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale("en", "US"))
    private val comparator = ComparatorGravity().thenBy{it.mDistanceToMe}

    inner class AlertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val icon: ImageView = itemView.findViewById(R.id.alert_icon)
        val location: TextView = itemView.findViewById(R.id.alert_location)
        val timestamp: TextView = itemView.findViewById(R.id.alert_timestamp)
        val gravityIcon: ImageView = itemView.findViewById(R.id.alert_gravity)
        lateinit var disasterType: DisasterType

        override fun onClick(v: View?) {
            Log.d(_tag, "Alert info button onClick handler")
            if (v?.id == R.id.alert_info_btn) {
                val action = when (disasterType) {
                    HURRICANE ->
                        actionNavHomeToFragmentHurricane()
                    EXTREME_HEAT ->
                        actionNavHomeToFragmentExtremeHeat()
                    FLOOD ->
                        actionNavHomeToFragmentFlood()
                    else -> null
                }
                action?.let {
                    Log.d(_tag, "switching to new fragment")
                    v.findNavController().navigate(action)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val itemView = inflater.inflate(R.layout.disaster_recyclerview_item, parent, false)
        return AlertViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val current = mAlerts[position]
        updateDisasterFields(holder, current)
        updateLocationField(holder, current)
        initButtonsListener(holder, current)
    }

    private fun updateDisasterFields(holder: AlertViewHolder, current: Alert) {
        holder.icon.setImageResource(getAlertIconId(current.disaster.type))
        holder.timestamp.text = dateFormatter.format(current.disaster.date)
        holder.gravityIcon.setImageResource(getAlertGravityIconId(current.disaster.gravity))
    }

    private fun updateLocationField(holder: AlertViewHolder, current: Alert) {
        val location = """(${current.mDistanceToMe?.div(1000)?.roundToInt()} km)""".trimMargin()
        holder.location.text = location
    }

    private fun initButtonsListener(holder: AlertViewHolder, current: Alert) {
        holder.disasterType = current.disaster.type
        (holder.itemView.findViewById<ImageButton>(R.id.alert_info_btn)).setOnClickListener(holder)
    }

    private fun getAlertIconId(type: DisasterType) = when (type) {
        AVALANCHE -> R.mipmap.avalanche_icon
        EARTHQUAKE -> R.mipmap.earthquake_icon
        EXTREME_HEAT -> R.mipmap.extreme_heat_icon
        FIRE -> R.mipmap.fire_icon
        FLOOD -> R.mipmap.flood_icon
        HURRICANE -> R.mipmap.hurricane_icon
        TSUNAMI -> R.mipmap.tsunami_icon
        VOLCANIC_ERUPTION -> R.mipmap.volcanic_eruption_icon
    }

    private fun getAlertGravityIconId(gravity: DisasterGravity) = when (gravity) {
        DisasterGravity.WARNING -> R.drawable.ic_info_24dp
        DisasterGravity.CRITICAL -> R.drawable.ic_warning_24dp
        else -> 0
    }

    //TODO put setters in a Coroutine scope, each waiting the end of the other call, before executing
    //TODO notify with payloads
    internal fun setDisasters(disasters: List<Disaster>) {
        if (::mLocation.isInitialized) {
            this.mAlerts = MutableList(disasters.size) { index -> Alert(disasters[index], mLocation) }
        } else {
            this.mAlerts = MutableList(disasters.size) { index -> Alert(disasters[index]) }
        }
        this.mAlerts.sortWith(comparator)
        notifyDataSetChanged()
    }

    internal fun setLocation(location: Location) {
        this.mLocation = location
        this.mAlerts.forEach { it.setDistanceToMe(mLocation) }
        this.mAlerts.sortWith(comparator)
        notifyDataSetChanged()
    }

    override fun getItemCount() = mAlerts.size


}
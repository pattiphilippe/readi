package patti.philippe.read_i.ui.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.location.Location
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.disaster_recyclerview_item.view.*
import patti.philippe.read_i.R
import patti.philippe.read_i.db.Alert
import patti.philippe.read_i.db.Disaster
import patti.philippe.read_i.db.DisasterGravity
import patti.philippe.read_i.db.DisasterType

class DisasterListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<DisasterListAdapter.DisasterViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var alerts = mutableListOf<Alert>()


    inner class DisasterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alert: CardView = itemView.findViewById(R.id.alert)
        val buttonsLayout: LinearLayout = itemView.findViewById(R.id.alertButtons)
        val icon: ImageView = itemView.findViewById(R.id.alerticon)
        val location: TextView = itemView.findViewById(R.id.alertLocation)
        val timestamp: TextView = itemView.findViewById(R.id.alertTimestamp)
        val gravityIcon : ImageView = itemView.findViewById(R.id.alertGravity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisasterViewHolder {
        val itemView = inflater.inflate(R.layout.disaster_recyclerview_item, parent, false)
        return DisasterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DisasterViewHolder, position: Int) {
        val current = alerts[position]
        holder.alert.setOnClickListener{holder.buttonsLayout.isEnabled = !holder.buttonsLayout.isEnabled}
        holder.icon.setImageResource(getAlertIconId(current.disaster.type))
        holder.location.text = current.disaster.location.toString()
        holder.timestamp.text = current.disaster.date.toString()
        holder.gravityIcon.setImageResource(getAlertGravityIconId(current.disaster.gravity))
    }

    private fun getAlertIconId(type: DisasterType) = when (type) {
        DisasterType.AVALANCHE -> R.drawable.avalanche_icon
        DisasterType.EARTHQUAKE -> R.drawable.earthquake_icon
        DisasterType.EXTREME_HEAT -> R.drawable.extreme_heat_icon
        DisasterType.FIRE -> R.drawable.fire_icon
        DisasterType.FLOOD -> R.drawable.flood_icon
        DisasterType.HURRICANE -> R.drawable.hurricane_icon
        DisasterType.TSUNAMI -> R.drawable.tsunami_icon
        DisasterType.VOLCANIC_ERUPTION -> R.drawable.volcanic_eruption_icon
    }

    private fun getAlertGravityIconId(gravity: DisasterGravity) = when(gravity){
        DisasterGravity.WARNING -> R.drawable.ic_info_24dp
        DisasterGravity.CRITICAL -> R.drawable.ic_warning_24dp
        else -> 0
    }


    internal fun setDisasters(disasters: List<Disaster>) {
        val location = Location("").apply {
            latitude = 50.0
            longitude = 4.0
        }
        disasters.forEach { alerts.add(Alert(it, location)) }
        notifyDataSetChanged()
    }

    override fun getItemCount() = alerts.size
}
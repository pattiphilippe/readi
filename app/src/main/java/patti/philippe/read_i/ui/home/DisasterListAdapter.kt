package patti.philippe.read_i.ui.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.disaster_recyclerview_item.view.*
import patti.philippe.read_i.R
import patti.philippe.read_i.db.Disaster
import patti.philippe.read_i.db.DisasterGravity
import patti.philippe.read_i.db.DisasterType

class DisasterListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<DisasterListAdapter.DisasterViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var disasters = emptyList<Disaster>()

    inner class DisasterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //TODO check if good R reference
        val alert: LinearLayout = itemView.findViewById(R.id.alert)
        val icon: ImageView = itemView.findViewById(R.id.alerticon)
        val location: TextView = itemView.findViewById(R.id.alertLocation)
        val distance: TextView = itemView.findViewById(R.id.alertDistance)
        val timestamp: TextView = itemView.findViewById(R.id.alertTimestamp)
        val advice: TextView = itemView.findViewById(R.id.alertAdvice)
        val moreInfo: Button = itemView.findViewById(R.id.alertMoreInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisasterViewHolder {
        val itemView = inflater.inflate(R.layout.disaster_recyclerview_item, parent, false)
        return DisasterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DisasterViewHolder, position: Int) {
        val current = disasters[position]
        holder.alert.setBackgroundResource(getBackgroundDrawableId(current.gravity))
        holder.icon.setImageResource(getAlertIconId(current.type))
        holder.location.text = current.location
        holder.timestamp.text = current.date.toString()
        holder.distance.text = "30 km"
        holder.advice.text = "ADVICE"
    }

    private fun getBackgroundDrawableId(gravity: DisasterGravity): Int = when (gravity){
        DisasterGravity.INFO -> R.drawable.alert_info
        DisasterGravity.WARNING -> R.drawable.alert_warning
        DisasterGravity.CRITICAL -> R.drawable.alert_critical
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


    internal fun setDisasters(disasters: List<Disaster>) {
        this.disasters = disasters
        notifyDataSetChanged()
    }

    override fun getItemCount() = disasters.size
}
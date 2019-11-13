package patti.philippe.read_i.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.disaster_recyclerview_item.view.*
import patti.philippe.read_i.R
import patti.philippe.read_i.db.Disaster

class DisasterListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<DisasterListAdapter.DisasterViewHolder>(){

    private val inflater : LayoutInflater = LayoutInflater.from(context)
    private var disasters = emptyList<Disaster>()

    inner class DisasterViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        //TODO check if good R reference
        val disasterItemView: TextView = itemView.findViewById(R.id.disaster_recyclerview_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisasterViewHolder {
        val itemView = inflater.inflate(R.layout.disaster_recyclerview_item, parent, false)
        return DisasterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DisasterViewHolder, position: Int) {
        val current = disasters[position]
        holder.disasterItemView.text = current.type.name
    }

    internal fun setDisasters(disasters: List<Disaster>) {
        this.disasters = disasters
        notifyDataSetChanged()
    }

    override fun getItemCount() = disasters.size
}
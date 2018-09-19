package com.example.delivery.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.delivery.R
import com.example.delivery.dbmodel.DeliveryRealmObject
import com.example.delivery.extra.GTImageView


/**
 * Created by Ganesh Tikone on 17/9/18.
 * Class: DeliveryAdapter
 */
class DeliveryAdapter : RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {


    private val listOfDelivery = mutableListOf<DeliveryRealmObject>()

    private var listener: OnDeliveryItemClickListener? = null

    private val mLastClickTime = System.currentTimeMillis()
    private val CLICK_TIME_INTERVAL: Long = 300


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_thing_to_deliver, parent, false)
        return DeliveryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listOfDelivery.size
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {

        val delivery = listOfDelivery[position]
        holder.imageView.loadImageFromUrl(delivery.imageUrl)
        holder.descriptionTextView.text = delivery.description
    }


    /**
     *
     */
    fun updateData(data: MutableList<DeliveryRealmObject>) {
        listOfDelivery.clear()
        listOfDelivery.addAll(data)
        notifyDataSetChanged()
    }

    /**
     *
     */
    fun setListener(delegate: OnDeliveryItemClickListener) {
        this.listener = delegate
    }

    /**
     * ViewHolder class for RecyclerView
     */
    inner class DeliveryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: GTImageView = itemView.findViewById(R.id.gtImageView)
        var descriptionTextView: TextView = itemView.findViewById(R.id.deliveryDescriptionTextView)
        var rowView: View = itemView.findViewById(R.id.rowView)

        init {
            handleClick()
        }

        fun handleClick() {
            rowView.setOnClickListener {
                val now = System.currentTimeMillis()
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return@setOnClickListener
                } else {
                    listener?.onItemClick(listOfDelivery[adapterPosition].id)
                }

            }
        }
    }


    // --- Interface ---

    interface OnDeliveryItemClickListener {
        fun onItemClick(itemId: Int)
    }

}
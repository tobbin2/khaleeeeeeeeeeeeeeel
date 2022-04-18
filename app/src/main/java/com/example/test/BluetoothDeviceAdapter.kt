/*package com.example.test

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar


class DataModel(var name: String)

class BluetoothDeviceAdapter(data: ArrayList<DataModel>, context: Context) :
    ArrayAdapter<DataModel?>(context, R.layout.row_item, data), View.OnClickListener {
    private val dataSet: ArrayList<DataModel>
    var mContext: Context

    // View lookup cache
    private class ViewHolder {
        var txtName: TextView? = null
    }

    override fun onClick(v: View) {
        val position = v.getTag() as Int
        val `object`: Any? = getItem(position)
        val dataModel: DataModel? = `object` as DataModel?
        when (v.getId()) {
            R.id.item_info -> Snackbar.make(
                v,
                "Release date " + dataModel.getFeature(),
                Snackbar.LENGTH_LONG
            )
                .setAction("No action", null).show()
        }
    }

    private var lastPosition = -1

    fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        // Get the data item for this position
        var convertView: View? = convertView
        val dataModel: DataModel? = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view
        val viewHolder: ViewHolder // view lookup cache stored in tag
        val result: View?
        if (convertView == null) {
            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.row_item, parent, false)
            viewHolder.txtName = convertView.findViewById(R.id.name)

            result = convertView
            convertView.setTag(viewHolder)
        } else {
            viewHolder = convertView.getTag()
            result = convertView
        }

        lastPosition = position
        viewHolder.txtName?.setText(dataModel.getName())
        // Return the completed view to render on screen
        return convertView
    }

    init {
        dataSet = data
        mContext = context
    }
}*/
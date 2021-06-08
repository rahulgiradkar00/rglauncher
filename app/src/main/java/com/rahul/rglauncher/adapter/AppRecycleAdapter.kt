package com.rahul.rglauncher.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rahul.applicationmodule.model.AppModel
import com.rahul.rglauncher.MainActivity
import com.rahul.rglauncher.R
import com.rahul.rglauncher.adapter.AppRecycleAdapter.AppViewHolder
import java.util.*

class AppRecycleAdapter(var context: Context, var appAllList: List<AppModel>) :
    RecyclerView.Adapter<AppViewHolder>() {
    var appSearchedList: MutableList<AppModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.setItem(appSearchedList[position])
    }

    override fun getItemCount(): Int {
        return appSearchedList.size
    }

    fun modifySeach(query: String) {
        appSearchedList.clear()
        for (a in appAllList) {
            if (a.appName.toLowerCase().contains(query.toLowerCase())) {
                appSearchedList.add(a)
            }
        }
        notifyDataSetChanged()
    }

    fun updateList(installedAppList: List<AppModel>) {
        appAllList = installedAppList
        appSearchedList.clear()
        appSearchedList.addAll(installedAppList)
    }

    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mLayout: View
        var mImage: ImageView
        var mLabel: TextView
        fun setItem(appModel: AppModel) {
            mImage.setImageDrawable(appModel.appIcon)
            mLabel.text = appModel.appName
            mLayout.setOnClickListener {
                (context as MainActivity).itemPress(appModel)
                Log.e("Item clicked", appModel.appName + "")
            }
            mLayout.setOnLongClickListener {
                (context as MainActivity).itemLongPress(appModel)
                false
            }
            mLayout.setOnCreateContextMenuListener { menu, v, menuInfo -> /*MenuInflater inflater = menu..getMenuInflater();
                    inflater.inflate(R.menu.menu_main, menu);*/
                menu.add(
                    Menu.NONE, R.id.appInfo,
                    Menu.NONE, "App Info"
                )
                menu.add(
                    Menu.NONE, R.id.appUnistall,
                    Menu.NONE, "Uninstall"
                )
            }
        }

        init {
            mLayout = itemView.findViewById(R.id.layout)
            mImage = itemView.findViewById(R.id.image)
            mLabel = itemView.findViewById(R.id.label)
        }
    }

    init {
        appSearchedList = ArrayList()
        appSearchedList.addAll(appAllList)
    }
}
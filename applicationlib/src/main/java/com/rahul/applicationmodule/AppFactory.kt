package com.rahul.applicationmodule

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.NameNotFoundException
import android.util.Log
import com.rahul.applicationmodule.model.AppModel
import java.util.*

class AppFactory(private val applicationContext: Context) {
    /***
     * Get App list
     * @return List of app
     * @param order
     */
    fun getInstalledAppList(order: Order): List<AppModel> {
        val list: MutableList<AppModel> = ArrayList()
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val untreatedAppList = applicationContext.packageManager.queryIntentActivities(intent, 0)
        val packageManager = applicationContext.packageManager
        for (untreatedApp in untreatedAppList) {
            try {
                val appName = untreatedApp.activityInfo.loadLabel(packageManager).toString()
                val appPackageName = untreatedApp.activityInfo.packageName
                val appImage = untreatedApp.activityInfo.loadIcon(packageManager)
                val ss = packageManager.getPackageInfo(appPackageName, 0)
                val app = AppModel(
                    appPackageName,
                    appName,
                    appImage,
                    untreatedApp.activityInfo.name,
                    ss.versionName,
                    ss.longVersionCode
                )
                if (!list.contains(app)) {
                    list.add(app)
                }
                Log.e("app: - ", app.toString())
            } catch (e: NameNotFoundException) {
                e.printStackTrace()
            }
        }
        if (order == Order.ASC) {
            list.sortBy { it.appName.lowercase() }
        } else if (order == Order.ASC) {
            list.sortByDescending { it.appName.lowercase() }
        }
        return list
    }

    enum class Order {
        ASC, DESC, DEFAULT
    }
}
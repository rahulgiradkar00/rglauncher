package com.rahul.applicationmodule.model

import android.graphics.drawable.Drawable

data class AppModel(
    var packageName: String,
    var appName: String,
    var appIcon: Drawable,
    var mainActivityName: String,
    var versionName: String,
    var versionCode: Long

)
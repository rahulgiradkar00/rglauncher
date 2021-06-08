package com.rahul.rglauncher.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class IconView : AppCompatImageView {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}
    constructor(
        context: Context?, attrs: AttributeSet?,
        defStyle: Int
    ) : super(context!!, attrs, defStyle) {
    }

    override fun onMeasure(width: Int, height: Int) {
        super.onMeasure(width, height)
        val measuredWidth = measuredWidth
        val measuredHeight = measuredHeight
        if (measuredWidth > measuredHeight) {
            setMeasuredDimension(measuredHeight, measuredHeight)
        } else {
            setMeasuredDimension(measuredWidth, measuredWidth)
        }
    }
}
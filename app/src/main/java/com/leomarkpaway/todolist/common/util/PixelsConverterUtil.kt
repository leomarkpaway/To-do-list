package com.leomarkpaway.todolist.common.util

import android.content.Context
import android.util.TypedValue

fun convertPixelsToDp(context: Context, pixels: Int): Float {
    val displayMetrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pixels.toFloat(), displayMetrics)
}

fun convertDpToPixels(context: Context, dp: Float): Int {
    val displayMetrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics).toInt()
}
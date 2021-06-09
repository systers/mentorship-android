package org.anitab.mentorship.dsl

import android.content.Context
import android.util.TypedValue

fun Context.getColorAttr(colorResId: Int): Int {
    val typedValue = TypedValue()
    val typedArray = obtainStyledAttributes(typedValue.data, intArrayOf(colorResId))
    val color = typedArray.getColor(0, 0)
    typedArray.recycle()
    return color
}

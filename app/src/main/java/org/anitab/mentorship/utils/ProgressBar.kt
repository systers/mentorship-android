package org.anitab.mentorship.utils

import android.R
import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout

/**
 * This class will show and hide a Progressbar in an activity or a fragment.
 * @param context of the activity or fragment where the progressbar is to be shown.
 */
class ProgressBar(context: Context) {

    private val progressBar: ProgressBar

    init {

        val layout = (context as Activity).findViewById<View>(R.id.content).rootView as ViewGroup

        progressBar = ProgressBar(context, null, R.attr.progressBarStyleLargeInverse)
        progressBar.isIndeterminate = true

        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT)

        val rl = RelativeLayout(context)

        rl.gravity = Gravity.CENTER
        rl.addView(progressBar)

        layout.addView(rl, params)

        hide()
    }

    /**
     * Shows the progressbar
     */
    fun show() {
        progressBar.visibility = View.VISIBLE
    }

    /**
     * Hides the progressbar
     */
    fun hide() {
        progressBar.visibility = View.INVISIBLE
    }
}

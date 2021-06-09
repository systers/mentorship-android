package org.anitab.mentorship.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.anitab.mentorship.view.activities.BaseActivity

/**
 * A Fragment class which other Fragments can extend from. It reduces boilerplate of
 * other fragments setting up their own views and  action bat title
 */
abstract class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResourceId(), container, false)
    }

    /**
     * This function is responsible to return the layout resource id of the fragment,
     * so that the view can be set by the parent fragment
     * @return layout resource id of the fragment child view
     */
    protected abstract fun getLayoutResourceId(): Int

    /**
     * Instance of parent activity as [BaseActivity]
     */
    val baseActivity: BaseActivity
        get() = activity as BaseActivity
}

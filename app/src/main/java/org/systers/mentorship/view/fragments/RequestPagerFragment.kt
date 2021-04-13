package org.systers.mentorship.view.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.android.synthetic.main.fragment_request_pager.*
import org.systers.mentorship.R
import org.systers.mentorship.models.Relationship
import org.systers.mentorship.utils.Constants
import org.systers.mentorship.view.activities.RequestDetailActivity
import org.systers.mentorship.view.adapters.RequestsAdapter
import javax.inject.Inject

/**
 * This fragment is instantiated per each tab from the RequestsFragment ViewPager
 */
class RequestPagerFragment: BaseFragment() {
    @ApplicationContext
    @Inject
    lateinit var appContext: Context
    companion object {
        /**
         * Creates an instance of [RequestPagerFragment]
         */
        fun newInstance(requestsList: List<Relationship>, emptyListText: String) : BaseFragment {

            val args = Bundle()
            args.putParcelableArrayList(Constants.REQUEST_LIST, ArrayList(requestsList))
            args.putString(Constants.REQUEST_EMPTY_LIST_TEXT, emptyListText)

            val frag = RequestPagerFragment()
            frag.arguments = args

            return frag
        }

        val deletedRequests = mutableSetOf<Int>()
    }

    private lateinit var requestsList: MutableList<Relationship>
    private lateinit var emptyListText: String

    override fun getLayoutResourceId() = R.layout.fragment_request_pager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            requestsList = it.getParcelableArrayList(Constants.REQUEST_LIST)
            emptyListText = it.getString(Constants.REQUEST_EMPTY_LIST_TEXT)
        }
        setView()
    }

    private val openRequestDetail: (Relationship) -> Unit =
            { requestDetail ->
                val intent = Intent(activity, RequestDetailActivity::class.java)
                intent.putExtra(Constants.RELATIONSHIP_EXTRA, requestDetail)
                startActivityForResult(intent, Constants.DELETE_REQUEST_RESULT_ID)
            }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val id = data?.getStringExtra(Constants.REQUEST_ID)
        if (id != null) {
            deletedRequests.add(id.toInt())
        }

        setView()
    }
    
    private fun setView() {
        val filtered = requestsList.filter {
            val id = it.id
            !deletedRequests.contains(id)
        }

        if (filtered.isEmpty()) {
            tvEmptyList.text = emptyListText
            rvRequestsList.visibility = View.GONE
        } else {
            rvRequestsList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = RequestsAdapter(appContext, filtered, openRequestDetail)
            }
            tvEmptyList.visibility = View.GONE
        }
    }
}

package org.systers.mentorship.view.fragments


import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.fragment_relation_pager.*
import org.systers.mentorship.R
import org.systers.mentorship.models.Relationship
import org.systers.mentorship.utils.EXTENDED_DATE_FORMAT
import org.systers.mentorship.utils.convertUnixTimestampIntoStr
import org.systers.mentorship.view.activities.MainActivity
import org.systers.mentorship.viewmodels.RelationViewModel

@SuppressLint("ValidFragment")
/**
 * The fragment is responsible present the current mentorship relation  details
 */
class RelationFragment(private var mentorshipRelation: Relationship) : BaseFragment() {

    companion object {
        /**
         * Creates an instance of RelationFragment
         */
        fun newInstance(mentorshipRelation: Relationship) = RelationFragment(mentorshipRelation)
        val TAG = RelationFragment::class.java.simpleName
    }

    private val relationViewModel by lazy {
        ViewModelProviders.of(this).get(RelationViewModel::class.java)
    }
    private val activityCast by lazy { activity as MainActivity }

    private val alertDialog by lazy { activity?.let { AlertDialog.Builder(it) } }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_relation_pager
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activityCast.showProgressDialog(getString(R.string.fetching_users))
        populateView(mentorshipRelation)

        relationViewModel.successfulCancel.observe(viewLifecycleOwner, Observer { successful ->
            activityCast.hideProgressDialog()
            if (successful != null) {
                if (successful) {
                    baseActivity.replaceFragment(R.id.contentFragment, RelationPagerFragment.newInstance(), R.string.fragment_title_relation)
                    tvMenteeLabel.visibility = View.GONE
                    tvMentorLabel.visibility = View.GONE
                    tvEndDateLabel.visibility = View.GONE
                    tvNotesLabel.visibility = View.GONE
                    btnCancelRelation.visibility = View.GONE
                    tvMentorName.visibility = View.GONE
                    tvMenteeName.visibility = View.GONE
                    tvEndDate.visibility = View.GONE
                    tvRelationNotes.visibility = View.GONE
                } else {
                    view?.let {
                        Snackbar.make(it, relationViewModel.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })
        tvRelationNotes.movementMethod = ScrollingMovementMethod()
    }

    private fun populateView(relationResponse: Relationship) {

        activityCast.hideProgressDialog()
            tvMentorName.text = relationResponse.mentor.name
            tvMenteeName.text = relationResponse.mentee.name
            tvEndDate.text = convertUnixTimestampIntoStr(
                    relationResponse.endDate, EXTENDED_DATE_FORMAT)
            tvRelationNotes.text = relationResponse.notes
            btnCancelRelation.visibility = View.VISIBLE
            btnCancelRelation.setOnClickListener {

                with(alertDialog) {
                    this?.setTitle(getString(R.string.cancel_relation_title))
                    this?.setMessage(getString(R.string.cancel_relation_text))
                    this?.setCancelable(false)
                    this?.setPositiveButton(getString(R.string.confirm_cancel_relation)) { dialog, which ->
                        relationViewModel.cancelMentorshipRelation(relationResponse.id)
                    }
                    this?.setNegativeButton(getString(R.string.cancel_relation_denied)) { dialog, which ->
                        dialog.dismiss()
                    }
                }?.create()?.show()
            }
    }
}

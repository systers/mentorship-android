package org.anitab.mentorship.view.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_request_detail.*
import org.anitab.mentorship.R
import org.anitab.mentorship.models.RelationState
import org.anitab.mentorship.models.Relationship
import org.anitab.mentorship.utils.Constants
import org.anitab.mentorship.utils.EXTENDED_DATE_FORMAT
import org.anitab.mentorship.utils.convertUnixTimestampIntoStr
import org.anitab.mentorship.utils.getTextWithBoldWord
import org.anitab.mentorship.utils.getUnixTimestampInMilliseconds
import org.anitab.mentorship.view.fragments.RequestPagerFragment
import org.anitab.mentorship.viewmodels.RequestDetailViewModel

/**
 * This activity will show a Mentorship request detail from the Requests List
 */
class RequestDetailActivity : BaseActivity() {

    private val requestDetailViewModel by lazy {
        ViewModelProviders.of(this).get(RequestDetailViewModel::class.java)
    }
    private val mentorshipRelationResponse by lazy {
        intent.getParcelableExtra<Relationship>(Constants.RELATIONSHIP_EXTRA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_detail)
        supportActionBar?.title = getString(R.string.request_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        populateView(mentorshipRelationResponse)
        setObservables(mentorshipRelationResponse)
        setOnClickListeners(mentorshipRelationResponse)
    }

    private fun populateView(relationResponse: Relationship) {
        tvRequestNotes.text = relationResponse.notes
        val isFromMentee: Boolean = relationResponse.actionUserId == relationResponse.mentee.id

        val requestDirection = getString(if (relationResponse.sentByMe) R.string.to else R.string.from)

        val otherUserName = if (relationResponse.sentByMe) {
            if (isFromMentee) {
                relationResponse.mentor.name
            } else {
                relationResponse.mentee.name
            }
        } else {
            if (isFromMentee) {
                relationResponse.mentee.name
            } else {
                relationResponse.mentor.name
            }
        }
        tvOtherUserName.text = getString(R.string.request_direction_formatted, requestDirection, otherUserName)

        val summaryStrId = if (relationResponse.sentByMe) {
            R.string.request_sent_by_current_user_message
        } else {
            R.string.request_sent_by_other_user_message
        }
        val actionUserRole = getString(if (isFromMentee) R.string.mentee else R.string.mentor)
        val requestEndDate = convertUnixTimestampIntoStr(
                relationResponse.endDate, EXTENDED_DATE_FORMAT)

        val requestSummaryMessage = getString(summaryStrId,
                otherUserName, actionUserRole, requestEndDate)
        tvRequestSummary.text = requestSummaryMessage

        if (relationResponse.state == RelationState.PENDING.value) {
            setActionButtons(relationResponse)
        } else {
            setStateMessage(relationResponse)
        }

        // TODD: Needed to enable scrolling on text view
        tvRequestNotes.movementMethod = ScrollingMovementMethod()
    }

    private fun setActionButtons(relationResponse: Relationship) {
        val hasEndTimePassed = getUnixTimestampInMilliseconds(relationResponse.endDate) < System.currentTimeMillis()
        if (!hasEndTimePassed) {
            if (relationResponse.sentByMe) {
                btnDelete.visibility = View.VISIBLE
                btnAccept.visibility = View.GONE
                btnReject.visibility = View.GONE
            } else {
                btnDelete.visibility = View.GONE
                btnAccept.visibility = View.VISIBLE
                btnReject.visibility = View.VISIBLE
            }
            tvStateMessage.visibility = View.GONE
        } else {
            tvStateMessage.visibility = View.VISIBLE
            tvStateMessage.text = getString(R.string.relation_end_date_has_passed)
        }
    }

    private fun setStateMessage(relationResponse: Relationship) {
        val stateStrId = when (relationResponse.state) {
            RelationState.ACCEPTED.value -> R.string.accepted
            RelationState.REJECTED.value -> R.string.rejected
            RelationState.CANCELLED.value -> R.string.cancelled
            RelationState.COMPLETED.value -> R.string.completed
            else -> {
                null
            }
        }

        stateStrId?.let {
            tvStateMessage.visibility = View.VISIBLE
            val stateStr = getString(stateStrId)
            val relationStateMessage = getString(R.string.relation_state_message, stateStr)
            tvStateMessage.text = getTextWithBoldWord(relationStateMessage, stateStr)
        }
    }

    private fun setOnClickListeners(relationResponse: Relationship) {

        btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            // set title for alert dialog
            builder.setTitle("Alert")
            // set message for alert dialog
            builder.setMessage("Are you sure you want to delete request?")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            // performing positive action
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                requestDetailViewModel.deleteRequest(relationResponse.id)
            }
            // performing cancel action
            builder.setNeutralButton("Cancel") { dialogInterface, which ->
            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        btnReject.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            // set title for alert dialog
            builder.setTitle("Alert")
            // set message for alert dialog
            builder.setMessage("Are you sure you want to reject request?")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            // performing positive action
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                requestDetailViewModel.rejectRequest(relationResponse.id)
            }
            // performing cancel action
            builder.setNeutralButton("Cancel") { dialogInterface, which ->
            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        btnAccept.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            // set title for alert dialog
            builder.setTitle("Alert")
            // set message for alert dialog
            builder.setMessage("Are you sure you want to accept request?")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            // performing positive action
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                requestDetailViewModel.acceptRequest(relationResponse.id)
            }
            // performing cancel action
            builder.setNeutralButton("Cancel") { dialogInterface, which ->
            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }

    private fun setObservables(relationResponse: Relationship) {
        requestDetailViewModel.successful.observe(this, {
            successful ->
            hideProgressDialog()
            if (successful != null) {
                if (successful) {
                    Toast.makeText(this, requestDetailViewModel.message, Toast.LENGTH_LONG).show()
                    val previousScreen = Intent(applicationContext, RequestPagerFragment::class.java)
                    previousScreen.putExtra(Constants.REQUEST_ID, relationResponse.id.toString())
                    setResult(Constants.DELETE_REQUEST_RESULT_ID, previousScreen)
                    finish()
                } else {
                    Snackbar.make(getRootView(), requestDetailViewModel.message, Snackbar.LENGTH_LONG)
                            .show()
                }
            }
        })
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }
}

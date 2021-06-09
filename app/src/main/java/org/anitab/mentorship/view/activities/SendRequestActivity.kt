package org.anitab.mentorship.view.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.android.synthetic.main.activity_send_request.*
import org.anitab.mentorship.R
import org.anitab.mentorship.models.RelationState
import org.anitab.mentorship.models.Relationship
import org.anitab.mentorship.remote.requests.RelationshipRequest
import org.anitab.mentorship.utils.SEND_REQUEST_END_DATE_FORMAT
import org.anitab.mentorship.utils.convertDateIntoUnixTimestamp
import org.anitab.mentorship.utils.getAuthTokenPayload
import org.anitab.mentorship.utils.getUnixTimestampInMilliseconds
import org.anitab.mentorship.viewmodels.RequestsViewModel
import org.anitab.mentorship.viewmodels.SendRequestViewModel

/**
 * This activity will show a Mentorship request detail from the Requests List
 */
class SendRequestActivity : BaseActivity() {

    private lateinit var myCalendar: Calendar
    companion object {
        const val OTHER_USER_ID_INTENT_EXTRA = "OTHER_USER_ID_INTENT_EXTRA"
        const val OTHER_USER_NAME_INTENT_EXTRA = "OTHER_USER_NAME_INTENT_EXTRA"
    }

    private lateinit var pendingSentRelationships: List<Relationship>
    private val sendRequestViewModel: SendRequestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_request)

        tvRequestEndDate.isEnabled = false
        supportActionBar?.title = getString(R.string.send_request)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val otherUserName = intent.getStringExtra(OTHER_USER_NAME_INTENT_EXTRA)
        val otherUserId = intent.getIntExtra(OTHER_USER_ID_INTENT_EXTRA, 0)
        val currentUserId = getAuthTokenPayload().identity
        setObservables()
        populateView(otherUserName, otherUserId, currentUserId)

        // Setting default date , 1 month after a current date
        myCalendar = Calendar.getInstance()
        myCalendar.add(Calendar.MONTH, 1)
        updateEndDateEditText()

        val date: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateEndDateEditText()
        }
        ivCalendar.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, date,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
            datePickerDialog.show()
        }
    }
    private fun updateEndDateEditText() {
        val sdf = SimpleDateFormat(SEND_REQUEST_END_DATE_FORMAT, Locale.US)
        val editable = SpannableStringBuilder(sdf.format(myCalendar.time))
        tvRequestEndDate.text = editable
    }

    private fun populateView(userName: String, otherUserId: Int, currentUserId: Int) {
        tvOtherUserName.text = userName
        btnSendRequest.setOnClickListener {
            val mentorId: Int
            val menteeId: Int
            val notes = etRequestNotes.text.toString()
            val endDate = convertDateIntoUnixTimestamp(
                    tvRequestEndDate.text.toString(), SEND_REQUEST_END_DATE_FORMAT)

            when {
                rgCurrentUserRole.checkedRadioButtonId == btnMentorRadio.id -> {
                    mentorId = currentUserId
                    menteeId = otherUserId
                }
                rgCurrentUserRole.checkedRadioButtonId == btnMenteeRadio.id -> {
                    menteeId = currentUserId
                    mentorId = otherUserId
                }
                else -> {

                    Snackbar.make(getRootView(), "Please select your role for the mentorship relation", Snackbar.LENGTH_LONG)
                            .show()
                    return@setOnClickListener
                }
            }

            if (!TextUtils.isEmpty(notes.trim())) {

                val sendRequestData = RelationshipRequest(
                        menteeId = menteeId,
                        mentorId = mentorId,
                        notes = notes,
                        endDate = endDate
                )
                if (!isRequestDuplicate(sendRequestData)) {
                    sendRequestViewModel.sendRequest(sendRequestData)
                } else {
                    Snackbar.make(getRootView(), getString(R.string.same_request_already_sent) + tvOtherUserName.text, Snackbar.LENGTH_LONG)
                            .show()
                }
            } else {

                etRequestNotes.error = getString(R.string.notes_empty_error)
            }
        }
    }

    private fun setObservables() {
        sendRequestViewModel.successful.observe(this, {
            successful ->
            hideProgressDialog()
            if (successful != null) {
                if (successful) {
                    Toast.makeText(this, sendRequestViewModel.message, Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Snackbar.make(getRootView(), sendRequestViewModel.message, Snackbar.LENGTH_LONG)
                            .show()
                }
            }
        })
        val requestsViewModel: RequestsViewModel by viewModels()
        requestsViewModel.successful.observe(this, {
            successful ->
            if (successful != null) {
                if (successful) {
                    requestsViewModel.allRequestsList?.let {
                        pendingSentRelationships = it.filter {
                            val isPendingState = RelationState.PENDING.value == it.state
                            val hasEndTimePassed = getUnixTimestampInMilliseconds(it.endDate) < System.currentTimeMillis()

                            isPendingState && !hasEndTimePassed
                        }
                    }
                } else {
                    requestsViewModel.message?.let {
                        Snackbar.make(getRootView(), it, Snackbar.LENGTH_LONG)
                            .show()
                    }
                }
            }
        })
        requestsViewModel.getAllMentorshipRelations()
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

    private fun isRequestDuplicate(newRelationship: RelationshipRequest): Boolean {
        pendingSentRelationships.forEach { relationship: Relationship ->
            if (newRelationship.menteeId == relationship.mentee.id && newRelationship.mentorId == relationship.mentor.id &&
                    newRelationship.endDate.toFloat() == relationship.endDate) {
                return true
            }
        }
        return false
    }
}

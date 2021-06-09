package org.anitab.mentorship.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_requests_item.view.*
import org.anitab.mentorship.MentorshipApplication
import org.anitab.mentorship.R
import org.anitab.mentorship.models.Relationship
import org.anitab.mentorship.utils.DATE_FORMAT
import org.anitab.mentorship.utils.convertUnixTimestampIntoStr
import org.anitab.mentorship.utils.setTextViewStartingWithBoldSpan

/**
 * This class represents the adapter that fills in each view of the Requests recyclerView
 * @param requestsList list of request and relation to show
 * @param openDetailFunction function to be called when an item from Requests list is clicked
 */
class RequestsAdapter(
    private val requestsList: List<Relationship>,
    private val openDetailFunction: (requestDetail: Relationship) -> Unit
) : RecyclerView.Adapter<RequestsAdapter.RequestsViewHolder>() {

    val context = MentorshipApplication.getContext()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestsViewHolder =
            RequestsViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.list_requests_item, parent, false)
            )

    override fun onBindViewHolder(@NonNull holder: RequestsViewHolder, position: Int) {
        val item = requestsList[position]
        val itemView = holder.itemView

        setTextViewStartingWithBoldSpan(itemView.tvMentorName, context.getString(R.string.mentor), item.mentor.name)
        setTextViewStartingWithBoldSpan(itemView.tvMenteeName, context.getString(R.string.mentee), item.mentee.name)
        setTextViewStartingWithBoldSpan(itemView.tvEndDate, context.getString(R.string.end_date), convertUnixTimestampIntoStr(item.endDate, DATE_FORMAT))
        setTextViewStartingWithBoldSpan(itemView.tvNotes, context.getString(R.string.notes), item.notes)
        itemView.tvCreationDate.text = convertUnixTimestampIntoStr(item.creationDate, DATE_FORMAT)

        // switch arrow direction according to the user that sent a mentorship request
        if (!item.sentByMe) {
            itemView.ivActionUserIndicator.rotation = 180f
        }

        itemView.setOnClickListener { openDetailFunction(item) }
    }

    override fun getItemCount(): Int = requestsList.size

    /**
     * This class holds a view for each item of the Requests list
     * @param itemView represents each view of Requests list
     */
    class RequestsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

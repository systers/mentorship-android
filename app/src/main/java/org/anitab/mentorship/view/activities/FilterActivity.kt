package org.anitab.mentorship.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_filter.*
import org.anitab.mentorship.R
import org.anitab.mentorship.utils.Constants.AVAILABLE_TO_MENTOR_KEY
import org.anitab.mentorship.utils.Constants.FILTER_MAP
import org.anitab.mentorship.utils.Constants.INTERESTS_KEY
import org.anitab.mentorship.utils.Constants.LOCATION_KEY
import org.anitab.mentorship.utils.Constants.NEED_MENTORING_KEY
import org.anitab.mentorship.utils.Constants.SKILLS_KEY
import org.anitab.mentorship.utils.Constants.SORT_KEY
import org.anitab.mentorship.view.fragments.MembersFragment

class FilterActivity : BaseActivity() {

    // a backup variable for the view that was selected in sort by fragment
    private var previousSelectionSort: View? = null

    private var needMentoring = false
    private var availableToMentor = false

    private var map: HashMap<String, String>? = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        setTitle(R.string.filter)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)

        map = intent?.extras?.get(FILTER_MAP) as HashMap<String, String>?
        initializeViews()
        initializeClickListeners()
    }

    private fun initializeViews() {
        when (map?.get(SORT_KEY)) {
            MembersFragment.SortValues.REGISTRATION_DATE.name -> {
                cardSortRegistrationDate.setBackgroundResource(R.drawable.background_rounded_primary)
                tvSortRegistrationDate.setTextColor(ResourcesCompat.getColor(
                        resources, R.color.white, null))
                previousSelectionSort = cardSortRegistrationDate
            }
            MembersFragment.SortValues.NAMEAZ.name -> {
                cardSortNameAZ.setBackgroundResource(R.drawable.background_rounded_primary)
                tvSortNameAZ.setTextColor(ResourcesCompat.getColor(
                        resources, R.color.white, null))
                previousSelectionSort = cardSortNameAZ
            }
            MembersFragment.SortValues.NAMEZA.name -> {
                cardSortNameZA.setBackgroundResource(R.drawable.background_rounded_primary)
                tvSortNameZA.setTextColor(ResourcesCompat.getColor(
                        resources, R.color.white, null))
                previousSelectionSort = cardSortNameZA
            }
        }

        if (map?.get(NEED_MENTORING_KEY) == "true") {
            cardFilterNeedMentoring.setBackgroundResource(R.drawable.background_rounded_primary)
            tvFilterNeedMentoring.setTextColor(ResourcesCompat.getColor(
                    resources, R.color.white, null))
            needMentoring = true
        }
        if (map?.get(AVAILABLE_TO_MENTOR_KEY) == "true") {
            cardFilterAvailableToMentor.setBackgroundResource(R.drawable.background_rounded_primary)
            tvFilterAvailableToMentor.setTextColor(ResourcesCompat.getColor(
                    resources, R.color.white, null))
            availableToMentor = true
        }

        etFilterInterests.setText(map?.get(INTERESTS_KEY) ?: "")
        etFilterLocation.setText(map?.get(LOCATION_KEY) ?: "")
        etFilterSkills.setText(map?.get(SKILLS_KEY) ?: "")
    }

    private fun initializeClickListeners() {
        cardSortAge.setOnClickListener {
            Snackbar.make(scrollViewFilter, getString(R.string.not_implemented), Snackbar.LENGTH_SHORT).show()
            // TODO: Add DateOfBirth field to the user model
        }

        cardFilterNeedMentoring.setOnClickListener {
            if (needMentoring) {
                it.setBackgroundResource(R.drawable.background_rounded_white)
                tvFilterNeedMentoring.setTextColor(ResourcesCompat.getColor(
                        resources, R.color.textColorBlack, null))
            } else {
                it.setBackgroundResource(R.drawable.background_rounded_primary)
                tvFilterNeedMentoring.setTextColor(ResourcesCompat.getColor(
                        resources, R.color.white, null))
            }
            needMentoring = !needMentoring
            map?.put(NEED_MENTORING_KEY, needMentoring.toString())
        }
        cardFilterAvailableToMentor.setOnClickListener {
            if (availableToMentor) {
                it.setBackgroundResource(R.drawable.background_rounded_white)
                tvFilterAvailableToMentor.setTextColor(ResourcesCompat.getColor(
                        resources, R.color.textColorBlack, null))
            } else {
                it.setBackgroundResource(R.drawable.background_rounded_primary)
                tvFilterAvailableToMentor.setTextColor(ResourcesCompat.getColor(
                        resources, R.color.white, null))
            }
            availableToMentor = !availableToMentor
            map?.put(AVAILABLE_TO_MENTOR_KEY, availableToMentor.toString())
        }

        btnClearAll.setOnClickListener {
            // sort by fragment
            with(previousSelectionSort) {
                this?.setBackgroundResource(R.drawable.background_rounded_white)
                ((this as CardView?)?.getChildAt(0) as TextView?)
                        ?.setTextColor(ResourcesCompat.getColor(
                                resources, R.color.textColorBlack, null))
            }
            cardSortRegistrationDate.setBackgroundResource(R.drawable.background_rounded_primary)
            tvSortRegistrationDate.setTextColor(ResourcesCompat.getColor(
                    resources, R.color.white, null))
            previousSelectionSort = cardSortRegistrationDate

            // filter by fragment
            // buttons
            cardFilterNeedMentoring.setBackgroundResource(R.drawable.background_rounded_white)
            tvFilterNeedMentoring.setTextColor(ResourcesCompat.getColor(
                    resources, R.color.textColorBlack, null))
            cardFilterAvailableToMentor.setBackgroundResource(R.drawable.background_rounded_white)
            tvFilterAvailableToMentor.setTextColor(ResourcesCompat.getColor(
                    resources, R.color.textColorBlack, null))

            // EditTexts
            etFilterInterests.setText("")
            etFilterLocation.setText("")
            etFilterSkills.setText("")

            map = hashMapOf(SORT_KEY to MembersFragment.SortValues.REGISTRATION_DATE.name)
        }

        btnApplyFilter.setOnClickListener {
            map?.put(SORT_KEY, when (previousSelectionSort?.id) {
                R.id.cardSortNameAZ ->
                    MembersFragment.SortValues.NAMEAZ.name
                R.id.cardSortNameZA ->
                    MembersFragment.SortValues.NAMEZA.name
                R.id.cardSortRegistrationDate ->
                    MembersFragment.SortValues.REGISTRATION_DATE.name
                else ->
                    MembersFragment.SortValues.REGISTRATION_DATE.name
            })

            map?.put(INTERESTS_KEY, etFilterInterests.text.toString())
            map?.put(LOCATION_KEY, etFilterLocation.text.toString())
            map?.put(SKILLS_KEY, etFilterSkills.text.toString())

            finishActivity()
        }
    }

    private fun finishActivity() {
        val resultIntent = Intent()
        resultIntent.putExtra(FILTER_MAP, map)
        setResult(Activity.RESULT_OK, resultIntent)
        onBackPressed()
    }

    fun buttonOnClickSort(view: View) {
        with(previousSelectionSort) {
            this?.setBackgroundResource(R.drawable.background_rounded_white)
            ((this as CardView?)?.getChildAt(0) as TextView?)
                    ?.setTextColor(ResourcesCompat.getColor(
                            resources, R.color.textColorBlack, null))
        }

        view.setBackgroundResource(R.drawable.background_rounded_primary)
        ((view as CardView).getChildAt(0) as TextView)
                .setTextColor(ResourcesCompat.getColor(
                        resources, R.color.white, null))

        previousSelectionSort = view
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_stay, R.anim.anim_slide_to_bottom)
    }
}

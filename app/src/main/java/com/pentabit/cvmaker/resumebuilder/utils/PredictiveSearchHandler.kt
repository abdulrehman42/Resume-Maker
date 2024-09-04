package com.pentabit.cvmaker.resumebuilder.utils

import android.R
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.pentabit.cvmaker.resumebuilder.callbacks.OnLookUpResult
import com.pentabit.cvmaker.resumebuilder.models.api.LookUpResponse
import com.pentabit.cvmaker.resumebuilder.utils.Helper.removeSpaceFromString
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils

class PredictiveSearchHandler(
    private val key: String,
    private val isAlreadyInDB: Boolean,
    private val autoCompleteTextView: AutoCompleteTextView,
    private val viewModel: AddDetailResumeVM,
    private val enableBtn: View? = null
) : TextWatcher {
    private var handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    private lateinit var adapter: ArrayAdapter<String>
    private var lst = ArrayList<String>()
    private val startingText: String = autoCompleteTextView.text.toString().trim()


    init {
        autoCompleteTextView.addTextChangedListener(this)
        autoCompleteTextView.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            val s = autoCompleteTextView.text.toString()
            if (hasFocus) {
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable!!)
                }
                searchRunnable = Runnable {
                    if (s != null && s.length > 1) {
                        enableBtn?.visibility = View.VISIBLE
                        getSuggestions(s.toString())
                    } else {
                        enableBtn?.visibility = View.GONE
                    }
                    // Ensuring the search box retains focus
                    autoCompleteTextView?.requestFocus()
                }

                handler.postDelayed(searchRunnable!!, 500)
            }
        }
    }

    fun getText(): String {
        val text = autoCompleteTextView.text.toString().trim()
        val isNumeric = text.matches(Regex("\\d+"))
        if (isNumeric)
            AppsKitSDKUtils.makeToast("please write correct")
         else
            return if (text.isEmpty()) {
                ""
            } else if ((isAlreadyInDB && text == startingText) || lst.contains(
                    removeSpaceFromString(text)
                )
            ) {
                "1__$text"
            } else {
                "-1__$text"
            }

        return ""
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // do nothing
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
       /* if (searchRunnable != null) {
            handler.removeCallbacks(searchRunnable!!)
        }
        searchRunnable = Runnable {
            if (s != null && s.length > 1) {
                enableBtn?.visibility = View.VISIBLE
                getSuggestions(s.toString())
            } else {
                enableBtn?.visibility = View.GONE
            }
            // Ensuring the search box retains focus
            autoCompleteTextView?.requestFocus()
        }

        handler.postDelayed(searchRunnable!!, 500)*/

    }




    override fun afterTextChanged(s: Editable?) {
        // do nothing
    }

    private fun getSuggestions(text: String) {
        fetchSuggestions(text)
    }

    private fun fetchSuggestions(text: String) {
        viewModel.getLookUp(key, text, 0, 20, object : OnLookUpResult {
            override fun onLookUpResult(list: MutableList<LookUpResponse>) {
                val suggestions: MutableList<String> = ArrayList()
                for (data in list) {
                    suggestions.add(data.text)
                }
                updateSuggestions(suggestions)
            }
        })
    }

    private fun updateSuggestions(suggestions: List<String>) {
        adapter = ArrayAdapter<String>(
            autoCompleteTextView.context,
            R.layout.simple_dropdown_item_1line,
            suggestions
        )
        lst = ArrayList(suggestions)

        autoCompleteTextView.setAdapter(adapter)
        adapter.notifyDataSetChanged()

        // Show dropdown suggestions
        autoCompleteTextView.showDropDown()
    }


}
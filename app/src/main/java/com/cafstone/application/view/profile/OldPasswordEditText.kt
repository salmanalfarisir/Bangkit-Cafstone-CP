package com.cafstone.application.view.profile


import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout

class OldPasswordEditText(context: Context, attrs: AttributeSet) :
    AppCompatEditText(context, attrs) {

    private var errorTextView: TextInputLayout? = null
    private var errorMessage: String? = null

    fun setErrorTextView(text: TextInputLayout) {
        errorTextView = text
    }

    init {
        // Add text change listener to validate input
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s == null) {
                    errorMessage = null
                    errorTextView?.error = errorMessage
                } else if (s.length < 8) {
                    errorMessage = "Password should be at least 8 characters long"
                    errorTextView?.error = errorMessage
                } else {
                    errorMessage = null
                    errorTextView?.error = errorMessage
                }
            }
        })
    }

}
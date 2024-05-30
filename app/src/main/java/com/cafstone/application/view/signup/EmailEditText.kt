package com.cafstone.application.view.signup

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout

class EmailEditText (context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs){
    private var errorTextView: TextInputLayout? = null

    fun setErrorTextView(text: TextInputLayout) {
        errorTextView = text
    }

    init {
        // Add text change listener to validate input
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val a = s.toString()
                validateEmail(a)
            }
        })
    }

    private fun validateEmail(email: String) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorTextView?.error = null
        } else {
            errorTextView?.error = "Its Not Email"
        }
    }
}
package com.cafstone.application.view.signup

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout

class PasswordEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

    private var errorTextView: TextInputLayout? = null
    private var errorMessage: String? = null
    private var passworderrorTextView: TextInputLayout? = null
    private var passwordTextView : PasswordConfirmEditText? = null

    fun setErrorTextView(text: TextInputLayout) {
        errorTextView = text
    }
    fun setPasswordErrorTextView(text: TextInputLayout) {
        passworderrorTextView = text
    }
    fun setPasswordTextView(text: PasswordConfirmEditText) {
        passwordTextView = text
    }

    init {
        // Add text change listener to validate input
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s == null)
                {
                    errorMessage = null
                    errorTextView?.error = errorMessage
                    passwordTextView?.text = null
                    passwordTextView?.isEnabled = false
                    passworderrorTextView?.hint = "Masukkan Password Terlebih Dahulu"
                }
                else if (s.length < 8) {
                    errorMessage = "Password should be at least 8 characters long"
                    errorTextView?.error = errorMessage
                    passwordTextView?.text = null
                    passwordTextView?.isEnabled = false
                    passworderrorTextView?.hint = "Masukkan Password Terlebih Dahulu"
                } else {
                    errorMessage = null
                    errorTextView?.error = errorMessage
                    passwordTextView?.isEnabled = true
                    passworderrorTextView?.hint = null
                }
            }
        })
    }

}
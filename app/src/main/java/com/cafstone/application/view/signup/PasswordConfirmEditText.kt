package com.cafstone.application.view.signup

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout

class PasswordConfirmEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

    private var errorTextView: TextInputLayout? = null
    private var TextView: PasswordConfirmEditText? = null
    private var passwordTextView: PasswordEditText? = null

    fun setErrorTextView(text: TextInputLayout) {
        errorTextView = text
        errorTextView?.hint = "Masukkan Password Terlebih Dahulu"
    }
    fun setTextView(text: PasswordConfirmEditText) {
        TextView = text
        TextView?.isEnabled = false
    }
    fun setpasswordTextView(text: PasswordEditText) {
        passwordTextView = text
    }

    init {
        // Add text change listener to validate input
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty())
                {
                    errorTextView?.error = null
                }
                else if (s.toString().equals(passwordTextView?.text.toString()))
                {
                    errorTextView?.error = null
                }else{
                    val error = "The password and confirmation password do not match."
                    errorTextView?.error = error
                }
            }
        })
    }

}
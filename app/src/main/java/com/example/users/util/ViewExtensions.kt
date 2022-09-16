package com.example.users.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.example.users.R
import com.google.android.material.textfield.TextInputLayout

fun EditText.validateEditText(textInputLayout: TextInputLayout) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (this@validateEditText.text.toString().trim().isNotEmpty()) {
                textInputLayout.error = null
                textInputLayout.isErrorEnabled = false
            } else {
                textInputLayout.error = context.getString(R.string.cant_be_empty)
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }

    })
}

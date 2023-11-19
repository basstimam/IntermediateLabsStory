package com.example.dicodingstoryapp

import android.content.Context

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText

class CustomEditText : AppCompatEditText {

    var isCharacterPasswordValid = false
    var isEmailFormatValid = false
    var isNameValid = false

    /*getString*/
    var passwordValidation: String = context.getString(R.string.passwordTextValidation)
    var emailValidation: String = context.getString(R.string.emailTextValidation)
    var nameValidation: String = context.getString(R.string.nameTextValidation)

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init()
    }

    private fun init() {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //do nothing
            }

            override fun onTextChanged(char: CharSequence?, start: Int, before: Int, count: Int) {
                if (char.toString().isNotEmpty()) {
                    when (inputType) {
                        InputType.TYPE_TEXT_VARIATION_PASSWORD, 129 -> char?.let {
                            handlePasswordValidation(
                                it
                            )
                        }
                        InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, 33 -> char?.let {
                            handleEmailValidation(
                                it
                            )
                        }
                        InputType.TYPE_CLASS_TEXT, 1 -> char?.let { handleNameValidation(it) }
                        else -> Log.d("CustomEditText", "onTextChanged: $inputType")
                    }
                }
            }

            private fun handlePasswordValidation(char: CharSequence) {
                isCharacterPasswordValid = char.toString().length >= 8
                error = if (isCharacterPasswordValid) null else passwordValidation
            }

            private fun handleEmailValidation(char: CharSequence) {
                isEmailFormatValid = android.util.Patterns.EMAIL_ADDRESS.matcher(char).matches()
                error = if (isEmailFormatValid) null else emailValidation
            }

            private fun handleNameValidation(char: CharSequence) {
                isNameValid = char.toString().length >= 3
                error = if (isNameValid) null else nameValidation
            }

            override fun afterTextChanged(char: Editable?) {
                //do nothing
            }
        })
    }
}
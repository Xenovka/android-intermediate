package com.dicoding.storyapp.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.storyapp.R
import com.dicoding.storyapp.isValidEmail
import com.dicoding.storyapp.isValidPassword

class PasswordForm: AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun setError(error: CharSequence?) {
        super.setError(error)
    }

    override fun setError(error: CharSequence?, icon: Drawable?) {
        super.setError(error, icon)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        background = ContextCompat.getDrawable(context, R.drawable.edit_text_border)
    }

    private fun init() {
        val emailHint = context.getString(R.string.email_address)
        val passwordHint = context.getString(R.string.password)
        val inputHint = hint.toString()

        addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val input = s.toString()

                if(inputHint == emailHint) {
                    when {
                        input.isEmpty() -> error = context.getString(R.string.empty_email)
                        !input.isValidEmail() -> error = context.getString(R.string.invalid_email)
                    }
                } else if (inputHint == passwordHint) {
                    when {
                        input.isEmpty() -> error = context.getString(R.string.empty_password)
                        !input.isValidPassword() -> error = context.getString(R.string.invalid_password)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
package com.spreedly.securewidgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout
import com.spreedly.client.models.SpreedlySecureOpaqueString

open class SecureTextField : FrameLayout {
    lateinit var textLayout: TextInputLayout
    lateinit var editText: EditText

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context) : super(context) {}

    public override fun onFinishInflate() {
        super.onFinishInflate()
        init()
    }

    private fun init() {
        var view = findTextInputLayout(this)
        if (view !is TextInputLayout) {
            textLayout = TextInputLayout(this.context)
            this.addView(textLayout)
        } else {
            textLayout = view
        }
        view = findEditText(textLayout)
        if (view is AppCompatEditText) {
            editText = view
        } else {
            editText = AppCompatEditText(textLayout.context)
            setHint()
            textLayout.addView(editText)
        }
    }

    val text: SpreedlySecureOpaqueString?
        get() = SpreedlySecureOpaqueString(editText.text.toString())

    protected fun findTextInputLayout(v: View?): View? {
        if (v is TextInputLayout) {
            return v
        }
        var result: View? = null
        if (v is ViewGroup) {
            val vg = v
            for (i in 0 until vg.childCount) {
                val child = vg.getChildAt(i)
                result = findTextInputLayout(child)
                if (result is TextInputLayout) {
                    return result
                }
            }
        }
        return result
    }

    protected fun findEditText(v: View?): View? {
        if (v is EditText) {
            return v
        }
        var result: View? = null
        if (v is ViewGroup) {
            val vg = v
            for (i in 0 until vg.childCount) {
                val child = vg.getChildAt(i)
                result = findEditText(child)
                if (result is EditText) {
                    return result
                }
            }
        }
        return result
    }

    private fun setHint() {
        textLayout.hint = when (id) {
            R.id.spreedly_credit_card_number -> {
                context.getString(R.string.hint_credit_card_number)
            }
            R.id.spreedly_cvv -> {
                context.getString(R.string.hint_cvv)
            }
            R.id.spreedly_ba_account_number -> {
                context.getString(R.string.hint_account_number)
            }
            else -> ""
        }
    }

    open fun setError(error: String?) {
        textLayout.error = error
    }
}

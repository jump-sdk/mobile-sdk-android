package com.spreedly.securewidgets

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import com.google.android.material.textfield.TextInputLayout
import com.spreedly.client.models.SpreedlySecureOpaqueString
import com.spreedly.client.models.enums.CardBrand

class SecureCreditCardField : SecureTextField {
    var visible = true
    private var clickListener: OnClickListener? = null
    var cardBrandHelper: CardBrandHelper? = null
    var previous = ""
    private var ccTransformationMethod: CreditCardTransformationMethod

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        ccTransformationMethod = CreditCardTransformationMethod()
    }

    constructor(context: Context) : super(context) {
        ccTransformationMethod = CreditCardTransformationMethod()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        cardBrandHelper = CardBrandHelper()
        clickListener = OnClickListener { _: View? ->
            Log.i("Spreedly", "button clicked")
            val s = editText.selectionEnd
            if (visible) {
                textLayout.setEndIconDrawable(R.drawable.ic_visible)
                editText.transformationMethod = ccTransformationMethod
                visible = false
            } else {
                textLayout.setEndIconDrawable(R.drawable.ic_visibilityoff)
                editText.transformationMethod = null
                visible = true
            }
            editText.setSelection(s)
        }
        textLayout.hint = context.getString(R.string.hint_credit_card_number)
        editText.inputType = InputType.TYPE_CLASS_PHONE
        setEndIcons()
        setStartIcon()
    }

    override fun setError(error: String?) {
        textLayout.error = error
    }

    fun setEndIcons() {
        val s = editText.selectionEnd
        textLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
        if (visible) {
            textLayout.setEndIconDrawable(R.drawable.ic_visibilityoff)
            editText.transformationMethod = null
        } else {
            editText.transformationMethod = ccTransformationMethod
            textLayout.setEndIconDrawable(R.drawable.ic_visible)
        }
        textLayout.setEndIconOnClickListener(clickListener)
        editText.setSelection(s)
    }

    private fun setStartIcon() {
        textLayout.setStartIconDrawable(R.drawable.ic_spr_mono_generic)
        textLayout.setStartIconTintList(null)
        editText.addTextChangedListener(object : TextWatcher {
            var lock = false
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                var text = editText.text.toString()
                text = text.replace(" ", "")
                val brand: CardBrand
                val secureString = SpreedlySecureOpaqueString(text)
                if (text == previous) {
                    return
                }
                previous = text
                var mono = false
                if (text.length < 16) {
                    brand = secureString.softDetect()
                    mono = true
                } else if (text.length < 20) {
                    brand = secureString.detectCardType()
                } else {
                    brand = CardBrand.error
                }
                if (brand === CardBrand.error) {
                    textLayout.error = context.getString(R.string.error_bad_card_number)
                } else {
                    textLayout.error = null
                }
                if (text.length in 16..19 && brand !== CardBrand.error) {
                    visible = false
                    setEndIcons()
                } else {
                    visible = true
                    setEndIcons()
                }
                if (mono) {
                    textLayout.setStartIconDrawable(cardBrandHelper!!.getMonoIcon(brand))
                } else {
                    textLayout.setStartIconDrawable(cardBrandHelper!!.getIcon(brand))
                }
                if (lock) {
                    return
                }
                lock = true
                var newText = ""
                for (i in text.indices) {
                    if (i != 0 && i % 4 == 0 && i < 16) {
                        newText += " "
                    }
                    newText += text[i]
                }
                s.replace(0, s.length, newText)
                lock = false
            }
        })
    }
}

package com.spreedly.securewidgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import com.google.android.material.textfield.TextInputLayout
import com.spreedly.client.SpreedlyClient
import com.spreedly.client.SpreedlyClient.Companion.newInstance
import com.spreedly.client.models.Address
import com.spreedly.client.models.BankAccountInfo
import com.spreedly.client.models.CreditCardInfo
import com.spreedly.client.models.enums.AccountHolderType
import com.spreedly.client.models.enums.AccountType
import com.spreedly.client.models.enums.CardBrand
import com.spreedly.client.models.results.BankAccountResult
import com.spreedly.client.models.results.CreditCardResult
import com.spreedly.client.models.results.SpreedlyError
import com.spreedly.client.models.results.TransactionResult

class SecureFormLayout : LinearLayout {
    var spreedlyClient: SpreedlyClient? = null
    var accountTypeHelper: AccountTypeHelper?
    var fullNameInput: TextInputLayout? = null
    var firstNameInput: TextInputLayout? = null
    var lastNameInput: TextInputLayout? = null
    var company: TextInputLayout? = null
    var creditCardNumberField: SecureCreditCardField? = null
    var cvvField: SecureTextField? = null
    var expirationField: SecureExpirationDate? = null
    var monthInput: TextInputLayout? = null
    var yearInput: TextInputLayout? = null
    var address1Input: TextInputLayout? = null
    var address2Input: TextInputLayout? = null
    var cityInput: TextInputLayout? = null
    var stateInput: TextInputLayout? = null
    var phoneInput: TextInputLayout? = null
    var zipInput: TextInputLayout? = null
    var shippingAddress1Input: TextInputLayout? = null
    var shippingAddress2Input: TextInputLayout? = null
    var shippingCityInput: TextInputLayout? = null
    var shippingStateInput: TextInputLayout? = null
    var shippingCountrySpinner: Spinner? = null
    var shippingPhoneInput: TextInputLayout? = null
    var shippingZipInput: TextInputLayout? = null
    var emailInput: TextInputLayout? = null
    var bankAccountNumberField: SecureTextField? = null
    var routingNumberInput: TextInputLayout? = null
    var accountTypeInput: TextInputLayout? = null
    var accountTypeSpinner: Spinner? = null
    var accountTypeRadio: RadioGroup? = null
    var accountHolderTypeInput: TextInputLayout? = null
    var accountHolderTypeSpinner: Spinner? = null
    var accountHolderTypeRadio: RadioGroup? = null
    var errorView: TextView? = null
    var sameAddress: CheckBox? = null

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        accountTypeHelper = AccountTypeHelper(context)
    }

    constructor(context: Context) : super(context) {
        accountTypeHelper = AccountTypeHelper(context)
    }

    fun setSpreedlyClient(envKey: String, test: Boolean) {
        spreedlyClient = newInstance(envKey, test)
    }

    fun setSpreedlyClient(envKey: String, envSecret: String?, test: Boolean) {
        spreedlyClient = newInstance(envKey, envSecret!!, test)
    }

    public override fun onFinishInflate() {
        super.onFinishInflate()
        init()
    }

    suspend fun createCreditCardPaymentMethod(): TransactionResult<CreditCardResult>? {
        Log.i("Spreedly", "createCreditCardPaymentMethod firing")
        resetCardErrors()
        resetGenericErrors()
        var hasError = validateNames()
        hasError = validateAddress(hasError)
        if (creditCardNumberField == null || creditCardNumberField!!.text!!.detectCardType() === CardBrand.error) {
            creditCardNumberField!!.setError(context.getString(R.string.error_bad_card_number))
            hasError = true
        }
        if (expirationField != null && (expirationField!!.getYear() == 0 || expirationField!!.getMonth() == 0)) {
            expirationField!!.setError(context.getString(R.string.error_bad_date))
            hasError = true
        }
        if (hasError) {
            return null
        }
        val info = createCreditCardInfo()
        val transaction: TransactionResult<CreditCardResult> =
            spreedlyClient!!.createCreditCardPaymentMethod(info)
        return if (!transaction.succeeded) {
            handleErrors(transaction.errors ?: listOf())
            null
        } else {
            transaction
        }
    }

    suspend fun createBankAccountPaymentMethod(): TransactionResult<BankAccountResult>? {
        Log.i("Spreedly", "createCreditCardPaymentMethod firing")
        resetBankErrors()
        resetGenericErrors()
        var hasError = validateNames()
        hasError = validateAddress(hasError)
        if (bankAccountNumberField != null) {
            if (bankAccountNumberField!!.text!!.length === 0) {
                hasError = true
                bankAccountNumberField!!.setError(
                    context.getString(R.string.error_blank_account_number),
                )
            } else if (!bankAccountNumberField!!.text!!.isNumber) {
                hasError = true
                bankAccountNumberField!!.setError(
                    context.getString(R.string.error_bad_account_number),
                )
            }
        }
        if (routingNumberInput != null) {
            if (getString(routingNumberInput!!).length == 0) {
                hasError = true
                routingNumberInput!!.error = context.getString(R.string.error_blank_routing_number)
            } else {
                try {
                    getString(routingNumberInput!!).toDouble()
                } catch (e: Exception) {
                    hasError = true
                    routingNumberInput!!.error =
                        context.getString(R.string.error_bad_routing_number)
                }
            }
        }
        if (hasError) {
            return null
        }
        val info = createBankAccountInfo()
        val transaction: TransactionResult<BankAccountResult> =
            spreedlyClient!!.createBankPaymentMethod(info)
        return if (!transaction.succeeded) {
            handleErrors(transaction.errors ?: listOf())
            null
        } else {
            transaction
        }
    }

    fun handleErrors(errors: List<SpreedlyError>) {
        try {
            for (i in errors.indices) {
                val error = errors[i]
                val widgetError = WidgetError.valueOf(error.attribute!!.uppercase())
                if (widgetError != null) {
                    val resourceId = widgetError.resourceId
                    val view = findViewById<View>(resourceId)
                    val viewClass: Class<*> = view.javaClass
                    if (viewClass == TextInputLayout::class.java) {
                        val textInputLayout = view as TextInputLayout
                        textInputLayout.error = error.message
                    } else if (viewClass == SecureTextField::class.java) {
                        val secureTextField = view as SecureTextField
                        secureTextField.setError(error.message)
                    } else if (viewClass == SecureCreditCardField::class.java) {
                        val secureCreditCardField = view as SecureCreditCardField
                        secureCreditCardField.setError(error.message)
                    } else if (viewClass == SecureExpirationDate::class.java) {
                        val secureExpirationDate = view as SecureExpirationDate
                        secureExpirationDate.setError(error.message)
                    } else if (errorView != null) {
                        errorView!!.text = error.message
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("Spreedly", e.message!!)
            throw e
        }
    }

    private fun nameTold(name: String): Int {
        val widgetError = WidgetError.valueOf(name.uppercase())
        return widgetError?.resourceId ?: 0
    }

    private fun getNumber(textInputLayout: TextInputLayout): Int {
        return try {
            textInputLayout.editText!!.text.toString().toInt()
        } catch (e: NullPointerException) {
            0
        } catch (e: NumberFormatException) {
            0
        }
    }

    private fun getString(textInputLayout: TextInputLayout): String {
        return try {
            textInputLayout.editText!!.text.toString()
        } catch (e: NullPointerException) {
            ""
        }
    }

    private fun getBoolean(checkBox: CheckBox?): Boolean {
        return try {
            checkBox!!.isChecked
        } catch (e: NullPointerException) {
            false
        }
    }

    private fun resetCardErrors() {
        if (creditCardNumberField != null) creditCardNumberField!!.setError(null)
        if (cvvField != null) cvvField!!.setError(null)
        if (expirationField != null) expirationField!!.setError(null)
        if (monthInput != null) monthInput!!.error = null
        if (yearInput != null) yearInput!!.error = null
    }

    private fun resetBankErrors() {
        if (bankAccountNumberField != null) bankAccountNumberField!!.setError(null)
        if (routingNumberInput != null) routingNumberInput!!.error = null
    }

    private fun resetGenericErrors() {
        if (fullNameInput != null) fullNameInput!!.error = null
        if (firstNameInput != null) firstNameInput!!.error = null
        if (lastNameInput != null) lastNameInput!!.error = null
        if (address1Input != null) address1Input!!.error = null
        if (address2Input != null) address2Input!!.error = null
        if (cityInput != null) cityInput!!.error = null
        if (stateInput != null) stateInput!!.error = null
        if (zipInput != null) zipInput!!.error = null
        if (phoneInput != null) phoneInput!!.error = null
        if (shippingAddress1Input != null) shippingAddress1Input!!.error = null
        if (shippingAddress2Input != null) shippingAddress2Input!!.error = null
        if (shippingCityInput != null) shippingCityInput!!.error = null
        if (shippingStateInput != null) shippingStateInput!!.error = null
        if (shippingZipInput != null) shippingZipInput!!.error = null
        if (shippingPhoneInput != null) shippingPhoneInput!!.error = null
    }

    private fun validateNames(): Boolean {
        var hasError = false
        if (fullNameInput != null && getString(fullNameInput!!).length == 0) {
            fullNameInput!!.error = context.getString(R.string.error_blank_first_name)
            hasError = true
        }
        if (firstNameInput != null && getString(firstNameInput!!).length == 0) {
            firstNameInput!!.error = context.getString(R.string.error_blank_first_name)
            hasError = true
        }
        if (lastNameInput != null && getString(lastNameInput!!).length == 0) {
            lastNameInput!!.error = context.getString(R.string.error_blank_last_name)
            hasError = true
        }
        return hasError
    }

    private fun validateAddress(hasError: Boolean): Boolean {
        var hasError = hasError
        if (address1Input != null && getString(address1Input!!).length == 0) {
            hasError = true
            address1Input!!.error = context.getString(R.string.error_blank_address)
        }
        if (cityInput != null && getString(cityInput!!).length == 0) {
            hasError = true
            cityInput!!.error = context.getString(R.string.error_blank_city)
        }
        if (stateInput != null && getString(stateInput!!).length == 0) {
            hasError = true
            stateInput!!.error = context.getString(R.string.error_blank_state)
        }
        if (zipInput != null && getString(zipInput!!).length == 0) {
            hasError = true
            zipInput!!.error = context.getString(R.string.error_blank_zipcode)
        }
        if (shippingAddress1Input != null && getString(shippingAddress1Input!!).length == 0) {
            hasError = true
            shippingAddress1Input!!.error = context.getString(R.string.error_blank_address)
        }
        if (shippingCityInput != null && getString(shippingCityInput!!).length == 0) {
            hasError = true
            shippingCityInput!!.error = context.getString(R.string.error_blank_city)
        }
        if (shippingStateInput != null && getString(shippingStateInput!!).length == 0) {
            hasError = true
            shippingStateInput!!.error = context.getString(R.string.error_blank_state)
        }
        if (shippingZipInput != null && getString(shippingZipInput!!).length == 0) {
            hasError = true
            shippingZipInput!!.error = context.getString(R.string.error_blank_zipcode)
        }
        return hasError
    }

    private fun init() {
        creditCardNumberField = findViewById(R.id.spreedly_credit_card_number)
        cvvField = findViewById(R.id.spreedly_cvv)
        fullNameInput = findViewById(R.id.spreedly_full_name)
        expirationField = findViewById(R.id.spreedly_cc_expiration_date)
        firstNameInput = findViewById(R.id.spreedly_first_name)
        lastNameInput = findViewById(R.id.spreedly_last_name)
        company = findViewById(R.id.spreedly_company)
        monthInput = findViewById(R.id.spreedly_cc_month)
        yearInput = findViewById(R.id.spreedly_cc_year)
        address1Input = findViewById(R.id.spreedly_address1)
        address2Input = findViewById(R.id.spreedly_address2)
        cityInput = findViewById(R.id.spreedly_city)
        stateInput = findViewById(R.id.spreedly_state)
        zipInput = findViewById(R.id.spreedly_zip)
        phoneInput = findViewById(R.id.spreedly_phone_number)
        shippingAddress1Input = findViewById(R.id.spreedly_shipping_address1)
        shippingAddress2Input = findViewById(R.id.spreedly_shipping_address2)
        shippingCityInput = findViewById(R.id.spreedly_shipping_city)
        shippingStateInput = findViewById(R.id.spreedly_shipping_state)
        shippingZipInput = findViewById(R.id.spreedly_shipping_zip)
        shippingCountrySpinner = findViewById(R.id.spreedly_shipping_country)
        shippingPhoneInput = findViewById(R.id.spreedly_shipping_phone_number)
        emailInput = findViewById(R.id.spreedly_email)
        bankAccountNumberField = findViewById(R.id.spreedly_ba_account_number)
        routingNumberInput = findViewById(R.id.spreedly_ba_routing_number)
        val accountTypeView = findViewById<View>(R.id.spreedly_ba_account_type)
        if (accountTypeView != null) {
            if (accountTypeView.javaClass == TextInputLayout::class.java) {
                accountTypeInput = accountTypeView as TextInputLayout
            } else if (accountTypeView.javaClass == Spinner::class.java || accountTypeView.javaClass == AppCompatSpinner::class.java) {
                accountTypeSpinner = accountTypeView as Spinner
            } else {
                accountTypeRadio = accountTypeView as RadioGroup
            }
        }
        val accountHolderTypeView = findViewById<View>(R.id.spreedly_ba_account_holder_type)
        if (accountHolderTypeView != null) {
            if (accountHolderTypeView.javaClass == TextInputLayout::class.java) {
                accountHolderTypeInput = accountHolderTypeView as TextInputLayout
            } else if (accountHolderTypeView.javaClass == Spinner::class.java) {
                accountHolderTypeSpinner = accountHolderTypeView as Spinner
            } else {
                accountHolderTypeRadio = accountHolderTypeView as RadioGroup
            }
        }
        errorView = findViewById(R.id.spreedly_generic_error)
    }

    private fun createCreditCardInfo(): CreditCardInfo {
        val address = getAddress()
        val shippingAddress = getShippingAddress()
        val name = getName()
        val number = creditCardNumberField?.let { it.text }
        val cvv = cvvField?.let { it.text }
        val month = expirationField?.let { it.getMonth() }
        val year = expirationField?.let { it.getYear() }

        return CreditCardInfo(
            firstName = name.second.first,
            lastName = name.second.second,
            fullName = name.first,
            number = number!!,
            verificationValue = cvv!!,
            month = month!!,
            year = year!!,
            address = address,
            shippingAddress = shippingAddress,
        )
    }

    private fun createBankAccountInfo(): BankAccountInfo {
        val address = getAddress()
        val shippingAddress = getShippingAddress()
        val name = getName()
        val bankAccount = bankAccountNumberField!!.text!!
        val routingNumber = getString(routingNumberInput!!)

        val accountType = if (accountTypeSpinner != null) {
            AccountType.valueOf(accountTypeSpinner!!.selectedItem.toString())
        } else if (accountTypeRadio != null) {
            accountTypeHelper!!.getAccountType(
                (
                    findViewById<View>(
                        accountTypeRadio!!.checkedRadioButtonId,
                    ) as RadioButton
                    ).text.toString(),
            )
        } else if (accountTypeInput != null) {
            AccountType.valueOf(getString(accountTypeInput!!))
        } else {
            null
        }
        val accountHolderType = if (accountHolderTypeSpinner != null) {
            AccountHolderType.valueOf(accountHolderTypeSpinner!!.selectedItem.toString())
        } else if (accountHolderTypeRadio != null) {
            accountTypeHelper!!.getAccountHolderType(
                (
                    findViewById<View>(
                        accountHolderTypeRadio!!.checkedRadioButtonId,
                    ) as RadioButton
                    ).text.toString(),
            )
        } else if (accountHolderTypeInput != null) {
            AccountHolderType.valueOf(getString(accountHolderTypeInput!!))
        } else {
            null
        }
        return BankAccountInfo(
            firstName = name.second.first,
            lastName = name.second.second,
            fullName = name.first,
            routingNumber = routingNumber,
            accountNumber = bankAccount,
            accountType = accountType,
            accountHolderType = accountHolderType,
            address = address,
            shippingAddress = shippingAddress,
        )
    }

    private fun getAddress(): Address {
        val address1 = getString(address1Input!!)
        val address2 = getString(address2Input!!)
        val city = getString(cityInput!!)
        val state = getString(stateInput!!)
        val zip = getString(zipInput!!)
        return Address(
            address1 = address1,
            address2 = address2,
            city = city,
            state = state,
            zip = zip,
        )
    }

    private fun getShippingAddress(): Address {
        return if (getBoolean(sameAddress)) {
            getAddress()
        } else {
            val address1 = getString(shippingAddress1Input!!)
            val address2 = getString(shippingAddress2Input!!)
            val city = getString(shippingCityInput!!)
            val state = getString(shippingStateInput!!)
            val zip = getString(shippingZipInput!!)
            return Address(
                address1 = address1,
                address2 = address2,
                city = city,
                state = state,
                zip = zip,
            )
        }
    }

    private fun getName(): Pair<String, Pair<String, String>> {
        return getString(fullNameInput!!) to Pair(
            getString(firstNameInput!!),
            getString(lastNameInput!!),
        )
    }
}

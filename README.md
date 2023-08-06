[![Android CI](https://github.com/jump-sdk/mobile-sdk-android/actions/workflows/android.yml/badge.svg)](https://github.com/jump-sdk/mobile-sdk-android/actions/workflows/android.yml)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=jump-sdk_mobile-sdk-android&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=jump-sdk_mobile-sdk-android)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=jump-sdk_mobile-sdk-android&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=jump-sdk_mobile-sdk-android)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=jump-sdk_mobile-sdk-android&metric=bugs)](https://sonarcloud.io/summary/new_code?id=jump-sdk_mobile-sdk-android)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=jump-sdk_mobile-sdk-android&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=jump-sdk_mobile-sdk-android)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=jump-sdk_mobile-sdk-android&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=jump-sdk_mobile-sdk-android)


# Integration
All integration options require a Spreedly account and an environment key. See [Create Your API Credentials](https://docs.spreedly.com/basics/credentials/#environment-key) for details.

## Installation
This library is not publicly published and must be built and delivered via your own maven repository or local setup.

## Customization with `SecureCreditCardForm`
See the [documentation on the Composable function](https://github.com/jump-sdk/mobile-sdk-android/blob/master/securewidgets-compose/src/main/kotlin/com/spreedly/composewidgets/SecureCreditCardForm.kt#L37) for details on how to customize the form.
See also the [sample usage](https://github.com/jump-sdk/mobile-sdk-android/blob/master/sdk_sample/src/main/java/com/spreedly/sdk_sample/widget/ComposeWidgetsFragment.kt#L65) in the demo app

### SecureForm Components
The `SecureForm` finds views based on expected ids.
 - A `TextInputLayout` with the id `spreedly_full_name` or two `TextInputLayout`s with ids `spreedly_first_name` and `spreedly_last_name`
 - For Credit Cards `SecureForm` expects:
 -- `SecureCreditCardNumber` with the id `spreedly_credit_card`.
 -- `SecureExpirationDate` with the id `spreedly_cc_expiration_date`.
 -- `SecureTextField` with the id `spreedly_cvv`.
 - For Bank Accounts `SecureForm` expects:
 -- `TextInputLayout` with the id `spreedly_ba_routing_number`
 -- `SecureTextField` with id `spreedly_ba_account_number`
 -- Either a `RadioGroup`, `Spinner` or `TextInputLayout` with the id `spreedly_ba_account_type`
 - To include shipping or billing addresses, use `TextInputLayout` with the following ids:
 -- For billing addresses:  `spreedly_address1`, `spreedly_address2`, `spreedly_city`, `spreedly_state`, `spreedly_zip`, `spreedly_country`(optional), `spreedly_phone_number`(optional)
 -- For shipping addresses: `spreedly_shipping_address1`, `spreedly_shipping_address2`, `spreedly_shipping_city`, `spreedly_shipping_state`, `spreedly_shipping_zip`, `spreedly_shipping_country` (optional), `spreedly_shipping_phone_number` (optional)

Example Credit Card Payment XML:
``` xml
<com.spreedly.securewidgets.SecureFormLayout xmlns:android="http://schemas.android.com/apk/res/android"
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  android:layout_marginStart="@dimen/activity_horizontal_margin"
  android:layout_marginEnd="@dimen/activity_horizontal_margin"
  android:padding="16dp"
  android:gravity="center"
  android:id="@+id/credit_card_form"
  android:orientation="vertical">

    <com.google.android.material.textfield.TextInputLayout
	  android:id="@id/spreedly_full_name"
	  android:layout_width="match_parent"
	  android:layout_height="wrap_content">

        <com.google.android.material.textfield.TextInputEditText
		  android:layout_width="match_parent"
		  android:layout_height="wrap_content"
		  android:autofillHints="name"
		  android:hint="@string/name_hint"
		  android:inputType="text" />
    </com.google.android.material.textfield.TextInputLayout>

    <com.spreedly.securewidgets.SecureCreditCardField
	    android:id="@id/spreedly_credit_card_number"
		android:layout_width="match_parent"
		android:layout_height="wrap_content" />

    <com.spreedly.securewidgets.SecureTextField
		android:id="@id/spreedly_cvv"
		android:layout_width="match_parent"
	    android:layout_height="wrap_content" />

    <com.spreedly.securewidgets.SecureExpirationDate
		android:id="@id/spreedly_cc_expiration_date"
	    android:layout_width="match_parent"
	    android:layout_height="wrap_content" />

    <TextView
	    android:id="@+id/token"
	    android:layout_width="match_parent"
	    android:layout_height="wrap_content"
	    android:textColor="@android:color/holo_blue_dark"
	    android:textIsSelectable="true" />

    <TextView
	    android:id="@+id/error"
	    android:layout_width="match_parent"
	    android:layout_height="wrap_content"
	    android:textColor="@android:color/holo_red_dark"
	    android:textIsSelectable="true" />

    <Button
	    android:id="@id/spreedly_cc_submit"
	    android:layout_width="match_parent"
	    android:layout_height="wrap_content"
	    android:text="@string/submit" />

</com.spreedly.securewidgets.SecureFormLayout>
```

### SecureForm Methods

The `SecureForm` has the following methods:
 - `public Single<TransactionResult<PaymentMethodResult>> createCreditCardPaymentMethod()`: Captures data from user input (name, credit card number, cvv, expiration date and optionally billing and shipping address) and makes a `create credit card payment request` to the Spreedly API.
 - `Single<TransactionResult<PaymentMethodResult>> createBankAccountPaymentMethod()`: Captures data from user input (name, bank account number, routing number, account type and optionally billing and shipping address), and makes a `create bank account payment request` to the Spreedly API.
 - `public Single<TransactionResult<PaymentMethodResult>> createCreditCardPaymentMethod(@Nullable Address billingAddress, @Nullable Address shippingAddress)`: Behaves the same as `createCreditCardPaymentMethod()`except it doesn't not search for billing or shipping address components. Use this if addresses have been submitted on a different view.
 - `Single<TransactionResult<PaymentMethodResult>> createBankAccountPaymentMethod(@Nullable Address billingAddress, @Nullable Address shippingAddress)`: Behaves the same as `createBankAccountPaymentMethod()`except it doesn't not search for billing or shipping address components. Use this if addresses have been submitted on a different view.

## Full Customization with Core SDK
If you prefer a completely customized payment method collection and selection experience, you can use the Core SDK and `SpreedlyClient`
For example, to create a new credit card payment method with Spreedly, create and configure a `CreditCardInfo` object, and send is using `SpreedlyClient`

Constructors:

```
CreditCardInfo(@NonNull String: fullName, @NonNull SpreedlySecureOpaqueString: number, @Nullable SpreedlySecureOpaqueString: verificationValue, @NonNull int: year, @NonNull int: month);
```

```
SpreedlyClient.newInstance(@NonNull String envKey, @NonNull String envSecret,@NonNull boolean test)
```

Example code:

```jvm
CreditCardInfo info = new CreditCardInfo("Full Name", new SpreedlySecureOpaqueString("4111111111111111"), new SpreedlySecureOpaqueString("432"), 2025, 12);
SpreedlyClient client = SpreedlyClient.newInstance("your key", "your secret", true);
client.createCreditCardPaymentMethod(info, null, null).subscribe(new SingleObserver<TransactionResult<PaymentMethodResult>>() {
    @Override
  public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
  public void onSuccess(TransactionResult<PaymentMethodResult> trans) {
        try {
            if (trans.succeeded) {
                Log.i("Spreedly", "trans.result.token: " + trans.result.token);
                //do something with trans result
            } else {
                Log.e("Spreedly", "trans.message: " + trans.message);
               // do something with error
            }
        } finally {
          //finish
        }
    }

    @Override
  public void onError(@NonNull Throwable e) {
        Log.e("Spreedly", e.getMessage(), e);
        //do something with error
    }
});
```

# Lint

Run

    ./gradlew lint


# Coverage

A coverage report is regularly posted [here](https://ergonlabs.github.io/spreedly-docs/coverage/java/core-sdk/index.html).

To see this locally run:

    ./gradlew test

Then open [build/core-sdk/reports/jacoco/test/html/index.html](build/core-sdk/reports/jacoco/test/html/index.html)

# Docs

To build docs use:

    rm -rf docs
    ./gradlew alljavadoc












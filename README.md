[![Publish Core and Compose Packages](https://github.com/jump-sdk/mobile-sdk-android/actions/workflows/publish.yml/badge.svg)](https://github.com/jump-sdk/mobile-sdk-android/actions/workflows/publish.yml)
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
See the [documentation](https://jump-sdk.github.io/mobile-sdk-android/securewidgets-compose/com.spreedly.composewidgets/-secure-credit-card-form.html) for details on how to customize the form.

See also the [sample usage](https://github.com/jump-sdk/mobile-sdk-android/blob/master/sdk_sample/src/main/java/com/spreedly/sdk_sample/widget/ComposeWidgetsFragment.kt#L65) in the demo app

### SecureForm Components
See the [documentation on the Composable functions](https://github.com/jump-sdk/mobile-sdk-android/tree/master/securewidgets-compose/src/main/kotlin/com/spreedly/composewidgets)

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

# Checks / pre-commit

```
./gradlew :core-kmm:detektAndroidRelease :core-kmm:test :securewidgets-compose:detekt :securewidgets-compose:lintRelease
```

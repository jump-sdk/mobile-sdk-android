package com.spreedly.client.models.enums

enum class CardBrand(val range: List<IntRange>?, val bins: List<String>?) {
    visa(null, null),
    mastercard(
        listOf(IntRange(222100, 272099), IntRange(510000, 559999)),
        null,
    ),
    americanExpress(null, null),
    alelo(
        listOf(
            IntRange(402588, 402588),
            IntRange(404347, 404347),
            IntRange(405876, 405876),
            IntRange(405882, 405882),
            IntRange(405884, 405884),
            IntRange(405886, 405886),
            IntRange(430471, 430471),
            IntRange(438061, 438061),
            IntRange(438064, 438064),
            IntRange(470063, 470066),
            IntRange(496067, 496067),
            IntRange(506699, 506704),
            IntRange(506706, 506706),
            IntRange(506713, 506714),
            IntRange(506716, 506716),
            IntRange(506749, 506750),
            IntRange(506752, 506752),
            IntRange(506754, 506756),
            IntRange(506758, 506762),
            IntRange(506764, 506767),
            IntRange(506770, 506771),
            IntRange(509015, 509019),
            IntRange(509880, 509882),
            IntRange(509884, 509885),
            IntRange(509987, 509992),
        ),
        null,
    ),
    cabal(
        listOf(
            IntRange(60420100, 60440099),
            IntRange(58965700, 58965799),
            IntRange(60352200, 60352299),
        ),
        null,
    ),
    carnet(
        listOf(
            IntRange(506199, 506499),
        ),
        listOf(
            "286900",
            "502275",
            "606333",
            "627535",
            "636318",
            "636379",
            "639388",
            "639484",
            "639559",
            "50633601",
            "50633606",
            "58877274",
            "62753500",
            "60462203",
            "60462204",
            "588772",
        ),
    ),
    dankort(null, null),
    dinersClub(null, null),
    discover(null, null),
    elo(
        listOf(
            IntRange(506707, 506708),
            IntRange(506715, 506715),
            IntRange(506718, 506722),
            IntRange(506724, 506724),
            IntRange(506726, 506736),
            IntRange(506739, 506739),
            IntRange(506741, 506743),
            IntRange(506745, 506747),
            IntRange(506753, 506753),
            IntRange(506774, 506776),
            IntRange(506778, 506778),
            IntRange(509000, 509001),
            IntRange(509003, 509003),
            IntRange(509007, 509007),
            IntRange(509020, 509022),
            IntRange(509035, 509035),
            IntRange(509039, 509042),
            IntRange(509045, 509045),
            IntRange(509048, 509048),
            IntRange(509051, 509071),
            IntRange(509073, 509074),
            IntRange(509077, 509080),
            IntRange(509084, 509084),
            IntRange(509091, 509094),
            IntRange(509098, 509098),
            IntRange(509100, 509100),
            IntRange(509104, 509104),
            IntRange(509106, 509109),
            IntRange(627780, 627780),
            IntRange(636368, 636368),
            IntRange(650031, 650033),
            IntRange(650035, 650045),
            IntRange(650047, 650047),
            IntRange(650406, 650410),
            IntRange(650434, 650436),
            IntRange(650439, 650439),
            IntRange(650485, 650504),
            IntRange(650506, 650530),
            IntRange(650577, 650580),
            IntRange(650582, 650591),
            IntRange(650721, 650727),
            IntRange(650901, 650922),
            IntRange(650928, 650928),
            IntRange(650938, 650939),
            IntRange(650946, 650948),
            IntRange(650954, 650955),
            IntRange(650962, 650963),
            IntRange(650967, 650967),
            IntRange(650971, 650971),
            IntRange(651652, 651667),
            IntRange(651675, 651678),
            IntRange(655000, 655010),
            IntRange(655012, 655015),
            IntRange(655051, 655052),
            IntRange(655056, 655057),
        ),
        null,
    ),
    jcb(null, null),
    maestro(
        listOf(
            IntRange(561200, 561269),
            IntRange(561271, 561299),
            IntRange(561320, 561356),
            IntRange(581700, 581751),
            IntRange(581753, 581800),
            IntRange(589998, 591259),
            IntRange(591261, 596770),
            IntRange(596772, 598744),
            IntRange(598746, 599999),
            IntRange(600297, 600314),
            IntRange(600316, 600335),
            IntRange(600337, 600362),
            IntRange(600364, 600382),
            IntRange(601232, 601254),
            IntRange(601256, 601276),
            IntRange(601640, 601652),
            IntRange(601689, 601700),
            IntRange(602011, 602050),
            IntRange(639000, 639099),
            IntRange(670000, 679999),
        ),
        listOf("500033", "581149"),
    ),
    naranja(null, listOf("589562")),
    sodexo(null, null),
    vr(null, null),
    unknown(null, null),
    error(null, null),
}

val CardBrand.isValid: Boolean
    get() = this != CardBrand.error && this != CardBrand.unknown

// source: https://en.wikipedia.org/wiki/Payment_card_number
fun CardBrand.validateNumberLength(number: String): Boolean {
    return when (this) {
        CardBrand.americanExpress -> number.length == 15
        CardBrand.dinersClub -> number.length in 14..19
        CardBrand.discover -> number.length in 16..19
        CardBrand.jcb -> number.length in 16..19
        CardBrand.maestro -> number.length in 12..19
        CardBrand.mastercard -> number.length == 16
        CardBrand.visa -> number.length == 13 || number.length == 16 || number.length == 19
        CardBrand.unknown -> false
        CardBrand.error -> false
        else -> number.length in 12..19
    }
}

val CardBrand.maxNumberLength: Int get() = when (this) {
    CardBrand.americanExpress -> 15
    CardBrand.mastercard -> 16
    else -> 19
}

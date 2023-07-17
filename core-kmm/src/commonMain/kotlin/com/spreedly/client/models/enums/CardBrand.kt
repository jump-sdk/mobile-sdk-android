package com.spreedly.client.models.enums

import com.spreedly.client.models.Range

enum class CardBrand(val range: Array<Range>?, val bins: Array<String>?) {
    visa(null, null), mastercard(
        arrayOf(Range(222100, 272099), Range(510000, 559999)),
        null
    ),
    americanExpress(null, null), alelo(
        arrayOf(
            Range(402588, 402588),
            Range(404347, 404347),
            Range(405876, 405876),
            Range(405882, 405882),
            Range(405884, 405884),
            Range(405886, 405886),
            Range(430471, 430471),
            Range(438061, 438061),
            Range(438064, 438064),
            Range(470063, 470066),
            Range(496067, 496067),
            Range(506699, 506704),
            Range(506706, 506706),
            Range(506713, 506714),
            Range(506716, 506716),
            Range(506749, 506750),
            Range(506752, 506752),
            Range(506754, 506756),
            Range(506758, 506762),
            Range(506764, 506767),
            Range(506770, 506771),
            Range(509015, 509019),
            Range(509880, 509882),
            Range(509884, 509885),
            Range(509987, 509992)
        ), null
    ),
    cabal(
        arrayOf(Range(60420100, 60440099), Range(58965700, 58965799), Range(60352200, 60352299)),
        null
    ),
    carnet(
        arrayOf(
            Range(506199, 506499)
        ),
        arrayOf(
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
            "588772"
        )
    ),
    dankort(null, null), dinersClub(null, null), discover(null, null), elo(
        arrayOf(
            Range(506707, 506708),
            Range(506715, 506715),
            Range(506718, 506722),
            Range(506724, 506724),
            Range(506726, 506736),
            Range(506739, 506739),
            Range(506741, 506743),
            Range(506745, 506747),
            Range(506753, 506753),
            Range(506774, 506776),
            Range(506778, 506778),
            Range(509000, 509001),
            Range(509003, 509003),
            Range(509007, 509007),
            Range(509020, 509022),
            Range(509035, 509035),
            Range(509039, 509042),
            Range(509045, 509045),
            Range(509048, 509048),
            Range(509051, 509071),
            Range(509073, 509074),
            Range(509077, 509080),
            Range(509084, 509084),
            Range(509091, 509094),
            Range(509098, 509098),
            Range(509100, 509100),
            Range(509104, 509104),
            Range(509106, 509109),
            Range(627780, 627780),
            Range(636368, 636368),
            Range(650031, 650033),
            Range(650035, 650045),
            Range(650047, 650047),
            Range(650406, 650410),
            Range(650434, 650436),
            Range(650439, 650439),
            Range(650485, 650504),
            Range(650506, 650530),
            Range(650577, 650580),
            Range(650582, 650591),
            Range(650721, 650727),
            Range(650901, 650922),
            Range(650928, 650928),
            Range(650938, 650939),
            Range(650946, 650948),
            Range(650954, 650955),
            Range(650962, 650963),
            Range(650967, 650967),
            Range(650971, 650971),
            Range(651652, 651667),
            Range(651675, 651678),
            Range(655000, 655010),
            Range(655012, 655015),
            Range(655051, 655052),
            Range(655056, 655057)
        ), null
    ),
    jcb(null, null), maestro(
        arrayOf(
            Range(561200, 561269),
            Range(561271, 561299),
            Range(561320, 561356),
            Range(581700, 581751),
            Range(581753, 581800),
            Range(589998, 591259),
            Range(591261, 596770),
            Range(596772, 598744),
            Range(598746, 599999),
            Range(600297, 600314),
            Range(600316, 600335),
            Range(600337, 600362),
            Range(600364, 600382),
            Range(601232, 601254),
            Range(601256, 601276),
            Range(601640, 601652),
            Range(601689, 601700),
            Range(602011, 602050),
            Range(639000, 639099),
            Range(670000, 679999)
        ), arrayOf("500033", "581149")
    ),
    naranja(null, arrayOf("589562")), sodexo(null, null), vr(null, null), unknown(
        null,
        null
    ),
    error(null, null);

    private val icon = 0
}
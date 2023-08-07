package com.spreedly.client.models

import com.spreedly.client.models.enums.CardBrand
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SpreedlySecureOpaqueStringTest {
    @Test
    fun CanCreateString() {
        val string = SpreedlySecureOpaqueString("")
        assertNotNull(string)
    }

    @Test
    fun canCreateStringWithContent() {
        val string = SpreedlySecureOpaqueString("text")
        assertEquals("text", string._encode())
    }

    @Test
    fun CanClearString() {
        val string = SpreedlySecureOpaqueString("")
        string.append("test")
        string.clear()
        assertEquals(0, string.length)
    }

    @Test
    fun CanAppendString() {
        val string = SpreedlySecureOpaqueString("")
        string.append("test")
        assertEquals(4, string.length)
    }

    @Test
    fun canIdentifyMasterCard() {
        val string = SpreedlySecureOpaqueString("")
        string.append("5555555555554444")
        assertEquals(CardBrand.mastercard, string.detectCardType())
        assertEquals(CardBrand.mastercard, string.softDetect())
    }

    @Test
    fun canIdentifyMastercardAlt() {
        val string = SpreedlySecureOpaqueString("")
        string.append("2223003122003222")
        assertEquals(CardBrand.mastercard, string.detectCardType())
        assertEquals(CardBrand.mastercard, string.softDetect())
    }

    @Test
    fun canIdentifyVisaCard() {
        val string = SpreedlySecureOpaqueString("")
        string.append("4111111111111111")
        assertEquals(CardBrand.visa, string.detectCardType())
        assertEquals(CardBrand.visa, string.softDetect())
    }

    @Test
    fun canIdentifyLongVisaCard() {
        val string = SpreedlySecureOpaqueString("")
        string.append("4444333322221111455")
        assertEquals(CardBrand.visa, string.detectCardType())
        assertEquals(CardBrand.visa, string.softDetect())
    }

    @Test
    fun canIdentifyDankort() {
        val string = SpreedlySecureOpaqueString("")
        string.append("5019717010103742")
        assertEquals(CardBrand.dankort, string.detectCardType())
        assertEquals(CardBrand.dankort, string.softDetect())
    }

    @Test
    fun canIdentifyAmericanExpress() {
        val string = SpreedlySecureOpaqueString("")
        string.append("378282246310005")
        assertEquals(CardBrand.americanExpress, string.detectCardType())
        assertEquals(CardBrand.americanExpress, string.softDetect())
    }

    @Test
    fun canIdentifyDiscover() {
        val string = SpreedlySecureOpaqueString("")
        string.append("6011111111111117")
        assertEquals(CardBrand.discover, string.detectCardType())
        assertEquals(CardBrand.discover, string.softDetect())
    }

    @Test
    fun canIdentifyDinersClub() {
        val string = SpreedlySecureOpaqueString("")
        string.append("30569309025904")
        assertEquals(CardBrand.dinersClub, string.detectCardType())
        assertEquals(CardBrand.dinersClub, string.softDetect())
    }

    @Test
    fun canIdentifyJCB() {
        val string = SpreedlySecureOpaqueString("")
        string.append("3569990010030400")
        assertEquals(CardBrand.jcb, string.detectCardType())
        assertEquals(CardBrand.jcb, string.softDetect())
    }

    @Test
    fun canIdentifyMaestro() {
        val string = SpreedlySecureOpaqueString("")
        string.append("6759000000000000005")
        assertEquals(CardBrand.maestro, string.detectCardType())
        assertEquals(CardBrand.maestro, string.softDetect())
    }

    @Test
    fun canIdentifySodexo() {
        val string = SpreedlySecureOpaqueString("")
        string.append("6060690000000002")
        assertEquals(CardBrand.sodexo, string.detectCardType())
        assertEquals(CardBrand.sodexo, string.softDetect())
    }

    @Test
    fun canIdentifyNaranja() {
        val string = SpreedlySecureOpaqueString("")
        string.append("5895627823453003")
        assertEquals(CardBrand.naranja, string.detectCardType())
        assertEquals(CardBrand.naranja, string.softDetect())
    }

    @Test
    fun canIdentifyElo() {
        val string = SpreedlySecureOpaqueString("")
        string.append("5067310000000010")
        assertEquals(CardBrand.elo, string.detectCardType())
        assertEquals(CardBrand.elo, string.softDetect())
    }

    @Test
    fun canIdentifyAlelo() {
        val string = SpreedlySecureOpaqueString("")
        string.append("5067705232092752")
        assertEquals(CardBrand.alelo, string.detectCardType())
        assertEquals(CardBrand.alelo, string.softDetect())
    }

    @Test
    fun canIdentifyCabal() {
        val string = SpreedlySecureOpaqueString("")
        string.append("6035227716427021")
        assertEquals(CardBrand.cabal, string.detectCardType())
        assertEquals(CardBrand.cabal, string.softDetect())
    }

    @Test
    fun canIdentifyCarnet() {
        val string = SpreedlySecureOpaqueString("")
        string.append("5062280000000002")
        assertEquals(CardBrand.carnet, string.detectCardType())
        assertEquals(CardBrand.carnet, string.softDetect())
    }

    @Test
    fun detectCardTypeErrorsWithInvalidNumber() {
        val string = SpreedlySecureOpaqueString("")
        string.append("5895627823453005")
        assertEquals(CardBrand.error, string.detectCardType())
    }

    @Test
    fun detectCardTypeReturnsUnknownIfCardDoesntMatch() {
        val string = SpreedlySecureOpaqueString("")
        string.append("8795727823453055")
        assertEquals(CardBrand.unknown, string.detectCardType())
        assertEquals(CardBrand.unknown, string.softDetect())
    }

    @Test
    fun softDetectLongCCReturnsUnknown() {
        val string = SpreedlySecureOpaqueString("87957278888888823453055")
        assertEquals(CardBrand.unknown, string.softDetect())
    }

    @Test
    fun nonvalidInputReturnsError() {
        val string = SpreedlySecureOpaqueString("notnumbers")
        assertEquals(CardBrand.error, string.detectCardType())
    }

    @Test
    fun canIdentifyCarnetAlt() {
        val string = SpreedlySecureOpaqueString("5022750000000003")
        assertEquals(CardBrand.carnet, string.softDetect())
        assertEquals(CardBrand.carnet, string.detectCardType())
    }

    @Test
    fun canIdentifyMaestroAlt() {
        val string = SpreedlySecureOpaqueString("5000330000000000")
        assertEquals(CardBrand.maestro, string.softDetect())
        assertEquals(CardBrand.maestro, string.detectCardType())
    }

    @Test
    fun canIdentifyVR() {
        val string = SpreedlySecureOpaqueString("6274160000000008")
        assertEquals(CardBrand.vr, string.softDetect())
        assertEquals(CardBrand.vr, string.detectCardType())
    }

    @Test
    fun canIdentifyNumber() {
        val string = SpreedlySecureOpaqueString("12345")
        assertTrue(string.isNumber)
    }

    @Test
    fun canIdentifyBadNumber() {
        val string = SpreedlySecureOpaqueString("1234-5")
        assertFalse(string.isNumber)
    }

    @Test
    fun shortCardNumberReturnsUnknown() {
        val string = SpreedlySecureOpaqueString("79927398713")
        assertEquals(CardBrand.unknown, string.detectCardType())
    }
}

package com.spreedly.client.models

import kotlin.test.Test
import kotlin.test.assertEquals

class RecacheInfoTest {
    @Test
    fun canCreateRecacheInfo() {
        val cvv = SpreedlySecureOpaqueString("444")
        val recacheInfo = RecacheInfo(cvv)
        val expected = "{\"payment_method\":{\"credit_card\":{\"verification_value\":\"444\"}}}"
        assertEquals(expected, recacheInfo.toJson().toString())
    }
}

package com.spreedly.client.models

class Range(val first: Int, val last: Int) {
    fun inRange(number: Int): Boolean {
        return first <= number && last >= number
    }
}
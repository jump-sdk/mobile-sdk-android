package com.spreedly.client

import io.ktor.client.engine.okhttp.OkHttp

actual val engine = OkHttp.create()

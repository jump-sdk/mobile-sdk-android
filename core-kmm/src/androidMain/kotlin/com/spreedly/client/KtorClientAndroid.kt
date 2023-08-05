package com.spreedly.client

import io.ktor.client.engine.okhttp.OkHttp

internal actual val engine = OkHttp.create()

package com.spreedly.client

import io.ktor.client.engine.darwin.Darwin

internal actual val engine = Darwin.create()

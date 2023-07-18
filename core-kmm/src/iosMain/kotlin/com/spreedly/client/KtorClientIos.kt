package com.spreedly.client

import io.ktor.client.engine.darwin.Darwin

actual val engine = Darwin.create()

package com.xinto.opencord.util

import android.util.Log
import com.xinto.opencord.BuildConfig

interface Logger {
    fun error(tag: String, message: String?, throwable: Throwable?)
    fun warn(tag: String, message: String, throwable: Throwable?)
    fun info(tag: String, message: String)
    fun debug(tag: String, message: String)
}

class LoggerImpl : Logger {
    private val fieldRegex = """"(login|password|email|phone|token)"\s*:\s*"[^"]+"""".toRegex()

    private fun clean(message: String): String {
        return message.replace(fieldRegex) {
            "\"${it.groupValues[1]}\":\"[OpenCord censored]\""
        }
    }

    override fun error(tag: String, message: String?, throwable: Throwable?) {
        if (message != null || throwable != null)
            Log.e(tag, message, throwable)
    }

    override fun warn(tag: String, message: String, throwable: Throwable?) {
        Log.w(tag, message, throwable)
    }

    override fun info(tag: String, message: String) {
        Log.i(tag, message)
    }

    override fun debug(tag: String, message: String) {
        if (!BuildConfig.DEBUG) return

        for (part in clean(message).chunked(4000)) {
            Log.d(tag, part)
        }
    }
}

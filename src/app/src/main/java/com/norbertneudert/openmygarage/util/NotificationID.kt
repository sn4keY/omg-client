package com.norbertneudert.openmygarage.util

import java.util.concurrent.atomic.AtomicInteger

class NotificationID {
    companion object {
        private val c: AtomicInteger = AtomicInteger(0)
        fun getID(): Int {
            return c.incrementAndGet()
        }
    }
}
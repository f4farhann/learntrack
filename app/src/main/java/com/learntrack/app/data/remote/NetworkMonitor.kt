package com.learntrack.app.data.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkMonitor @Inject constructor() {
    @Volatile
    var isOnline: Boolean = true
}
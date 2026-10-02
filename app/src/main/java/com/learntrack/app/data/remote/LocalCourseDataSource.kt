package com.learntrack.app.data.remote

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject


class LocalCourseDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkMonitor: NetworkMonitor
) : CourseDataSource {

    override suspend fun getCourses(): List<CourseDto> {
        delay(1000) // pretend it's a network call
        if (!networkMonitor.isOnline) throw IOException("No internet connection")

        return withContext(Dispatchers.IO) {
            val json = context.assets.open("courses.json")
                .bufferedReader().use { it.readText() }
            Gson().fromJson(json, Array<CourseDto>::class.java).toList()
        }
    }
}
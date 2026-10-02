package com.learntrack.app

import com.learntrack.app.data.remote.NetworkMonitor
import com.learntrack.app.domain.model.Course
import com.learntrack.app.domain.model.Lesson
import com.learntrack.app.ui.dashboard.DashboardUiState
import com.learntrack.app.ui.dashboard.DashboardViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    @get:Rule
    val mainRule = MainDispatcherRule()

    private val sampleCourse = Course(
        id = 1,
        title = "Python Programming",
        instructor = "John Smith",
        lessons = listOf(Lesson(101, "Intro", true), Lesson(102, "OOP", false))
    )

    @Test
    fun `cached courses are still shown when refresh fails (offline)`() = runTest {
        val repo = FakeCourseRepository()
        repo.courses.value = listOf(sampleCourse)                // data loaded earlier
        repo.refreshResult = Result.failure(IOException("offline")) // now the network fails

        val viewModel = DashboardViewModel(repo, NetworkMonitor())
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { }   // start collecting so stateIn becomes active
        }

        val state = viewModel.uiState.value
        assertTrue("Expected Success but was $state", state is DashboardUiState.Success)
        state as DashboardUiState.Success
        assertEquals(1, state.courses.size)
        assertTrue(state.showOfflineBanner)
    }

    @Test
    fun `shows error when refresh fails and nothing is cached`() = runTest {
        val repo = FakeCourseRepository()
        repo.refreshResult = Result.failure(IOException("offline"))

        val viewModel = DashboardViewModel(repo, NetworkMonitor())
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { }
        }

        assertTrue(viewModel.uiState.value is DashboardUiState.Error)
    }
}
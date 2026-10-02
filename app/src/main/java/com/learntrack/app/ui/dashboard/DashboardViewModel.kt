package com.learntrack.app.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learntrack.app.data.remote.NetworkMonitor
import com.learntrack.app.domain.repository.CourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: CourseRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)
    private val _isOffline = MutableStateFlow(false)

    val isOffline: StateFlow<Boolean> = _isOffline.asStateFlow()

    // Cached courses always win, so going offline never hides saved data
    val uiState: StateFlow<DashboardUiState> = combine(
        repository.observeCourses(), _isLoading, _error
    ) { courses, loading, error ->
        when {
            courses.isNotEmpty() -> DashboardUiState.Success(
                courses = courses,
                showOfflineBanner = error != null
            )
            loading -> DashboardUiState.Loading
            error != null -> DashboardUiState.Error(error)
            else -> DashboardUiState.Empty
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardUiState.Loading)

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository.refresh()
                .onFailure { _error.value = "Couldn't load courses. Check your connection." }
            _isLoading.value = false
        }
    }

    // Demo only: the data source is a local file, so we simulate offline
    fun setOffline(offline: Boolean) {
        _isOffline.value = offline
        networkMonitor.isOnline = !offline
        refresh()
    }
}
package com.learntrack.app.ui.dashboard

import com.learntrack.app.domain.model.Course


sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data object Empty : DashboardUiState
    data class Success(
        val courses: List<Course>,
        val showOfflineBanner: Boolean
    ) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}
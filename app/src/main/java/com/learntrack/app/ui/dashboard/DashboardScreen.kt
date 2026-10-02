package com.learntrack.app.ui.dashboard

import com.learntrack.app.domain.model.Course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onCourseClick: (Int) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val isOffline by viewModel.isOffline.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Courses") },
                actions = {
                    Text("Offline", style = MaterialTheme.typography.labelMedium)
                    Switch(
                        checked = isOffline,
                        onCheckedChange = viewModel::setOffline,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (val s = state) {
                DashboardUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                DashboardUiState.Empty -> Text(
                    "No courses available yet.",
                    modifier = Modifier.align(Alignment.Center)
                )

                is DashboardUiState.Error -> Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(s.message, color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = viewModel::refresh) { Text("Retry") }
                }

                is DashboardUiState.Success -> CourseList(
                    courses = s.courses,
                    showOfflineBanner = s.showOfflineBanner,
                    onCourseClick = onCourseClick
                )
            }
        }
    }
}

@Composable
private fun CourseList(
    courses: List<Course>,
    showOfflineBanner: Boolean,
    onCourseClick: (Int) -> Unit
) {
    Column {
        if (showOfflineBanner) {
            Text(
                "Offline: showing saved data",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                style = MaterialTheme.typography.labelLarge
            )
        }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(courses, key = { it.id }) { course ->
                CourseCard(course, onContinue = { onCourseClick(course.id) })
            }
        }
    }
}

@Composable
private fun CourseCard(course: Course, onContinue: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(course.title, style = MaterialTheme.typography.titleMedium)
            Text(course.instructor, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Text("${course.totalLessons} lessons  •  ${course.progress}% complete")
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { course.progress / 100f },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Button(onClick = onContinue) { Text("Continue") }
        }
    }
}
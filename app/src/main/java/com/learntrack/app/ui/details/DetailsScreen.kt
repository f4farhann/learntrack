package com.learntrack.app.ui.details

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
import com.learntrack.app.domain.model.Lesson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    onBack: () -> Unit,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val course by viewModel.course.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(course?.title ?: "Course") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }
            )
        }
    ) { padding ->
        val c = course
        if (c == null) {
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(Modifier.padding(padding).padding(16.dp)) {
                Text("Progress: ${c.progress}%", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { c.progress / 100f },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))

                LazyColumn {
                    items(c.lessons, key = { it.id }) { lesson ->
                        LessonRow(lesson, onComplete = { viewModel.onLessonCompleted(lesson.id) })
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonRow(lesson: Lesson, onComplete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            Text(lesson.title, style = MaterialTheme.typography.bodyLarge)
            Text(if (lesson.completed) "✓ Completed" else "○ Pending")
        }
        if (!lesson.completed) {
            Button(onClick = onComplete) { Text("Mark done") }
        }
    }
}
package dev.belalkhan.compose_navigation.create

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CreateView(
    viewModel: CreateViewModel = viewModel(),
    onPostCreated: () -> Unit = {}
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Create New Post",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = state.title,
            onValueChange = { viewModel.onTitleChange(it) },
            label = { Text("Title") },
            placeholder = { Text("Enter post title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.description,
            onValueChange = { viewModel.onDescriptionChange(it) },
            label = { Text("Description") },
            placeholder = { Text("Write something...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            maxLines = 5
        )

        Button(
            onClick = { viewModel.onSubmit() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = viewModel.isValid()
        ) {
            Text(if (state.isSubmitting) "Submitting..." else "Post")
        }

        val lifeCycleOwner = LocalLifecycleOwner.current

        LaunchedEffect(Unit) {
            lifeCycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventFlow.collectLatest { event ->
                    when(event){
                        NavigationEvent.NavigateToList -> onPostCreated()
                    }
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
private fun CreateViewPreview() {
    MaterialTheme {
        Surface {
            CreateView(CreateViewModel()) //preview only
        }
    }
}
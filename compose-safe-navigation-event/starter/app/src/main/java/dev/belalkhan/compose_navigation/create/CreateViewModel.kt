package dev.belalkhan.compose_navigation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CreateScreenState(
    val title: String = "",
    val description: String = "",
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val success: String? = null
)

class CreateViewModel : ViewModel() {
    private val _state = MutableStateFlow(CreateScreenState())
    val state: StateFlow<CreateScreenState> = _state

    fun onTitleChange(title: String) {
        _state.value = _state.value.copy(title = title)
    }

    fun onDescriptionChange(description: String) {
        _state.value = _state.value.copy(description = description)
    }

    fun isValid(): Boolean {
        val state = _state.value
        return state.title.isNotBlank() &&
                state.description.isNotBlank() &&
                !state.isSubmitting
    }


    fun onSubmit() = viewModelScope.launch {
        _state.value = _state.value.copy(isSubmitting = true)
        delay(2000L) // Simulating Network Call
        _state.value = _state.value.copy(isSubmitting = false, success = "Successfully created")
    }

    override fun onCleared() {
        super.onCleared()
        println("CreateViewModel destroyed")
    }

}
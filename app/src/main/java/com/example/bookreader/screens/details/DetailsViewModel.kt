package com.example.bookreader.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreader.data.Resource
import com.example.bookreader.model.Item
import com.example.bookreader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: BookRepository): ViewModel() {
    suspend fun getBookInfo(bookId: String): Resource<Item>{
        return repository.getBookInfo(bookId)
    }
}
package com.example.bookreader.repository

import com.example.bookreader.data.DataOrException
import com.example.bookreader.model.Item
import com.example.bookreader.network.BooksApi
import javax.inject.Inject

class BooksRepository @Inject constructor(private val api: BooksApi){
    private val dataOrException =
        DataOrException<List<Item>, Boolean, Exception>()
    private val bookInfoDataOrException =
        DataOrException<Item, Boolean, Exception>()

}
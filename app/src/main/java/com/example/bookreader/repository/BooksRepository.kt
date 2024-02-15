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

    suspend fun getBooks(searchQuery: String): DataOrException<List<Item>, Boolean,Exception>  {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllBooks(searchQuery).items
            if (dataOrException.data!!.isNotEmpty()) dataOrException.loading = false
        }catch (e : Exception){
            dataOrException.e = e
        }
        return dataOrException
    }

    suspend fun getBookInfo(bookId: String): DataOrException<Item, Boolean, Exception>{
        val response = try {
            bookInfoDataOrException.loading = true
            bookInfoDataOrException.data = api.getBookInfo(bookId = bookId)
            if (bookInfoDataOrException.data.toString().isNotEmpty()) bookInfoDataOrException.loading = false
            else{}
        }catch (e: java.lang.Exception){
            bookInfoDataOrException.e = e
        }
        return bookInfoDataOrException
    }


}
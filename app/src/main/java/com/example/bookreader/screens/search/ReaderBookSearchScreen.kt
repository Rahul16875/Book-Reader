package com.example.bookreader.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.bookreader.components.InputField
import com.example.bookreader.components.ReaderAppBar
import com.example.bookreader.model.Item
import com.example.bookreader.navigation.ReaderScreens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: BookSearchViewModel = hiltViewModel(),
){
    Scaffold(topBar = {
        ReaderAppBar(
            title = "Search Books",
            icon = Icons.Default.ArrowBack,
            navController = navController,
            showProfile = false){
            navController.navigate(ReaderScreens.ReaderHomeScreen.name)
//            We can also write..
//            navController.popBackStack()
        }
    }) {it ->
        Surface(modifier = Modifier.padding(top = 50.dp)) {
            Column {
                SearchForm(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)){searchQuery ->
                    viewModel.searchBooks(searchQuery)

                }
                Spacer(modifier = Modifier.height(13.dp))
                BookList(navController)
            }
        }
    }
}

@Composable
fun BookList(
    navController: NavController,
    viewModel: BookSearchViewModel = hiltViewModel()) {

    val listOfBooks = viewModel.list
    if (viewModel.isLoading){
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }else{
        LazyColumn(modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ){
            items(items = listOfBooks){ book ->
                BookRow(book,navController)
            }
        }
    }
//    val listOfBooks = listOf(
//        MBook(id = "dadfa", title = "Hello Again", authors = "Rahul Agrawal", notes = null),
//        MBook(id = "dafa", title = "Again", authors = "Rahul Agrawal", notes = null),
//        MBook(id = "dada", title = "Hello", authors = "Rahul Agrawal", notes = null),
//        MBook(id = "ddfa", title = "Hello Again", authors = "Rahul Agrawal", notes = null),
//        MBook(id = "dada", title = "Hello Again", authors = "Rahul Agrawal", notes = null)
//    )

}

@Composable
fun BookRow(book: Item, navController: NavController) {
    Card(modifier = Modifier
        .clickable {
            navController.navigate(ReaderScreens.DetailScreen.name + "/${book.id}")
        }
        .fillMaxWidth()
        .height(120.dp)
        .padding(5.dp),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(7.dp)) {
            Row (modifier = Modifier.padding(5.dp),
                verticalAlignment = Alignment.Top){
                val imageUrl: String =if (book.volumeInfo.imageLinks.smallThumbnail.isEmpty())
                    "http://books.google.com/books/content?id=Mpj7SRoy3gEC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
                else {
                    book.volumeInfo.imageLinks.smallThumbnail
                }
                Image(painter = rememberImagePainter(data = imageUrl),
                    contentDescription ="book_image",
                    modifier = Modifier
                        .width(80.dp)
                        .fillMaxHeight()
                        .padding(end = 4.dp))
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(text = book.volumeInfo.title, overflow = TextOverflow.Ellipsis)
                    Text(text = "Author: ${book.volumeInfo.authors}",
                        overflow = TextOverflow.Clip,
                        fontStyle = FontStyle.Italic)
                    Text(text = "Date: ${book.volumeInfo.publishedDate}",
                        overflow = TextOverflow.Clip,
                        fontStyle = FontStyle.Italic)
                    Text(text = "${book.volumeInfo.categories}",
                        overflow = TextOverflow.Clip,
                        fontStyle = FontStyle.Italic)
                    }
            }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) ->Unit ={}
){
    Column {
        val searchQueryState = rememberSaveable {
            mutableStateOf("")
        }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value){
            searchQueryState.value.trim().isNotEmpty()
        }

        InputField(
            valueState = searchQueryState,
            labelId = "Search",
            enabled = true,
            onAction = KeyboardActions{
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
//                searchQueryState.value = ""
                keyboardController?.hide()
            }
        )
    }
}
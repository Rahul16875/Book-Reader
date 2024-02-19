package com.example.bookreader.screens.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.bookreader.components.ReaderAppBar
import com.example.bookreader.model.Item
import com.example.bookreader.model.MBook
import com.example.bookreader.navigation.ReaderScreens
import com.example.bookreader.screens.home.HomeScreenViewModel
import com.example.bookreader.screens.search.BookRow
import com.example.bookreader.utills.formatDate
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(navController: NavController, viewModel: HomeScreenViewModel = hiltViewModel()){

        var books: List<MBook>
        val currentUser = FirebaseAuth.getInstance().currentUser

        Scaffold(
                topBar = {
                        ReaderAppBar(title = "Book Stats",
                                icon = Icons.Default.ArrowBack,
                                showProfile = false,
                                navController = navController){
                                navController.popBackStack()
                        }
                }) {it ->
                Surface {
                        books = if (!viewModel.data.value.data.isNullOrEmpty()){
                                viewModel.data.value.data!!.filter {
                                        (it.userId == currentUser?.uid)
                                }
                        }else{
                                emptyList()
                        }
                        Column {
                                Row(modifier = Modifier.padding(start = 20.dp, top = 55.dp)){
                                        Box(modifier = Modifier
                                                .size(45.dp)
                                                .padding(2.dp)){
                                                Icon(imageVector = Icons.Sharp.Person,
                                                        contentDescription = "icon")
                                        }
                                        Text(text = "Hi, ${
                                                currentUser?.email.toString().split("@")[0].uppercase(Locale.getDefault())
                                        }")
                                }
                                Card(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp),
                                        shape = CircleShape,
                                        elevation = CardDefaults.cardElevation(5.dp)) {
                                        val readBookList: List<MBook> = if (!viewModel.data.value.data.isNullOrEmpty()){
                                                books.filter {
                                                        (it.userId == currentUser?.uid) && (it.finishedReading != null)
                                                }
                                        }else{
                                                emptyList()
                                        }
                                        
                                        val readingBooks = books.filter {
                                                (it.startedReading != null && it.finishedReading == null)
                                        }
                                        
                                        Column (modifier = Modifier.padding(start = 25.dp,
                                                top = 4.dp,
                                                bottom = 4.dp),
                                                horizontalAlignment = Alignment.Start){
                                                Text(text = "Your Stats",
                                                        fontWeight = FontWeight.SemiBold)
                                                Divider()
                                                Text(text = "You're reading: ${readingBooks.size} books")
                                                Text(text = " You've read: ${readBookList.size} books")
                                                
                                        }
                                }
                                if (viewModel.data.value.loading == true){
                                        LinearProgressIndicator()
                                }else{
                                        Divider()
                                        LazyColumn(modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(),
                                                contentPadding = PaddingValues(16.dp)){
                                                // filter books by finished ones
                                                val readBooks: List<MBook> = if (!viewModel.data.value.data.isNullOrEmpty()){
                                                        viewModel.data.value.data!!.filter {
                                                                (it.userId == currentUser?.uid) && (it.finishedReading != null)
                                                        }
                                                }else{
                                                        emptyList()
                                                }

                                                items(items = readBooks){
                                                   BookRowStats(book = it)
                                                }
                                        }
                                }
                        }
                }

        }

}

@Composable
fun BookRowStats(
        book: MBook) {
        Card(modifier = Modifier
                .clickable {
//                        navController.navigate(ReaderScreens.DetailScreen.name + "/${book.id}")
                }
                .fillMaxWidth()
                .height(120.dp)
                .padding(5.dp),
                shape = RectangleShape,
                elevation = CardDefaults.cardElevation(7.dp)) {
                Row (modifier = Modifier.padding(5.dp),
                        verticalAlignment = Alignment.Top){
                        val imageUrl: String =if (book.photoUrl.toString().isEmpty())
                                "http://books.google.com/books/content?id=Mpj7SRoy3gEC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
                        else {
                                book.photoUrl.toString()
                        }
                        Image(painter = rememberImagePainter(data = imageUrl),
                                contentDescription ="book_image",
                                modifier = Modifier
                                        .width(80.dp)
                                        .fillMaxHeight()
                                        .padding(end = 4.dp))


                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                Row (horizontalArrangement = Arrangement.SpaceBetween){
                                        Text(text = book.title.toString(), overflow = TextOverflow.Ellipsis)
                                        if (book.rating!! >= 4){
                                                Spacer(modifier = Modifier.fillMaxWidth(0.8f))
                                                Icon(
                                                        imageVector = Icons.Default.ThumbUp,
                                                        contentDescription = "Thumbs Up",
                                                        tint = Color.Green.copy(alpha = 0.5f)
                                                )
                                        }else{
                                                Box{}
                                        }
                                }
                                Text(text = "Author: ${book.authors}",
                                        overflow = TextOverflow.Clip,
                                        fontStyle = FontStyle.Italic)

                                Text(text = "Started: ${formatDate(book.startedReading!!)}",
                                        softWrap = true,
                                        overflow = TextOverflow.Clip,
                                        fontStyle = FontStyle.Italic)

                                Text(text = "Finished: ${formatDate(book.finishedReading!!)}",
                                        overflow = TextOverflow.Clip,
                                        fontStyle = FontStyle.Italic)
                        }
                }
        }
}
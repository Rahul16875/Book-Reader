package com.example.bookreader.screens.home

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bookreader.components.FABContent
import com.example.bookreader.components.HomeContent
import com.example.bookreader.components.HorizontalScrollableComponent
import com.example.bookreader.components.ListCard
import com.example.bookreader.components.ReaderAppBar
import com.example.bookreader.model.MBook
import com.example.bookreader.navigation.ReaderScreens

//@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderHomeScreen(
    navController: NavController = NavController(context = LocalContext.current),
    viewModel: HomeScreenViewModel = hiltViewModel(),
){
       Scaffold(topBar = {
            ReaderAppBar(title = "A.Reader", navController = navController)
       }, floatingActionButton = {
               FABContent {
                    navController.navigate(ReaderScreens.SearchScreen.name)
               }
       }) {padding ->
               // content
               Surface (modifier = Modifier
                   .fillMaxSize()
                   .padding(top = 60.dp),){
                        HomeContent(navController,viewModel)

               }
       }
}


//@Composable
//fun ReadingRightNowArea(books: List<MBook>, navController: NavController){
//    ListCard(MBook())
//}

@Composable
fun ReadingRightNowArea(listOfBooks: List<MBook>,
                        navController: NavController) {
    //Filter books by reading now
    val readingNowList = listOfBooks.filter { mBook ->
        mBook.startedReading != null && mBook.finishedReading == null
    }

    HorizontalScrollableComponent(readingNowList){
        Log.d("TAG", "BoolListArea: $it")
        navController.navigate(ReaderScreens.UpdateScreen.name + "/$it")
    }

}
package com.example.bookreader.screens.update

import android.content.Context
import android.media.Rating
import android.util.Log
import android.view.MotionEvent
import android.widget.RatingBar
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.bookreader.R
import com.example.bookreader.components.InputField
import com.example.bookreader.components.ReaderAppBar
import com.example.bookreader.components.RoundedButton
import com.example.bookreader.data.DataOrException
import com.example.bookreader.model.MBook
import com.example.bookreader.navigation.ReaderScreens
import com.example.bookreader.screens.home.HomeScreenViewModel
import com.example.bookreader.utills.formatDate
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScreen(
    navController: NavController,
    bookItemId: String,
    viewModel: HomeScreenViewModel = hiltViewModel()
){
        Scaffold(topBar = {
            ReaderAppBar(title = "Update Book",
                icon = Icons.Default.ArrowBack,
                showProfile = false,
                navController =navController){
                navController.popBackStack()
            }

        }) {it ->
            val bookInfo = produceState<DataOrException<List<MBook>,
                    Boolean,
                    Exception>>(initialValue = DataOrException(data = emptyList(),
                        true, Exception(""))){
                        value = viewModel.data.value
            }.value

            Surface(modifier = Modifier
                .fillMaxSize()
                .padding(25.dp)) {
                Column(
                    modifier = Modifier.padding(top = 3.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (bookInfo.loading == true){
                        LinearProgressIndicator()
                        bookInfo.loading = false

                    }else{
                        Surface(modifier = Modifier
                            .padding(top = 30.dp)
                            .padding(2.dp)
                            .fillMaxWidth(),
                            shape = CircleShape,
                            shadowElevation = 4.dp
                            ) {
                                showBookUpdate(bookInfo = viewModel.data.value, bookItemId = bookItemId)
                        }

                        ShowSimpleForm(book = viewModel.data.value.data?.first {
                                it.googleBookId == bookItemId
                        }!!, navController)

                    }
                }
            }

        }
}

@Composable
fun ShowSimpleForm(book: MBook, navController: NavController) {

    val notesText = remember {
        mutableStateOf("")
    }

    SimpleForm(defaultValue = book.notes.toString().ifEmpty { "No thoughts available." }){
        notesText.value = it
    }

    val isStartedReading = remember {
        mutableStateOf(false)
    }

    val isFinishedReading = remember {
        mutableStateOf(false)
    }

    val ratingVal = remember {
        mutableStateOf(0)
    }

    val context = LocalContext.current
    Row(modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start) {

        TextButton(onClick = { isStartedReading.value = true },
            enabled = book.startedReading == null) {

            if (book.startedReading == null) {
                if (!isStartedReading.value) {
                    Text(text = "Start Reading")
                } else {
                    Text(
                        text = "Started Reading",
                        modifier = Modifier.alpha(0.6f),
                        color = Color.Red.copy(alpha = 0.5f)
                    )
                }
            }else{
                Text(text = "Started on: ${formatDate(book.startedReading!!) }")
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        TextButton(onClick = { isFinishedReading.value = true },
            enabled = book.finishedReading == null) {

            if (book.finishedReading == null){
                if (!isFinishedReading.value){
                    Text(text = "Mars as Read")
                }else{
                    Text(text = "Finished Reading!")
                }
            }else{
                Text(text = "Finished on: ${formatDate(book.finishedReading!!)}")
            }
        }
    }

    Text(text = "Rating", modifier = Modifier.padding(bottom = 5.dp))

    book.rating?.toInt().let {
        RatingBar(rating = it!!){ rating ->
            ratingVal.value = rating
        }
    }

    Spacer(modifier = Modifier.padding(bottom = 15.dp))


    Row {

        val changedNotes = book.notes != notesText.value
        val changedRating = book.rating?.toInt() != ratingVal.value

        val isFinishedTimeStamp = if (isFinishedReading.value) Timestamp.now()
        else book.finishedReading
        val isStartedTimeStamp = if (isStartedReading.value) Timestamp.now()
        else book.startedReading

        val bookUpdate = changedNotes || changedRating || isStartedReading.value || isFinishedReading.value

        val bookToUpdate = hashMapOf(
            "finished_reading_at" to isFinishedTimeStamp,
            "started_reading_at" to isStartedTimeStamp,
            "rating" to ratingVal.value,
            "notes" to notesText.value).toMap()


        RoundedButton(label = "Update"){
            if (bookUpdate){
                FirebaseFirestore.getInstance()
                    .collection("books")
                    .document(book.id!!)
                    .update(bookToUpdate)
                    .addOnCompleteListener {
                        showToast(context, "Book Updated Successfully!")
                        navController.navigate(ReaderScreens.ReaderHomeScreen.name)

                    }.addOnFailureListener {
                        Log.w("Error","Error updating document",it)
                    }
            }

        }

        Spacer(modifier = Modifier.width(100.dp))

        val openDialog = remember {
            mutableStateOf(false)
        }
        if (openDialog.value) run {
            ShowAlertDialog(message = stringResource(id = R.string.sure) + "\n" +
                            stringResource(id = R.string.action),openDialog){
                FirebaseFirestore.getInstance()
                    .collection("books")
                    .document(book.id!!)
                    .delete()
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            openDialog.value = false
                            navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                        }
                    }
            }
        }
        RoundedButton(label = "Delete"){
            openDialog.value = true
        }
    }
}

@Composable
fun ShowAlertDialog(
    message: String,
    openDialog: MutableState<Boolean>,
    onYesPressed: () -> Unit) {

    if (openDialog.value){
        AlertDialog(onDismissRequest = {},
                title ={ Text(text = "Delete Book")},
                text = { Text(text = message)},
                confirmButton = {
                    Row (modifier = Modifier.padding(all = 8.dp),
                        horizontalArrangement = Arrangement.Center){
                        TextButton(onClick = { onYesPressed.invoke()}) {
                            Text(text = "Yes")
                        }
                        TextButton(onClick = { openDialog.value = false }) {
                            Text(text = "No")
                        }
                    }
                })
    }
}

fun showToast(context: Context, msg: String) {
    Toast.makeText(context,msg, Toast.LENGTH_LONG).show()
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SimpleForm(modifier: Modifier = Modifier,
               loading: Boolean = false,
               defaultValue: String = "Great Book!",
               onSearch: (String) -> Unit) {
        Column {
            val textFieldValue = rememberSaveable{ mutableStateOf(defaultValue) }
            val keyboardController = LocalSoftwareKeyboardController.current
            val valid = remember(textFieldValue.value){
                textFieldValue.value.trim().isNotEmpty()
            }

            InputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(top = 3.dp)
                    .background(Color.White, CircleShape)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                valueState = textFieldValue,
                labelId = "Enter Your Thoughts",
                enabled = true,
                onAction = KeyboardActions{
                    if (!valid) return@KeyboardActions
                    onSearch(textFieldValue.value.trim())
                    keyboardController?.hide()
                }
            )
        }
}

@Composable
fun showBookUpdate(bookInfo: DataOrException<List<MBook>,
        Boolean, Exception>, bookItemId: String) {
    Row() {
        Spacer(modifier = Modifier.width(43.dp))
        if (bookInfo.data != null){
            Column(modifier = Modifier.padding(4.dp),
                verticalArrangement = Arrangement.Center) {

                CardListItem(book = bookInfo.data!!.first{
                    it.googleBookId == bookItemId
                }, onPressDetails = {})
            }
        }
    }

}

@Composable
fun CardListItem(book: MBook, onPressDetails: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(
                2.dp
            )
            .clip(RoundedCornerShape(20.dp))
            .clickable { },
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            Image(
                painter = rememberImagePainter(data = book.photoUrl.toString()),
                contentDescription = null,
                modifier = Modifier
                    .height(100.dp)
                    .width(120.dp)
                    .padding(4.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 120.dp,
                            topEnd = 20.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp
                        )
                    )
            )

            Column {
                Text(
                    text = book.title.toString(),
                    modifier = Modifier
                        .padding(
                            start = 8.dp,
                            end = 8.dp
                        )
                        .width(120.dp),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.authors.toString(),
                    modifier = Modifier.padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 2.dp,
                        bottom = 0.dp
                    )
                )
                Text(
                    text = book.publishedDate.toString(),
                    modifier = Modifier.padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 0.dp,
                        bottom = 8.dp
                    )
                )
            }

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    onPressRating: (Int) -> Unit
){
    var ratingState by remember {
        mutableStateOf(rating)
    }

    var selected by remember {
        mutableStateOf(false)
    }

    val size by animateDpAsState(
        targetValue = if (selected) 42.dp else 34.dp,
        spring(Spring.DampingRatioMediumBouncy), label = ""
    )

    Row (modifier = Modifier.width(280.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center){
        for (i in 1..5){
            Icon(painter = painterResource(id = R.drawable.ic_baseline_star_24),
                contentDescription = "star",
                modifier = Modifier
                    .width(size)
                    .height(size)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                onPressRating(i)
                                ratingState = i
                            }

                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if (i<= ratingState) Color(0xFFFFD700)
                        else Color(0xFFA2ADB1)
                )
        }
    }
}

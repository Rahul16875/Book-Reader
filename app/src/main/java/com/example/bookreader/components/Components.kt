package com.example.bookreader.components

import android.util.Log
import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.bookreader.R
import com.example.bookreader.model.MBook
import com.example.bookreader.navigation.ReaderScreens
import com.example.bookreader.screens.home.HomeScreenViewModel
import com.example.bookreader.screens.home.ReadingRightNowArea
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ReaderLogo(modifier: Modifier = Modifier
    .padding(bottom = 16.dp)) {
    Text(
        text = "A. Reader", style = MaterialTheme.typography.headlineLarge,
        color = Color.Red.copy(alpha = 0.5f),
        fontSize = 50.sp, fontWeight = FontWeight.SemiBold
    )
}


@Composable
fun EmailInput(modifier: Modifier = Modifier,
               emailState: MutableState<String>,
               labelId: String = "Email",
               enabled: Boolean = true,
               imeAction: ImeAction = ImeAction.Next,
               onAction: KeyboardActions = KeyboardActions.Default
){
    InputField(modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(modifier: Modifier = Modifier,
               valueState: MutableState<String>,
               labelId: String,
               enabled: Boolean,
               isSingleLine: Boolean = true,
               keyboardType: KeyboardType = KeyboardType.Text,
               imeAction: ImeAction = ImeAction.Next,
               onAction: KeyboardActions = KeyboardActions.Default
) {
    
    OutlinedTextField(
        value = valueState.value, onValueChange = { valueState.value = it},
        label = { Text(text = labelId)},
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth()
            ,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType= keyboardType, imeAction = imeAction),
        keyboardActions = onAction

    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInput(
    modifier: Modifier=Modifier,
    passwordState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions(),
) {

    val visualTransformation = if (passwordVisibility.value) VisualTransformation.None else
        PasswordVisualTransformation()

    OutlinedTextField(value = passwordState.value, onValueChange ={
        passwordState.value = it },
        label = { Text(text = labelId)},
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground),
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction),
        visualTransformation = visualTransformation,
        trailingIcon = {PasswordVisibility(passwordVisibility = passwordVisibility)},
        keyboardActions = onAction
    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value = !visible }) {
        Icons.Default.Close

    }
}

@Composable
fun TitleSection(modifier: Modifier = Modifier,
                 label: String){
    Surface(modifier = Modifier.padding(start = 5.dp, top = 1.dp)) {
        Column {
            Text(text = label,
                fontSize = 15.sp,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Left)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderAppBar(
    title: String,
    icon: ImageVector? = null,
    showProfile: Boolean = true,
    navController: NavController,
    onBackArrowClicked: () -> Unit = {}
){
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showProfile){
                    Image(painter = painterResource(id = R.drawable.bookicon), contentDescription = "Book Logo",
                        modifier = Modifier
                            .clip(CircleShape)
                            .scale(0.6f)
                            .size(50.dp), )
                }
                if (icon != null){
                    Icon(imageVector = icon, contentDescription = "arrow back",
                        tint = Color.Red.copy(alpha = 0.7f),
                        modifier = Modifier.clickable { onBackArrowClicked.invoke() })
                }
                Spacer(modifier = Modifier.width(40.dp))
                Text(text = title,
                    color = Color.Red.copy(alpha = 0.7f ),
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )


            }
        },
        actions = {
            IconButton(onClick = {
                FirebaseAuth.getInstance().signOut().run {
                    navController.navigate(ReaderScreens.LoginScreen.name)
                }
            }) {
                if (showProfile)
                Icon(imageVector = Icons.Default.Logout, contentDescription = "LogOut")
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(Color.Transparent)
    )
}

@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(onClick = { onTap() }, shape = RoundedCornerShape(50.dp),
        containerColor = Color(0xFF92CBDF)
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add a book", tint = Color.White)
    }

}

//@Composable
//fun HomeContent(navController: NavController, viewModel: HomeScreenViewModel){
//        var listOfbooks = emptyList<MBook>()
//        val currentUser = FirebaseAuth.getInstance().currentUser
//
//        if (viewModel.data.value.data.isNullOrEmpty()){
//            listOfbooks = viewModel.data.value.data!!.toList().filter {
//                it.userId == currentUser?.uid.toString()
//            }
//        }
//
////    val listOfbooks = listOf(
////        MBook(id = "dadfa", title = "Hello Again", authors = "Rahul Agrawal", notes = null),
////        MBook(id = "dafa", title = "Again", authors = "Rahul Agrawal", notes = null),
////        MBook(id = "dada", title = "Hello", authors = "Rahul Agrawal", notes = null),
////        MBook(id = "ddfa", title = "Hello Again", authors = "Rahul Agrawal", notes = null),
////        MBook(id = "dada", title = "Hello Again", authors = "Rahul Agrawal", notes = null)
////        )
//
//    val currentUserName = if (!FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty())
//        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) else
//        "N/A"
//
//    Column(modifier = Modifier.padding(2.dp),
//        verticalArrangement = Arrangement.Top) {
//        Row(modifier = Modifier.align(alignment = Alignment.Start)) {
//            TitleSection(label = "Your Reading \n" + " activity right now...")
//
//            Spacer(modifier = Modifier.fillMaxWidth(0.7f))
//            Column {
//                Icon(
//                    imageVector = Icons.Filled.AccountCircle,
//                    contentDescription = "Profile",
//                    modifier = Modifier
//                        .clickable {
//                            navController.navigate(ReaderScreens.ReaderStatsScreen.name)
//                        }
//                        .size(45.dp),
//                    tint = MaterialTheme.colorScheme.secondary)
//                Text(text = currentUserName!!,
//                    modifier = Modifier.padding(2.dp),
////                        style = MaterialTheme.typography)
//                    color = Color(0xFF25659D),
//                    fontSize = 15.sp,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis,
//                    fontWeight = FontWeight.SemiBold
//                )
//                Divider()
//            }
//        }
//        ReadingRightNowArea(books = listOf(), navController = navController)
//
//        TitleSection(label = "Reading List")
//        BookListArea(listofBooks = listOfbooks, navController = navController)
//
//    }
//}

@Composable
fun HomeContent(navController: NavController, viewModel: HomeScreenViewModel) {
    var listOfBooks = emptyList<MBook>()
    val currentUser = FirebaseAuth.getInstance().currentUser

    if (!viewModel.data.value.data.isNullOrEmpty()) {
        listOfBooks = viewModel.data.value.data!!.toList().filter { mBook ->
            mBook.userId == currentUser?.uid.toString()
        }

        Log.d("Books", "HomeContent: ${listOfBooks.toString()}")
    }

//    val listOfBooks = listOf(
//          MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes = null),
//        MBook(id = "dadfa", title = " Again", authors = "All of us", notes = null),
//        MBook(id = "dadfa", title = "Hello ", authors = "The world us", notes = null),
//        MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes = null),
//        MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes = null)
//                            )
    //me @gmail.com
    val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUserName = if (!email.isNullOrEmpty())
        FirebaseAuth.getInstance().currentUser?.email?.split("@")
            ?.get(0)else
        "N/A"
    Column(Modifier.padding(2.dp),
        verticalArrangement = Arrangement.Top) {
        Row(modifier = Modifier.align(alignment = Alignment.Start)) {
            TitleSection(label = "Your reading \n " + " activity right now...")
            Spacer(modifier = Modifier.fillMaxWidth(0.7f))
            Column {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(ReaderScreens.ReaderStatsScreen.name)
                        }
                        .size(45.dp),
                    tint = MaterialTheme.colorScheme.secondary)
                Text(text = currentUserName!!,
                    modifier = Modifier.padding(2.dp),
//                    style = MaterialTheme.typography.overline,
                    color = Color.Red,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Clip)
                Divider()
            }


        }

        ReadingRightNowArea(listOfBooks = listOfBooks,
            navController =navController )
        TitleSection(label = "Reading List")
        BookListArea(listOfBooks = listOfBooks,
            navController = navController)

    }

}

@Composable
fun BookListArea(listOfBooks: List<MBook>,
                 navController: NavController) {
    val addedBooks = listOfBooks.filter { mBook ->
        mBook.startedReading == null && mBook.finishedReading == null
    }



    HorizontalScrollableComponent(addedBooks){
        navController.navigate(ReaderScreens.UpdateScreen.name +"/$it")

    }



}

@Composable
fun HorizontalScrollableComponent(listofBooks: List<MBook>,
                                  viewModel: HomeScreenViewModel = hiltViewModel(),
                                  onCardPressed: (String) -> Unit) {

        val scrollState = rememberScrollState()
        Row (modifier = Modifier
            .fillMaxWidth()
            .heightIn(280.dp)
            .horizontalScroll(scrollState)){
            if (viewModel.data.value.loading == true){
                LinearProgressIndicator()
            }else{
                if (listofBooks.isNullOrEmpty()){
                    Surface(modifier = Modifier.padding(23.dp)) {
                        Text(text = "No books found, Add a Book",
                            color = Color.Red.copy(alpha = 0.4f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }else{
                    for (book in listofBooks){
                        ListCard(book){
                            onCardPressed(book.googleBookId.toString())

                        }
                    }
                }
            }
        }
}



@Composable
fun ListCard(book: MBook,
             onPressDetails: (String) -> Unit ={}){

    val context = LocalContext.current
    val resources = context.resources
    val displayMetrics = resources.displayMetrics

    val screenWidth = displayMetrics.widthPixels / displayMetrics.density

    val spacing = 10.dp
    Card(shape = RoundedCornerShape(29.dp),
        colors = CardDefaults.cardColors(Color.White) ,

        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .padding(16.dp)
            .height(260.dp)
            .width(202.dp)
            .clickable {
                onPressDetails.invoke(book.title.toString())
            }
    ) {
        Column(modifier = Modifier
            .width(screenWidth.dp - (spacing * 2))
            .fillMaxHeight(),
            horizontalAlignment = Alignment.Start) {
            Row (horizontalArrangement = Arrangement.Center){
                Image(painter = rememberImagePainter(data = book.photoUrl.toString()),
                    contentDescription = "book_image",
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(4.dp))
                Spacer(modifier = Modifier.width(50.dp))

                Column (modifier = Modifier.padding(top = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = "Fav_Icon",
                        modifier = Modifier.padding(bottom = 1.dp))

                    BookRating(score = book.rating!!)
                }
            }
            Text(text = book.title.toString(), modifier = Modifier.padding(5.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis)

            Text(text = book.authors.toString(), modifier = Modifier.padding(5.dp))

            val isStartedReading = remember {
                mutableStateOf(false)
            }

            Row (horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .weight(0.1f)
                    .padding(start = 112.dp)){

                isStartedReading.value = book.startedReading != null

                RoundedButton(label = if (isStartedReading.value) "Reading" else
                    "Not Yet", radius = 70)
            }
        }

    }
}

@Composable
fun BookRating(score: Double = 4.5) {
    Surface(modifier = Modifier
        .height(70.dp)
        .padding(4.dp)
        , shape = RoundedCornerShape(56.dp),
        shadowElevation = 6.dp,
        color = Color.White) {
        Column(modifier = Modifier.padding(4.dp),
//            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = Icons.Filled.StarBorder, contentDescription ="Start",
                modifier = Modifier.padding(3.dp))
            Text(text = score.toString())
        }
    }

}


@Composable
fun RoundedButton(
    label: String = "Reading",
    radius: Int = 29,
    onPress: () -> Unit = {}
){
    Surface(modifier = Modifier.clip(RoundedCornerShape(
        bottomEndPercent = radius,
        topStartPercent = radius)),
        color = Color(0xFF92CBDF)
    ) {

        Column(modifier = Modifier
            .width(90.dp)
            .heightIn(40.dp)
            .clickable { onPress.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, style = TextStyle(color = Color.White, fontSize = 15.sp))
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalComposeUiApi
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    onPressRating: (Int) -> Unit
) {
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

    Row(
        modifier = Modifier.width(280.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_star_24),
                contentDescription = "star",
                modifier = modifier
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
                tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }
    }
}
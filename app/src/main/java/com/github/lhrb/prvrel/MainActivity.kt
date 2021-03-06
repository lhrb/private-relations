package com.github.lhrb.prvrel

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.github.lhrb.prvrel.ui.theme.PrvrelTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "db"
        ).build()

        val personRepo: PersonRepository = PersonRepositoryImpl(db.dao())
        val itemRepo: ItemRepository = ItemRepositoryImpl(db.dao())

        setContent {
            PrvrelTheme {
                Column {
                    var persons = personRepo.getPersons().collectAsState(initial = emptyList())
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Routes.PERSONS) {
                        composable(Routes.PERSONS) {
                            personListView(
                                navController = navController,
                                persons = persons,
                                deletePerson = personRepo::deletePerson
                            )
                        }
                        composable(Routes.ADD_PERSON) {
                            addPersonView(
                                navController = navController,
                                addPerson = personRepo::insertPerson
                            )
                        }
                        composable(
                            Routes.ITEMS + "/{personId}",
                            arguments = listOf(navArgument("personId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            backStackEntry.arguments?.let {
                                items(
                                    personId = it.getInt("personId"),
                                    itemRepository = itemRepo,
                                    navController = navController
                                )
                            }
                        }
                        composable(
                            Routes.ADD_ITEM + "/{personId}",
                            arguments = listOf(navArgument("personId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            backStackEntry.arguments?.let {
                                addItemView(
                                    personId = it.getInt("personId"),
                                    addItem = itemRepo::insertItem,
                                    navController = navController
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}


@Composable
fun actionButton(text: String, onClick: () -> Unit) {
    FloatingActionButton(
        modifier = Modifier
            .padding(24.dp),
        onClick = onClick
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun topBar(title: String, onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp),
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                    contentDescription = "arrow back"
                )
            }
        }
    )
}

@Preview
@Composable
fun preview() {
    Scaffold(
        topBar = { topBar(title = "Private Relations", onBackClick = {}) }
    ) {}
}


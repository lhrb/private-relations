package com.github.lhrb.prvrel

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
fun topBar(title: String) {
    TopAppBar(title = {
        Text(
            text = title,
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(4.dp)
        )
    })
}


package com.github.lhrb.prvrel

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun personTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "Private Relations",
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp),
            )
        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_share_24),
                    contentDescription = "arrow back"
                )
            }
        }
    )
}

@Composable
fun personListView(
    persons: State<List<Person>>,
    navController: NavController,
    deletePerson: (Person) -> Unit
) {
    Scaffold(
        topBar = { personTopBar() },
        floatingActionButton = {
            actionButton(text = "+ Person") {
                navController.navigate(Routes.ADD_PERSON)
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
                items(items = persons.value) { person ->
                    personView(
                        person = person,
                        deletePerson = deletePerson,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun personView(
    person: Person,
    deletePerson: (Person) -> Unit,
    navController: NavController
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { navController.navigate(Routes.ITEMS + "/" + person.id) },
                    onLongPress = { expanded = true }
                )
            }) {
            Text(
                text = person.firstName,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(16.dp)
            )
        }
        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(onClick = {
                deletePerson.invoke(person)
                expanded = false
            }) {
                Text("Delete")
            }
        }
    }
}

@Composable
fun addPersonView(
    navController: NavController,
    addPerson: (Person) -> Unit
) {
    val activity = LocalContext.current as AppCompatActivity
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            topBar(
                title = "Add Person",
                onBackClick = { navController.navigate(Routes.PERSONS) })
        }
    ) {
        Column {

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
            Box {
                OutlinedTextField(
                    value = birthDate,
                    onValueChange = {},
                    label = { Text("Birthday") },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )
                Box(modifier = Modifier
                    .matchParentSize()
                    .alpha(0f)
                    .clickable {
                        showDatePicker(activity = activity) {
                            it?.let {
                                val date = Date(it)
                                birthDate = dateFormat.format(date)
                            }
                        }
                    }
                )
            }
            Button(
                onClick = {
                    addPerson.invoke(
                        Person(
                            firstName = firstName,
                            lastName = lastName,
                            birthday = birthDate
                        )
                    )
                    navController.navigate(Routes.PERSONS)
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "save", style = MaterialTheme.typography.h5)
            }
        }
    }
}

private fun showDatePicker(
    activity: AppCompatActivity,
    updatedDate: (Long?) -> Unit
) {
    val picker = MaterialDatePicker.Builder.datePicker().build()
    picker.show(activity.supportFragmentManager, picker.toString())
    picker.addOnPositiveButtonClickListener {
        updatedDate(it)
    }
}
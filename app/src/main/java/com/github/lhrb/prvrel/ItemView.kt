package com.github.lhrb.prvrel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun items(
    personId: Int,
    itemRepository: ItemRepository,
    navController: NavController
) {
    var items = itemRepository.getItems(personId = personId).collectAsState(initial = emptyList())
    itemListView(personId = personId, items = items, navController = navController)
}

@Composable
fun itemListView(
    personId: Int,
    items: State<List<Item>>,
    navController: NavController
) {
    Scaffold(
        topBar = { topBar(title = "Notes") }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
                items(items = items.value) { item ->
                    itemView(item = item)
                }
            }
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(80.dp),
                onClick = { navController.navigate(Routes.ADD_ITEM + "/" + personId) }) {
                Text(text = "+", style = MaterialTheme.typography.h4)
            }
        }
    }
}

@Composable
fun itemView(
    item: Item
) {
    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(
                text = item.content,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}

@Composable
fun addItemView(
    personId: Int,
    addItem: (Item) -> Unit,
    navController: NavController
) {
    var content by remember { mutableStateOf("") }
    Scaffold(
        topBar = { topBar(title = "Add Item") }
    ) {
        Column {
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
            Button(
                onClick = {
                    addItem.invoke(Item(personId = personId, content = content))
                    navController.navigate(Routes.ITEMS + "/" + personId)
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
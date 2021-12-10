package com.github.lhrb.prvrel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

interface ItemRepository {
    fun insertItem(item: Item)
    fun getItems(personId: Int): Flow<List<Item>>
}

class ItemRepositoryImpl(val dao: Dao) : ItemRepository {
    override fun insertItem(item: Item) {
        CoroutineScope(Dispatchers.IO).launch { dao.insertItem(item = item) }
    }

    override fun getItems(personId: Int): Flow<List<Item>> {
        return dao.getItems(personId = personId)
    }
}
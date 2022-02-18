package net.fearlessplus.ui.main.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.fearlessplus.model.ChartModel
import net.fearlessplus.model.EventModel
import net.fearlessplus.network.Api
import net.fearlessplus.network.send
import kotlinx.coroutines.*

class EventViewModel : ViewModel() {

    var content = MutableLiveData<EventModel>()
    fun getContent(): LiveData<EventModel> {
        val job = Job()
        val scope = CoroutineScope(Dispatchers.Default + job)
        scope.launch {
            withContext(Dispatchers.IO) {
                Api.service.getEventList().send({
                    content.value = it
                })
            }
        }
        return content
    }
}

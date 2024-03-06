package com.ddangddangddang.data.remote.scarlet

import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.retry.ExponentialWithJitterBackoffStrategy
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import okhttp3.OkHttpClient

class ScarletFactory {

    companion object {
        private const val BASE_DURATION = 1000L
        private const val MAX_DURATION = 10000L

        fun createInstance(serverURL: String, httpClient: OkHttpClient, lifecycle: Lifecycle): Scarlet {
            return Scarlet.Builder()
                .webSocketFactory(httpClient.newWebSocketFactory(serverURL))
                .lifecycle(lifecycle)
                .backoffStrategy(ExponentialWithJitterBackoffStrategy(BASE_DURATION, MAX_DURATION))
                .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
                .addMessageAdapterFactory(GsonMessageAdapter.Factory())
                .build()
        }
    }
}

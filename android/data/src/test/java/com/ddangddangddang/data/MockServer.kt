package com.ddangddangddang.data

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

object MockServer {
    val server = MockWebServer()
    private val dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val path = request.path ?: return MockResponse().setResponseCode(404)
            return when {
                path == "/auctions" && request.method == "GET" -> {
                    MockResponse()
                        .setHeader("Content-Type", "application/json")
                        .setResponseCode(200)
                        .setBody(auctionPreviewsResponse)
                }

                path == "/auctions" && request.method == "POST" -> {
                    MockResponse()
                        .setHeader("Content-Type", "application/json")
                        .setResponseCode(200)
                        .setBody(registerAuctionResponse)
                }
                else -> {
                    MockResponse().setResponseCode(404)
                }
            }
        }
    }

    private val auctionPreviewsResponse: String =
        """
            {
                "auctions" : [
                    {
                        "id" : 1,
                        "title" : "맥북 2021 13인치",
                        "image" : "https://www.apple.com/newsroom/images/product/mac/standard/Apple_MacBook-Pro_14-16-inch_10182021_big.jpg.large.jpg",
                        "auctionPrice" : 1000000,
                        "status" : "FAILURE",
                        "auctioneerCount" : 0
                    }
                ],
                "lastAuctionId" : 1
            }
        """.trimIndent()

    private val registerAuctionResponse: String =
        """
            {
                "id": 2
            }
        """.trimIndent()

    init {
        server.dispatcher = dispatcher
        server.start()
    }
}

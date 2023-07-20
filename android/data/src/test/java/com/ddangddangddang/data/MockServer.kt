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

                path.contains("/auctions") && path.split("/").size > 1 && request.method == "GET" -> {
                    MockResponse()
                        .setHeader("Content-Type", "application/json")
                        .setResponseCode(200)
                        .setBody(auctionDetailResponse)
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

    private val auctionDetailResponse: String =
        """
            {
                "auction":
                    {
                        "id": 2,
                        "images": [ "https://www.apple.com/newsroom/images/product/mac/standard/Apple_MacBook-Pro_14-16-inch_10182021_big.jpg.large.jpg" ],
                        "title": "맥북 16인치",
                        "category":
                            {
                                "main": "전자기기",
                                "sub": "노트북"
                            },
                        "description": "맥북 2019 16인치 팝니다. 급전이 필요해요...",
                        "startPrice": 900000,
                        "lastBidPrice": 100000,
                        "status": "UNBIDDEN",
                        "bidUnit": 3000,
                        "registerTime": "2023-07-20T16:38:28",
                        "closingTime": "2023-07-25T16:38:28",
                        "directRegions": [
                            {
                                "first": "서울특별시",
                                "second": "관악구",
                                "third": "성현동"
                            }
                        ],
                        "auctioneerCount": 0
                    },
                "seller":
                    {
                        "id": 1,
                        "image": "https://img1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/7r5X/image/9djEiPBPMLu_IvCYyvRPwmZkM1g.jpg",
                        "nickname": "닉네임",
                        "reliability": 5.0
                    }
            }
        """.trimIndent()

    init {
        server.dispatcher = dispatcher
        server.start()
    }
}

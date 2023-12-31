// package com.ddangddangddang.data
//
// import com.ddangddangddang.data.remote.AuctionService
// import com.ddangddangddang.data.repository.AuctionRepository
// import com.ddangddangddang.data.repository.AuctionRepositoryImpl
// import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
// import kotlinx.coroutines.ExperimentalCoroutinesApi
// import kotlinx.coroutines.test.runTest
// import kotlinx.serialization.json.Json
// import okhttp3.MediaType.Companion.toMediaType
// import org.junit.Assert.assertEquals
// import org.junit.Test
// import retrofit2.Retrofit
//
// class AuctionRepositoryImplTest {
//     private val service: AuctionService = Retrofit.Builder()
//         .baseUrl(MockServer.server.url(""))
//         .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
//         .build()
//         .create(AuctionService::class.java)
//     private val repository: AuctionRepository = AuctionRepositoryImpl.getInstance(service)
//
//     @OptIn(ExperimentalCoroutinesApi::class)
//     @Test
//     fun getAuctionPreviews() = runTest {
//         // given
//
//         // when
//         val actual = repository.getAuctionPreviews()
//
//         // then
//         val expected = createAuctionPreviewsResponse()
//         assertEquals(expected, actual)
//     }
//
//     @OptIn(ExperimentalCoroutinesApi::class)
//     @Test
//     fun registerAuction() = runTest {
//         // given
//
//         // when
//         val actual = repository.registerAuction(createRegisterAuctionRequest())
//
//         // then
//         val expected = createRegisterAuctionResponse()
//         assertEquals(expected, actual)
//     }
//
//     @OptIn(ExperimentalCoroutinesApi::class)
//     @Test
//     fun getAuctionDetail() = runTest {
//         // given
//
//         // when
//         val actual = repository.getAuctionDetail(2)
//
//         // then
//         val expected = createAuctionDetailResponse()
//         assertEquals(expected, actual)
//     }
// }

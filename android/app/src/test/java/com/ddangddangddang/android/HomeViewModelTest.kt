package com.ddangddangddang.android

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.ddangddangddang.android.feature.home.HomeViewModel
import com.ddangddangddang.android.model.AuctionHomeModel
import com.ddangddangddang.data.model.response.AuctionPreviewResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuctionRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {
    private val repository: AuctionRepository = mockk()
    private lateinit var viewModel: HomeViewModel
    private lateinit var cache: MutableLiveData<List<AuctionPreviewResponse>>

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        cache = MutableLiveData()
        coEvery { repository.observeAuctionPreviews() } returns cache

        viewModel = HomeViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `경매_목록을_불러오면_경매_상품과_마지막_경매_상품_아이디_값을_가져온다`() = runTest {
        // given
        val auctionPreviewsResponse = createAuctionPreviewsResponse()
        coEvery { repository.getAuctionPreviews() } answers {
            cache.value = auctionPreviewsResponse.auctions
            ApiResponse.Success(auctionPreviewsResponse)
        }

        // when
        viewModel.loadAuctions()

        // then
        viewModel.auctions.getOrAwaitValue()
        val expectedAuctions: List<AuctionHomeModel> = listOf(createAuctionHomeModel())
        assertEquals(expectedAuctions, viewModel.auctions.value)
    }

    @Test
    fun `아이디가_1인_경매_상품의_상세로_이동하면_아이디가_1인_경매_상세_이벤트가_설정된다`() {
        // given

        // when
        viewModel.navigateToAuctionDetail(1)

        // then
        assertEquals(HomeViewModel.HomeEvent.NavigateToAuctionDetail(1), viewModel.event.value)
    }

    @Test
    fun `경매_등록으로_이동하면_경매_등록_이벤트가_설정된다`() {
        // given

        // when
        viewModel.navigateToRegisterAuction()

        // then
        assertEquals(HomeViewModel.HomeEvent.NavigateToRegisterAuction, viewModel.event.value)
    }
}

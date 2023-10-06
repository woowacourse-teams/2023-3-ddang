package com.ddangddangddang.android

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ddangddangddang.android.feature.detail.AuctionDetailViewModel
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuctionRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AuctionDetailVIewModelTest {
    private lateinit var viewModel: AuctionDetailViewModel
    private lateinit var repository: AuctionRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        repository = mockk()
        viewModel = AuctionDetailViewModel(repository)
    }

    @Test
    fun `loadAuctionDetail(auctionId=1)_auctionId가_1인_경매_상품의_상세_정보를_가져온다`() {
        // given
        coEvery { repository.getAuctionDetail(1L) } returns ApiResponse.Success(createAuctionDetailResponse())

        // when
        viewModel.loadAuctionDetail(1L)

        // then
        val expected = createAuctionDetailModel()
        val actual = viewModel.auctionDetailModel.value
        assertEquals(expected, actual)
    }
}

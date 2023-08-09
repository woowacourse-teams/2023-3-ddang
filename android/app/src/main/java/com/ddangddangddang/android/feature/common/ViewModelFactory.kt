package com.ddangddangddang.android.feature.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.ddangddangddang.android.feature.detail.AuctionDetailViewModel
import com.ddangddangddang.android.feature.detail.bid.AuctionBidViewModel
import com.ddangddangddang.android.feature.home.HomeViewModel
import com.ddangddangddang.android.feature.login.LoginViewModel
import com.ddangddangddang.android.feature.main.MainViewModel
import com.ddangddangddang.android.feature.register.RegisterAuctionViewModel
import com.ddangddangddang.android.feature.register.category.SelectCategoryViewModel
import com.ddangddangddang.android.feature.register.region.SelectRegionsViewModel
import com.ddangddangddang.data.remote.AuctionRetrofit
import com.ddangddangddang.data.repository.AuctionRepositoryImpl
import com.ddangddangddang.data.repository.CategoryRepositoryImpl
import com.ddangddangddang.data.repository.ChatRepositoryImpl
import com.ddangddangddang.data.repository.RegionRepositoryImpl

val repository = AuctionRepositoryImpl.getInstance(AuctionRetrofit.getInstance().service)
val categoryRepository = CategoryRepositoryImpl.getInstance(AuctionRetrofit.getInstance().service)
val regionRepository = RegionRepositoryImpl.getInstance(AuctionRetrofit.getInstance().service)
val chatRepository = ChatRepositoryImpl.getInstance(AuctionRetrofit.getInstance().service)

@Suppress("UNCHECKED_CAST")
val viewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        with(modelClass) {
            // 레포지토리 싱글톤 객체 얻어옴
            when {
                isAssignableFrom(MainViewModel::class.java) -> MainViewModel()
                isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository)
                isAssignableFrom(AuctionDetailViewModel::class.java) -> AuctionDetailViewModel(
                    repository,
                    chatRepository,
                )
                isAssignableFrom(RegisterAuctionViewModel::class.java) -> RegisterAuctionViewModel(repository)
                isAssignableFrom(AuctionBidViewModel::class.java) -> AuctionBidViewModel(repository)
                isAssignableFrom(SelectCategoryViewModel::class.java) -> SelectCategoryViewModel(categoryRepository)
                isAssignableFrom(SelectRegionsViewModel::class.java) -> SelectRegionsViewModel(regionRepository)
                isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel()
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}

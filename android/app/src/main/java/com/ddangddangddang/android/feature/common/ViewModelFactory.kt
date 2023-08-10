package com.ddangddangddang.android.feature.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.ddangddangddang.android.feature.detail.AuctionDetailViewModel
import com.ddangddangddang.android.feature.detail.bid.AuctionBidViewModel
import com.ddangddangddang.android.feature.home.HomeViewModel
import com.ddangddangddang.android.feature.login.LoginViewModel
import com.ddangddangddang.android.feature.main.MainViewModel
import com.ddangddangddang.android.feature.mypage.MyPageViewModel
import com.ddangddangddang.android.feature.register.RegisterAuctionViewModel
import com.ddangddangddang.android.feature.register.category.SelectCategoryViewModel
import com.ddangddangddang.android.feature.register.region.SelectRegionsViewModel
import com.ddangddangddang.android.feature.splash.SplashViewModel
import com.ddangddangddang.android.global.DdangDdangDdang
import com.ddangddangddang.data.repository.AuctionRepositoryImpl
import com.ddangddangddang.data.repository.CategoryRepositoryImpl
import com.ddangddangddang.data.repository.ChatRepositoryImpl
import com.ddangddangddang.data.repository.RegionRepositoryImpl
import com.ddangddangddang.data.repository.UserRepositoryImpl

val auctionRepository = AuctionRepositoryImpl.getInstance(DdangDdangDdang.auctionRetrofit.service)
val categoryRepository = CategoryRepositoryImpl.getInstance(DdangDdangDdang.auctionRetrofit.service)
val regionRepository = RegionRepositoryImpl.getInstance(DdangDdangDdang.auctionRetrofit.service)
val chatRepository = ChatRepositoryImpl.getInstance(DdangDdangDdang.auctionRetrofit.service)
val userRepository = UserRepositoryImpl.getInstance(DdangDdangDdang.auctionRetrofit.service)

@Suppress("UNCHECKED_CAST")
val viewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        with(modelClass) {
            // 레포지토리 싱글톤 객체 얻어옴
            when {
                isAssignableFrom(MainViewModel::class.java) -> MainViewModel()
                isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(auctionRepository)
                isAssignableFrom(AuctionDetailViewModel::class.java) -> AuctionDetailViewModel(
                    auctionRepository,
                    chatRepository,
                )

                isAssignableFrom(RegisterAuctionViewModel::class.java) -> RegisterAuctionViewModel(
                    auctionRepository,
                )

                isAssignableFrom(AuctionBidViewModel::class.java) -> AuctionBidViewModel(
                    auctionRepository,
                )

                isAssignableFrom(SelectCategoryViewModel::class.java) -> SelectCategoryViewModel(
                    categoryRepository,
                )

                isAssignableFrom(SelectRegionsViewModel::class.java) -> SelectRegionsViewModel(
                    regionRepository,
                )

                isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(DdangDdangDdang.authRepository)
                isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel(DdangDdangDdang.authRepository)
                isAssignableFrom(MyPageViewModel::class.java) -> MyPageViewModel(userRepository)
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}

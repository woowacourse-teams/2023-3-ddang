package com.ddangddangddang.android.di

import com.ddangddangddang.data.repository.AuctionRepository
import com.ddangddangddang.data.repository.AuctionRepositoryImpl
import com.ddangddangddang.data.repository.AuthRepository
import com.ddangddangddang.data.repository.AuthRepositoryImpl
import com.ddangddangddang.data.repository.CategoryRepository
import com.ddangddangddang.data.repository.CategoryRepositoryImpl
import com.ddangddangddang.data.repository.ChatRepository
import com.ddangddangddang.data.repository.ChatRepositoryImpl
import com.ddangddangddang.data.repository.RegionRepository
import com.ddangddangddang.data.repository.RegionRepositoryImpl
import com.ddangddangddang.data.repository.ReviewRepository
import com.ddangddangddang.data.repository.ReviewRepositoryImpl
import com.ddangddangddang.data.repository.UserRepository
import com.ddangddangddang.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindAuctionRepository(auctionRepository: AuctionRepositoryImpl): AuctionRepository

    @Singleton
    @Binds
    abstract fun bindAuthRepository(authRepository: AuthRepositoryImpl): AuthRepository

    @Singleton
    @Binds
    abstract fun bindCategoryRepository(categoryRepository: CategoryRepositoryImpl): CategoryRepository

    @Singleton
    @Binds
    abstract fun bindChatRepository(chatRepository: ChatRepositoryImpl): ChatRepository

    @Singleton
    @Binds
    abstract fun bindRegionRepository(regionRepository: RegionRepositoryImpl): RegionRepository

    @Singleton
    @Binds
    abstract fun bindUserRepository(userRepository: UserRepositoryImpl): UserRepository

    @Singleton
    @Binds
    abstract fun bindReviewRepository(reviewRepository: ReviewRepositoryImpl): ReviewRepository
}

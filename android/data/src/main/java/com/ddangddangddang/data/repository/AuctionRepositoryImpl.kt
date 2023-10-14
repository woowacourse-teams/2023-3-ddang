package com.ddangddangddang.data.repository

import androidx.lifecycle.LiveData
import com.ddangddangddang.data.datasource.AuctionLocalDataSource
import com.ddangddangddang.data.datasource.AuctionRemoteDataSource
import com.ddangddangddang.data.model.SortType
import com.ddangddangddang.data.model.request.AuctionBidRequest
import com.ddangddangddang.data.model.request.RegisterAnswerRequest
import com.ddangddangddang.data.model.request.RegisterAuctionRequest
import com.ddangddangddang.data.model.request.RegisterQuestionRequest
import com.ddangddangddang.data.model.request.ReportAnswerRequest
import com.ddangddangddang.data.model.request.ReportAuctionArticleRequest
import com.ddangddangddang.data.model.request.ReportMessageRoomRequest
import com.ddangddangddang.data.model.request.ReportQuestionRequest
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import com.ddangddangddang.data.model.response.AuctionPreviewResponse
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.model.response.QnaResponse
import com.ddangddangddang.data.remote.ApiResponse
import java.io.File
import javax.inject.Inject

class AuctionRepositoryImpl @Inject constructor(
    private val localDataSource: AuctionLocalDataSource,
    private val remoteDataSource: AuctionRemoteDataSource,
) : AuctionRepository {

    override fun observeAuctionPreviews(): LiveData<List<AuctionPreviewResponse>> {
        return localDataSource.observeAuctionPreviews()
    }

    override suspend fun getAuctionPreviews(
        page: Int,
        size: Int?,
        sortType: SortType?,
        title: String?,
    ): ApiResponse<AuctionPreviewsResponse> {
        val response = remoteDataSource.getAuctionPreviews(page, size, sortType, title)
        if (response is ApiResponse.Success) {
            if (page == 1) localDataSource.clearAuctionPreviews()
            localDataSource.addAuctionPreviews(response.body.auctions)
        }
        return response
    }

    override suspend fun getAuctionPreviewsByTitle(
        page: Int,
        size: Int?,
        title: String,
    ): ApiResponse<AuctionPreviewsResponse> {
        return remoteDataSource.getAuctionPreviewsByTitle(page = page, size = size, title = title)
    }

    override suspend fun getAuctionDetail(id: Long): ApiResponse<AuctionDetailResponse> {
        val response = remoteDataSource.getAuctionDetail(id)
        if (response is ApiResponse.Success) {
            localDataSource.updateAuctionPreview(response.body)
        }
        if (response is ApiResponse.Failure && response.responseCode == 404) {
            localDataSource.removeAuctionPreview(id)
        }
        return response
    }

    override suspend fun registerAuction(
        images: List<File>,
        auction: RegisterAuctionRequest,
    ): ApiResponse<AuctionPreviewResponse> {
        val response = remoteDataSource.registerAuction(images, auction)
        if (response is ApiResponse.Success) {
            localDataSource.addAuctionPreview(response.body)
        }
        return response
    }

    override suspend fun submitAuctionBid(
        auctionId: Long,
        bidPrice: Int,
    ): ApiResponse<Unit> {
        return remoteDataSource.submitAuctionBid(AuctionBidRequest(auctionId, bidPrice))
    }

    override suspend fun reportAuction(auctionId: Long, description: String): ApiResponse<Unit> {
        return remoteDataSource.reportAuction(ReportAuctionArticleRequest(auctionId, description))
    }

    override suspend fun reportMessageRoom(roomId: Long, description: String): ApiResponse<Unit> {
        return remoteDataSource.reportMessageRoom(ReportMessageRoomRequest(roomId, description))
    }

    override suspend fun deleteAuction(auctionId: Long): ApiResponse<Unit> {
        val response = remoteDataSource.deleteAuction(auctionId)
        if (response is ApiResponse.Success) localDataSource.removeAuctionPreview(auctionId)
        return response
    }

    override suspend fun getAuctionQnas(auctionId: Long): ApiResponse<QnaResponse> {
        return remoteDataSource.getAuctionQnas(auctionId)
    }

    override suspend fun registerQuestion(registerQuestionRequest: RegisterQuestionRequest): ApiResponse<Unit> {
        return remoteDataSource.registerQuestion(registerQuestionRequest)
    }

    override suspend fun registerAnswer(
        questionId: Long,
        registerAnswerRequest: RegisterAnswerRequest,
    ): ApiResponse<Unit> {
        return remoteDataSource.registerAnswer(questionId, registerAnswerRequest)
    }

    override suspend fun deleteQuestion(questionId: Long): ApiResponse<Unit> {
        return remoteDataSource.deleteQuestion(questionId)
    }

    override suspend fun deleteAnswer(answerId: Long): ApiResponse<Unit> {
        return remoteDataSource.deleteAnswer(answerId)
    }

    override suspend fun reportQuestion(reportQuestionRequest: ReportQuestionRequest): ApiResponse<Unit> {
        return remoteDataSource.reportQuestion(reportQuestionRequest)
    }

    override suspend fun reportAnswer(reportAnswerRequest: ReportAnswerRequest): ApiResponse<Unit> {
        return remoteDataSource.reportAnswer(reportAnswerRequest)
    }
}

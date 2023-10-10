package com.ddangddangddang.android.feature.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityAuctionDetailBinding
import com.ddangddangddang.android.feature.common.notifyFailureMessage
import com.ddangddangddang.android.feature.detail.bid.AuctionBidDialog
import com.ddangddangddang.android.feature.imageDetail.ImageDetailActivity
import com.ddangddangddang.android.feature.messageRoom.MessageRoomActivity
import com.ddangddangddang.android.feature.report.ReportActivity
import com.ddangddangddang.android.model.RegionModel
import com.ddangddangddang.android.model.ReportType
import com.ddangddangddang.android.notification.NotificationType
import com.ddangddangddang.android.notification.cancelActiveNotification
import com.ddangddangddang.android.util.binding.BindingActivity
import com.ddangddangddang.android.util.view.Toaster
import com.ddangddangddang.android.util.view.observeLoadingWithDialog
import com.ddangddangddang.android.util.view.showDialog
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuctionDetailActivity :
    BindingActivity<ActivityAuctionDetailBinding>(R.layout.activity_auction_detail) {
    private val viewModel: AuctionDetailViewModel by viewModels()
    private val auctionId: Long by lazy { intent.getLongExtra(AUCTION_ID_KEY, -1L) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        setupViewModel()

        if (savedInstanceState == null) viewModel.loadAuctionDetail(auctionId)
    }

    private fun setupViewModel() {
        observeLoadingWithDialog(
            this,
            viewModel.isLoadingWithAnimation,
            binding.clAuctionDetailContainer,
        )
        viewModel.event.observe(this) { event ->
            handleEvent(event)
        }
        viewModel.auctionDetailModel.observe(this) {
            setupAuctionImages(it.images)
            setupDirectRegions(it.directRegions)
        }
    }

    private fun handleEvent(event: AuctionDetailViewModel.AuctionDetailEvent) {
        when (event) {
            is AuctionDetailViewModel.AuctionDetailEvent.Exit -> finish()
            is AuctionDetailViewModel.AuctionDetailEvent.PopupAuctionBid -> showAuctionBidDialog()
            is AuctionDetailViewModel.AuctionDetailEvent.EnterMessageRoom -> navigateToMessageRoom(
                event.roomId,
            )

            is AuctionDetailViewModel.AuctionDetailEvent.ReportAuction -> navigateToReport(event.auctionId)
            is AuctionDetailViewModel.AuctionDetailEvent.NavigateToImageDetail -> {
                navigateToImageDetail(event.images, event.focusPosition)
            }

            is AuctionDetailViewModel.AuctionDetailEvent.NotifyAuctionDoesNotExist -> notifyAuctionDoesNotExist()
            AuctionDetailViewModel.AuctionDetailEvent.DeleteAuction -> askDeletion()
            AuctionDetailViewModel.AuctionDetailEvent.NotifyAuctionDeletionComplete -> notifyDeleteComplete()
            is AuctionDetailViewModel.AuctionDetailEvent.AuctionDeleteFailure -> {
                notifyFailureMessage(event.error, R.string.detail_auction_delete_failure)
            }

            is AuctionDetailViewModel.AuctionDetailEvent.AuctionLoadFailure -> {
                notifyFailureMessage(event.error, R.string.detail_auction_load_failure)
            }

            is AuctionDetailViewModel.AuctionDetailEvent.EnterChatLoadFailure -> {
                notifyFailureMessage(event.error, R.string.detail_auction_enter_chat_room_failure)
            }
        }
    }

    private fun showAuctionBidDialog() {
        AuctionBidDialog.show(supportFragmentManager)
    }

    private fun navigateToMessageRoom(roomId: Long) {
        val intent = MessageRoomActivity.getIntent(this, roomId)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP + Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun navigateToReport(auctionId: Long) {
        startActivity(ReportActivity.getIntent(this, ReportType.ArticleReport.ordinal, auctionId))
    }

    private fun navigateToImageDetail(images: List<String>, focusPosition: Int) {
        startActivity(
            ImageDetailActivity.getIntent(this@AuctionDetailActivity, images, focusPosition),
        )
    }

    private fun notifyAuctionDoesNotExist() {
        showDialog(
            messageId = R.string.detail_auction_dialog_auction_does_not_exist_message,
            actionPositive = { finish() },
            isCancelable = false,
        )
    }

    private fun askDeletion() {
        showDialog(
            messageId = R.string.auction_detail_dialog_check_delete_message,
            negativeStringId = R.string.all_dialog_default_negative_button,
            actionPositive = { viewModel.deleteAuction() },
        )
    }

    private fun notifyDeleteComplete() {
        Toaster.showShort(this, getString(R.string.auction_detail_delete_complete))
        finish()
    }

    private fun setupAuctionImages(images: List<String>) {
        binding.vpImageList.apply {
            offscreenPageLimit = 3
            setSideVisiblePageTransformer()
            adapter = AuctionImageAdapter(images) { viewModel.navigateToImageDetail(it) }
        }

        TabLayoutMediator(binding.tlIndicator, binding.vpImageList) { _, _ -> }.attach()
    }

    private fun ViewPager2.setSideVisiblePageTransformer() {
        val pageMarginPx =
            resources.getDimensionPixelOffset(R.dimen.auction_detail_image_page_margin)
        val offsetPx = resources.getDimensionPixelOffset(R.dimen.auction_detail_image_page_offset)
        setPageTransformer { page, position ->
            val offset = position * (-2 * pageMarginPx + offsetPx)
            page.translationX = offset
        }
    }

    private fun setupDirectRegions(regions: List<RegionModel>) {
        binding.rvDirectExchangeRegions.adapter = AuctionDirectRegionsAdapter(regions)
    }

    override fun onResume() {
        super.onResume()
        cancelNotification()
    }

    private fun cancelNotification() {
        cancelActiveNotification(NotificationType.BID.name, auctionId.toInt())
    }

    companion object {
        private const val AUCTION_ID_KEY = "auction_id_key"

        fun getIntent(context: Context, auctionId: Long): Intent {
            return Intent(context, AuctionDetailActivity::class.java).apply {
                putExtra(AUCTION_ID_KEY, auctionId)
            }
        }
    }
}

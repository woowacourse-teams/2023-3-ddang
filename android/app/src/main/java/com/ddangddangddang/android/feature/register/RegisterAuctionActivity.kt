package com.ddangddangddang.android.feature.register

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityRegisterAuctionBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.feature.detail.AuctionDetailActivity
import com.ddangddangddang.android.feature.register.category.SelectCategoryActivity
import com.ddangddangddang.android.feature.register.region.SelectRegionsActivity
import com.ddangddangddang.android.global.AnalyticsDelegate
import com.ddangddangddang.android.global.AnalyticsDelegateImpl
import com.ddangddangddang.android.model.CategoryModel
import com.ddangddangddang.android.model.RegionSelectionModel
import com.ddangddangddang.android.model.RegisterImageModel
import com.ddangddangddang.android.util.binding.BindingActivity
import com.ddangddangddang.android.util.compat.getParcelableCompat
import com.ddangddangddang.android.util.compat.getSerializableExtraCompat
import com.ddangddangddang.android.util.view.showDialog
import com.ddangddangddang.android.util.view.showSnackbar
import java.time.LocalDateTime
import java.time.LocalTime

class RegisterAuctionActivity :
    BindingActivity<ActivityRegisterAuctionBinding>(R.layout.activity_register_auction),
    AnalyticsDelegate by AnalyticsDelegateImpl() {
    private val viewModel by viewModels<RegisterAuctionViewModel> { viewModelFactory }
    private val imageAdapter = RegisterAuctionImageAdapter { viewModel.setDeleteImageEvent(it) }
    private val pickMultipleMediaLaunchers = setupMultipleMediaLaunchers()
    private val categoryActivityLauncher = setupCategoryLauncher()
    private val regionActivityLauncher = setupRegionLauncher()
    private val startPriceWatcher by lazy { DefaultTextWatcher(viewModel::setStartPrice) }
    private val bidUnitWatcher by lazy { DefaultTextWatcher(viewModel::setBidUnit) }

    private fun setupMultipleMediaLaunchers(): List<ActivityResultLauncher<PickVisualMediaRequest>> {
        return List(RegisterAuctionViewModel.MAXIMUM_IMAGE_SIZE) { index ->
            if (index == 0) {
                registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                    if (uri != null) {
                        val image = RegisterImageModel(uri = uri)
                        viewModel.addImages(listOf(image))
                    }
                }
            } else {
                registerForActivityResult(
                    ActivityResultContracts.PickMultipleVisualMedia(index + 1),
                ) { uris ->
                    if (uris.isNotEmpty()) {
                        val images = uris.map { uri -> RegisterImageModel(uri = uri) }
                        viewModel.addImages(images)
                    }
                }
            }
        }
    }

    private fun setupCategoryLauncher(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val category = it.data?.getParcelableCompat<CategoryModel>(CATEGORY_RESULT)
                    ?: return@registerForActivityResult
                viewModel.setCategory(category)
            }
        }
    }

    private fun setupRegionLauncher(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val regions =
                    it.data?.getSerializableExtraCompat<Array<RegionSelectionModel>>(REGIONS_RESULT)
                        ?: return@registerForActivityResult
                viewModel.setRegion(regions.toList())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerAnalytics(javaClass.simpleName, lifecycle)

        binding.viewModel = viewModel
        setupViewModel()
        setupLinearLayoutRegisterImage()
        setupImageRecyclerView()
        setupStartPriceTextWatcher()
        setupBidUnitTextWatcher()
    }

    private fun setupViewModel() {
        viewModel.images.observe(this) { imageAdapter.setImages(it) }
        viewModel.event.observe(this) { handleEvent(it) }
        viewModel.startPrice.observe(this) {
            setPrice(binding.etStartPrice, startPriceWatcher, it.toInt())
        }
        viewModel.bidUnit.observe(this) {
            setPrice(binding.etBidUnit, bidUnitWatcher, it.toInt())
        }
    }

    private fun handleEvent(event: RegisterAuctionViewModel.RegisterAuctionEvent) {
        when (event) {
            is RegisterAuctionViewModel.RegisterAuctionEvent.ClosingTimePicker -> {
                showDateTimePicker(event.dateTime)
            }

            is RegisterAuctionViewModel.RegisterAuctionEvent.Exit -> {
                backActivity()
            }

            is RegisterAuctionViewModel.RegisterAuctionEvent.SubmitError -> {
                showErrorSubmitMessage(event.message)
            }

            is RegisterAuctionViewModel.RegisterAuctionEvent.SubmitResult -> {
                navigationToDetail(event.id)
                finish()
            }

            is RegisterAuctionViewModel.RegisterAuctionEvent.InputErrorEvent.InvalidValueInputEvent -> {
                showInvalidInputMessage()
            }

            is RegisterAuctionViewModel.RegisterAuctionEvent.InputErrorEvent.BlankExistEvent -> {
                showBlankExistMessage()
            }

            is RegisterAuctionViewModel.RegisterAuctionEvent.DeleteImage -> {
                showDeleteImageDialog(event.image)
            }

            is RegisterAuctionViewModel.RegisterAuctionEvent.MultipleMediaPicker -> {
                pickMultipleMediaLaunchers[viewModel.selectableImageSize - 1].launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                )
            }

            RegisterAuctionViewModel.RegisterAuctionEvent.PickCategory -> {
                navigationToCategorySelection()
            }

            RegisterAuctionViewModel.RegisterAuctionEvent.PickRegion -> {
                navigationToRegionSelection()
            }
        }
    }

    private fun showDateTimePicker(selectedDateTime: LocalDateTime) {
        // DataPicker Dialog에서 month는 0~11 범위
        // LocalDateTime에서 month는 1~12 범위
        // 따라서, 기본값을 세팅해줄 때는 -1, LocalDateTime에 저장할 때는 +1을 해주어야함
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                viewModel.setClosingDate(year, month + 1, dayOfMonth)
                showTimePicker(selectedDateTime.toLocalTime())
            },
            selectedDateTime.year,
            selectedDateTime.monthValue - 1,
            selectedDateTime.dayOfMonth,
        ).show()
    }

    private fun showTimePicker(selectedTime: LocalTime) {
        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                viewModel.setClosingTime(hourOfDay, minute)
            },
            selectedTime.hour,
            selectedTime.minute,
            false,
        ).show()
    }

    private fun showBlankExistMessage() {
        binding.root.showSnackbar(
            R.string.register_auction_blank_exist_error,
            R.string.register_auction_ok,
        )
    }

    private fun showInvalidInputMessage() {
        binding.root.showSnackbar(
            R.string.register_auction_invalid_input_error,
            R.string.register_auction_ok,
        )
    }

    private fun showErrorSubmitMessage(message: String) {
        binding.root.showSnackbar(
            message,
            getString(R.string.all_snackbar_default_action),
        )
    }

    private fun backActivity() {
        finish()
    }

    private fun navigationToDetail(id: Long) {
        startActivity(AuctionDetailActivity.getIntent(this, id))
    }

    private fun navigationToCategorySelection() {
        categoryActivityLauncher.launch(SelectCategoryActivity.getIntent(this))
    }

    private fun navigationToRegionSelection() {
        regionActivityLauncher.launch(SelectRegionsActivity.getIntent(this))
    }

    private fun setPrice(editText: EditText, watcher: DefaultTextWatcher, price: Int) {
        val displayPrice = getString(R.string.detail_auction_bid_dialog_input_price, price)
        editText.removeTextChangedListener(watcher)
        editText.setText(displayPrice)
        editText.setSelection(getCursorPositionFrontSuffix(displayPrice)) // " 원" 앞으로 커서 이동
        editText.addTextChangedListener(watcher)
    }

    private fun showDeleteImageDialog(image: RegisterImageModel) {
        showDialog(
            messageId = R.string.register_auction_dialog_delete_image_message,
            negativeStringId = R.string.all_dialog_default_negative_button,
            positiveStringId = R.string.register_auction_dialog_delete_image_positive_button,
            actionPositive = { viewModel.deleteImage(image) },
        )
    }

    private fun setupLinearLayoutRegisterImage() {
        binding.llRegisterImage.setOnClickListener {
            if (viewModel.selectableImageSize <= 0) {
                binding.llRegisterImage.showSnackbar(textId = R.string.register_autcion_snackbar_image_limit)
            } else {
                viewModel.pickImages()
            }
        }
    }

    private fun setupImageRecyclerView() {
        with(binding.rvImage) {
            adapter = imageAdapter
            addItemDecoration(RegisterAuctionImageSpaceItemDecoration(space = 24))
        }
    }

    private fun setupStartPriceTextWatcher() {
        binding.etStartPrice.addTextChangedListener(startPriceWatcher)
        binding.etStartPrice.setOnClickListener {
            binding.etStartPrice.setSelection(getCursorPositionFrontSuffix(binding.etStartPrice.text.toString()))
        }
    }

    private fun setupBidUnitTextWatcher() {
        binding.etBidUnit.addTextChangedListener(bidUnitWatcher)
        binding.etBidUnit.setOnClickListener {
            binding.etBidUnit.setSelection(getCursorPositionFrontSuffix(binding.etStartPrice.text.toString()))
        }
    }

    private fun getCursorPositionFrontSuffix(content: String): Int {
        return content.length - RegisterAuctionViewModel.SUFFIX_INPUT_PRICE.length
    }

    companion object {
        const val CATEGORY_RESULT = "category_result"
        const val REGIONS_RESULT = "region_result"

        fun getIntent(context: Context): Intent =
            Intent(context, RegisterAuctionActivity::class.java)
    }
}

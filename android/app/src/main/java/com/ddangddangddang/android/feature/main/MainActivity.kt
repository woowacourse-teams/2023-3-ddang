package com.ddangddangddang.android.feature.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS
import android.provider.Settings.EXTRA_APP_PACKAGE
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityMainBinding
import com.ddangddangddang.android.feature.home.HomeFragment
import com.ddangddangddang.android.feature.message.MessageFragment
import com.ddangddangddang.android.feature.mypage.MyPageFragment
import com.ddangddangddang.android.feature.search.SearchFragment
import com.ddangddangddang.android.global.screenViewLogEvent
import com.ddangddangddang.android.util.binding.BindingActivity
import com.ddangddangddang.android.util.compat.getSerializableExtraCompat
import com.ddangddangddang.android.util.view.BackKeyHandler
import com.ddangddangddang.android.util.view.showDialog
import com.ddangddangddang.android.util.view.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()
    private var isInitialized = false
    private val backKeyHandler = BackKeyHandler(this)

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            backKeyHandler.onBackPressed()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            if (shouldShowHowToGrantNotificationPermission()) {
                binding.root.showSnackbar(R.string.alarm_snackbar_how_to_grant_permission)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun shouldShowHowToGrantNotificationPermission(): Boolean =
        shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel

        setupViewModel()
        setupFragment()
        onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onResume() {
        super.onResume()
        if (isInitialized) {
            viewModel.currentFragmentType.value?.let { screenViewLogEvent(it.name) }
            return
        }
        isInitialized = true
    }

    private fun setupViewModel() {
        viewModel.event.observe(this) { handleEvent(it) }
        viewModel.currentFragmentType.observe(this) {
            changeFragment(it)
            screenViewLogEvent(it.name)
        }
    }

    private fun handleEvent(event: MainViewModel.MainEvent) {
        when (event) {
            MainViewModel.MainEvent.HomeToTop -> scrollHomeToTop()
            MainViewModel.MainEvent.NotificationPermissionCheck -> askNotificationPermission()
        }
    }

    private fun scrollHomeToTop() {
        val homeFragment =
            supportFragmentManager.findFragmentByTag(MainFragmentType.HOME.tag) as? HomeFragment
        homeFragment?.scrollToTop()
    }

    private fun changeFragment(type: MainFragmentType) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)

            supportFragmentManager.fragments.forEach(::hide)

            supportFragmentManager.findFragmentByTag(type.tag)?.let {
                show(it)
            } ?: createFragment(type).run {
                add(R.id.fcv_container, this, type.tag)
            }
        }
    }

    private fun createFragment(type: MainFragmentType): Fragment {
        return when (type) {
            MainFragmentType.HOME -> HomeFragment()
            MainFragmentType.SEARCH -> SearchFragment()
            MainFragmentType.MESSAGE -> MessageFragment()
            MainFragmentType.MY_PAGE -> MyPageFragment()
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (!shouldShowHowToGrantNotificationPermission()) {
                showRequestAlarmPermissionRationale()
            }
        } else {
            val notificationManager = NotificationManagerCompat.from(this)
            if (!notificationManager.areNotificationsEnabled()) {
                showRequestAlarmPermissionRationale()
            }
        }
    }

    private fun showRequestAlarmPermissionRationale() {
        showDialog(
            titleId = R.string.alarm_dialog_check_permission_title,
            messageId = R.string.alarm_dialog_check_permission_message,
            positiveStringId = R.string.alarm_dialog_check_permission_positive_button,
            actionPositive = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    requestNotificationPermissionForUnderTiramisu()
                }
            },
            isCancelable = false,
        )
    }

    private fun requestNotificationPermissionForUnderTiramisu() {
        showDialog(
            titleId = R.string.alarm_dialog_check_permission_title,
            messageId = R.string.alarm_dialog_check_permission_under_tiramisu_message,
            negativeStringId = R.string.all_dialog_default_negative_button,
            positiveStringId = R.string.alarm_dialog_check_permission_under_tiramisu_positive_button,
            actionPositive = ::openNotificationSettings,
            isCancelable = false,
        )
    }

    private fun openNotificationSettings() {
        val intent = Intent(ACTION_APP_NOTIFICATION_SETTINGS)
        intent.putExtra(EXTRA_APP_PACKAGE, this.packageName)
        startActivity(intent)
    }

    private fun setupFragment() {
        if (viewModel.currentFragmentType.value == null) {
            val type =
                intent.getSerializableExtraCompat(KEY_MAIN_FRAGMENT_TYPE) ?: MainFragmentType.HOME
            binding.bnvNavigation.selectedItemId = type.id
            viewModel.setupFragmentType(type)
        }
    }

    companion object {
        private const val KEY_MAIN_FRAGMENT_TYPE = "main_fragment_type"

        fun getIntent(context: Context, type: MainFragmentType = MainFragmentType.HOME): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(KEY_MAIN_FRAGMENT_TYPE, type)
            return intent
        }
    }
}

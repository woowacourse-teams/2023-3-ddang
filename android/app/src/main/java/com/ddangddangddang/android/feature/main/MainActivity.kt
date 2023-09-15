package com.ddangddangddang.android.feature.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityMainBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.feature.home.HomeFragment
import com.ddangddangddang.android.feature.message.MessageFragment
import com.ddangddangddang.android.feature.mypage.MyPageFragment
import com.ddangddangddang.android.feature.search.SearchFragment
import com.ddangddangddang.android.global.screenViewLogEvent
import com.ddangddangddang.android.util.binding.BindingActivity
import com.ddangddangddang.android.util.view.showDialog
import com.ddangddangddang.android.util.view.showSnackbar

class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel by viewModels<MainViewModel> { viewModelFactory }
    private var isInitialized = false

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

        askNotificationPermission()
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
        viewModel.currentFragmentType.observe(this) {
            changeFragment(it)
            screenViewLogEvent(it.name)
        }
    }

    private fun changeFragment(type: FragmentType) {
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

    private fun createFragment(type: FragmentType): Fragment {
        return when (type) {
            FragmentType.HOME -> HomeFragment()
            FragmentType.SEARCH -> SearchFragment()
            FragmentType.MESSAGE -> MessageFragment()
            FragmentType.MY_PAGE -> MyPageFragment()
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
            actionNegative = ::openNotificationSettings,
            isCancelable = false,
        )
    }

    private fun openNotificationSettings() {
        val intent = Intent(ACTION_APP_NOTIFICATION_SETTINGS)
        startActivity(intent)
    }
}

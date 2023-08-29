/*
 *    Copyright 2023 Roman Likhachev
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.yugyd.quiz.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.ads.MobileAds
import com.yugyd.quiz.core.AdIdProvider
import com.yugyd.quiz.core.ResIdProvider
import com.yugyd.quiz.main.MainView.Action
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var adIdProvider: AdIdProvider

    @Inject
    lateinit var resIdProvider: ResIdProvider

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            val systemUiController = rememberSystemUiController()
            val darkTheme = isSystemInDarkTheme()
            val useDarkIcons = !darkTheme

            DisposableEffect(systemUiController, useDarkIcons) {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons,
                )

                onDispose {}
            }

            QuizApplicationTheme(darkTheme = darkTheme) {
                MainApp(
                    viewModel = viewModel,
                    adIdProvider = adIdProvider,
                    resIdProvider = resIdProvider,
                )
            }
        }

        MobileAds.initialize(this)

        if (savedInstanceState == null) {
            processPush(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        processPush(intent)
        super.onNewIntent(intent)
    }

    private fun processPush(intent: Intent?) {
        val extras = intent?.extras
            ?.toMap().orEmpty()
            .filterValues { it is String }
            .mapValues { it.value as String }
        viewModel.onAction(Action.ProcessPushData(extras))
    }

    private fun Bundle.toMap() = keySet().associateWith(::getString)
}

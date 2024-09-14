package com.yugyd.quiz.ui.end.gameend

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.yugyd.quiz.ad.api.AdErrorDomainModel
import com.yugyd.quiz.ad.common.AdEventCallback
import com.yugyd.quiz.ad.interstitial.AdInterstitialEventCallback
import com.yugyd.quiz.ad.interstitial.LocalInterstitialAdFactory
import com.yugyd.quiz.ui.end.gameend.GameEndView.State.InterstitialAdState
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import java.lang.ref.WeakReference

@Composable
internal fun InterstitialAd(
    adState: InterstitialAdState?,
    adUnitId: String?,
    onAdLoaded: () -> Unit,
    onAdFailedToLoad: (AdErrorDomainModel) -> Unit,
    onAdFailedToShow: (AdErrorDomainModel) -> Unit,
    onAdDismissed: () -> Unit,
) {
    if (adState == null || adUnitId.isNullOrEmpty()) {
        return
    }

    val adFactory = LocalInterstitialAdFactory.current

    val context = LocalContext.current
    val activity = LocalContext.current as Activity

    val weakReference = WeakReference(activity)

    val loadCallback by rememberUpdatedState(
        object : AdEventCallback {
            override fun onAdLoaded() = onAdLoaded()
            override fun onAdFailedToLoad(adError: AdErrorDomainModel) =
                onAdFailedToLoad(adError)
        }
    )

    LaunchedEffect(Unit) {
        adFactory.create(
            context = context,
            isActivityDestroyProvider = {
                weakReference.get()?.isDestroyed ?: true
            },
            callback = loadCallback,
        )
    }

    val showCallback by rememberUpdatedState(
        object : AdInterstitialEventCallback {
            override fun onAdDismissed() = onAdDismissed()
            override fun onAdFailedToShow(adError: AdErrorDomainModel) =
                onAdFailedToShow(adError)
        }
    )

    LaunchedEffect(key1 = adState) {
        when (adState) {
            InterstitialAdState.LOAD_AD -> {
                adFactory.loadAd(adUnitId = adUnitId)
            }

            InterstitialAdState.IS_LOADED, InterstitialAdState.NOT_LOADED -> Unit

            InterstitialAdState.SHOW_AD -> {
                adFactory.showAd(
                    activity = activity,
                    callback = showCallback,
                )
            }
        }
    }

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                adFactory.destroy()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@ThemePreviews
@Composable
private fun InterstitialAdPreview() {
    QuizApplicationTheme {
        QuizBackground {
            InterstitialAd(
                adState = InterstitialAdState.NOT_LOADED,
                adUnitId = "Foo",
                onAdFailedToLoad = {},
                onAdLoaded = {},
                onAdDismissed = {},
                onAdFailedToShow = {},
            )
        }
    }
}

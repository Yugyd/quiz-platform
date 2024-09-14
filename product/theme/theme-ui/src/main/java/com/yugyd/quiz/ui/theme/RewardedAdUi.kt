package com.yugyd.quiz.ui.theme

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
import com.yugyd.quiz.ad.rewarded.AdRewardEventCallback
import com.yugyd.quiz.ad.rewarded.LocalRewardAdFactory
import com.yugyd.quiz.ad.rewarded.RewardDomainModel
import com.yugyd.quiz.ui.theme.ThemeView.State.RewardAdState
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import java.lang.ref.WeakReference

@Composable
internal fun RewardedAd(
    adState: RewardAdState?,
    adUnitId: String?,
    onAdLoaded: () -> Unit,
    onAdFailedToLoad: (AdErrorDomainModel) -> Unit,
    onReward: (RewardDomainModel) -> Unit,
    onAdDismissed: () -> Unit,
    onAdFailedToShow: (AdErrorDomainModel) -> Unit,
) {
    if (adState == null) {
        return
    }

    val adFactory = LocalRewardAdFactory.current

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
        object : AdRewardEventCallback {
            override fun onRewarded(reward: RewardDomainModel) = onReward(reward)
            override fun onAdDismissed() = onAdDismissed()
            override fun onAdFailedToShow(adError: AdErrorDomainModel) =
                onAdFailedToShow(adError)
        }
    )

    LaunchedEffect(key1 = adState) {
        when (adState) {
            RewardAdState.LOAD_AD -> {
                adFactory.loadAd(adUnitId = adUnitId!!)
            }

            RewardAdState.IS_LOADED, RewardAdState.NOT_LOADED -> Unit

            RewardAdState.SHOW_AD -> {
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
private fun ContentPreview() {
    QuizApplicationTheme {
        QuizBackground {
            RewardedAd(
                adState = RewardAdState.LOAD_AD,
                adUnitId = "Foo",
                onAdFailedToLoad = {},
                onAdLoaded = {},
                onReward = {},
                onAdDismissed = {},
                onAdFailedToShow = {},
            )
        }
    }
}

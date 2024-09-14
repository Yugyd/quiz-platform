package com.yugyd.quiz.gameui.game

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.yugyd.quiz.ad.api.AdErrorDomainModel
import com.yugyd.quiz.ad.banner.LocalAdViewFactory
import com.yugyd.quiz.ad.common.AdEventCallback
import com.yugyd.quiz.core.TextModel
import com.yugyd.quiz.gameui.game.GameView.State.BannerState
import com.yugyd.quiz.gameui.game.GameView.State.BannerState.AdBannerState
import com.yugyd.quiz.gameui.game.model.GameStateUiModel
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.extension.getText
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import java.lang.ref.WeakReference

private val adBannerReusableModifier = Modifier
    .fillMaxWidth()
    .defaultMinSize(minHeight = 50.dp)

@Composable
internal fun AdContainer(
    modifier: Modifier = Modifier,
    bannerState: BannerState,
    onProBannerClicked: () -> Unit,
    onBannerAdLoaded: () -> Unit,
    onBannerAdFailedToLoad: (AdErrorDomainModel) -> Unit,
) {
    Box(
        modifier = modifier
            .then(adBannerReusableModifier)
            .run {
                when (bannerState) {
                    is AdBannerState -> {
                        this
                    }

                    is BannerState.PromoBannerState -> {
                        clickable {
                            onProBannerClicked()
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        when (bannerState) {
            is AdBannerState -> {
                AdBanner(
                    bannerAdUnitId = bannerState.bannerAdUnitId.getText(),
                    onBannerAdLoaded = onBannerAdLoaded,
                    onBannerAdFailedToLoad = onBannerAdFailedToLoad,
                )
            }

            is BannerState.PromoBannerState -> {
                ProMessageBanner(proMessage = bannerState.proMessage.getText())
            }
        }
    }
}

@Composable
private fun ProMessageBanner(proMessage: String) {
    Surface(
        modifier = adBannerReusableModifier,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = proMessage,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun AdBanner(
    bannerAdUnitId: String,
    onBannerAdLoaded: () -> Unit,
    onBannerAdFailedToLoad: (AdErrorDomainModel) -> Unit,
) {
    val callback by rememberUpdatedState(
        object : AdEventCallback {
            override fun onAdLoaded() = onBannerAdLoaded()
            override fun onAdFailedToLoad(adError: AdErrorDomainModel) =
                onBannerAdFailedToLoad(adError)
        }
    )

    val factory = LocalAdViewFactory.current

    val windowSizeClass = currentWindowSize()

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            val weakReference = WeakReference(context as? Activity)

            factory.createAndLoadAd(
                context = context,
                bannerAdUnitId = bannerAdUnitId,
                adContainerWidth = windowSizeClass.width,
                resources = context.resources,
                callback = callback,
                isActivityDestroyProvider = {
                    weakReference.get()?.isDestroyed ?: true
                },
            )
        },
    )
}

@ThemePreviews
@Composable
private fun PromoAdContainerPreview(
    @PreviewParameter(GamePreviewParameterProvider::class) gameData: GameStateUiModel,
) {
    QuizApplicationTheme {
        QuizBackground {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary)
                        .weight(weight = 1F)
                ) {
                    Text(text = "Test")
                }

                AdContainer(
                    bannerState = BannerState.PromoBannerState(
                        proMessage = TextModel.StringTextModel("Foo"),
                    ),
                    onProBannerClicked = {},
                    onBannerAdLoaded = {},
                    onBannerAdFailedToLoad = {},
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun LoadingBannerAdContainerPreview(
    @PreviewParameter(GamePreviewParameterProvider::class) gameData: GameStateUiModel,
) {
    QuizApplicationTheme {
        QuizBackground {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary)
                        .weight(weight = 1F)
                ) {
                    Text(text = "Test")
                }

                AdContainer(
                    bannerState = AdBannerState(
                        bannerAdUnitId = TextModel.StringTextModel("Foo"),
                        proMessage = TextModel.StringTextModel("Foo"),
                    ),
                    onProBannerClicked = {},
                    onBannerAdLoaded = {},
                    onBannerAdFailedToLoad = {},
                )
            }
        }
    }
}


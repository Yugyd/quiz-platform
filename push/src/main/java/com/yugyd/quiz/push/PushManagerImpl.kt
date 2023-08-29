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

package com.yugyd.quiz.push

import android.Manifest
import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.os.SystemClock
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.yugyd.quiz.core.ResIdProvider
import com.yugyd.quiz.domain.repository.Logger
import com.yugyd.quiz.uikit.theme.app_color_positive
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PushManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val logger: Logger,
    private val resIdProvider: ResIdProvider
) : PushManager {

    private val notificationManager = NotificationManagerCompat.from(context)

    private val channelId by lazy {
        context.getString(R.string.default_channel_id)
    }

    private val launchIntent
        get() = context.packageManager.getLaunchIntentForPackage(context.packageName)

    override fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(createNotificationChannel())
        }
        logger.print(TAG, "Notification channels is created")
    }

    override fun processPush(pushMessage: PushMessage) {
        if (
            notificationManager.areNotificationsEnabled() &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val notification = createNotification(pushMessage)
            notificationManager.notify(pushMessage.messageId.hashCode(), notification)
            logger.print(
                TAG,
                "Process notification: $pushMessage.messageId, $pushMessage.title, $pushMessage.message"
            )
        } else {
            logger.print(TAG, "Notification not enabled")
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): NotificationChannel {
        val name = context.getString(R.string.default_channel_name)
        val description = context.getString(R.string.default_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        return NotificationChannel(
            channelId,
            name,
            importance
        ).apply {
            setShowBadge(true)
            this.description = description
        }
    }

    private fun createNotification(pushMessage: PushMessage) =
        NotificationCompat.Builder(context, channelId)
            .setContentTitle(pushMessage.title)
            .setContentText(pushMessage.message)
            .setSmallIcon(resIdProvider.pushIcon())
            .setColor(app_color_positive.value.toInt())
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(createPendingIntent(pushMessage))
            .build()

    private fun createPendingIntent(pushMessage: PushMessage) = PendingIntent.getActivity(
        context,
        SystemClock.currentThreadTimeMillis().toInt(),
        createLaunchActivityIntent(pushMessage),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
    )

    private fun createLaunchActivityIntent(pushMessage: PushMessage) = Intent(launchIntent).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        for ((key, value) in pushMessage.data.entries) {
            putExtra(key, value)
        }
    }

    companion object {
        private const val TAG = "PushManager"
    }
}

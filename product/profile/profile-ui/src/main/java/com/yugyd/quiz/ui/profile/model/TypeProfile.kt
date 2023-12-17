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

package com.yugyd.quiz.ui.profile.model

enum class TypeProfile(val id: Int) {
    TRANSITION(id = 1),
    SORT_QUEST(id = 2),
    VIBRATION(id = 3),
    PRO(id = 4),
    RESTORE_PURCHASE(id = 5),
    RATE_APP(id = 6),
    SHARE(id = 7),
    OTHER_APPS(id = 8),
    REPORT_ERROR(id = 9),
    PRIVACY_POLICY(id = 10),
    SETTINGS_SECTION(id = 11),
    PURCHASES_SECTION(id = 12),
    PLEASE_US_SECTION(id = 13),
    FEEDBACK_SECTION(id = 14),
    SOCIAL_SECTION(id = 15),
    TELEGRAM_SOCIAL(id = 16),
    SELECT_CONTENT(id = 17),
    OPEN_SOURCE(id = 18),
    NONE(id = -1)
}

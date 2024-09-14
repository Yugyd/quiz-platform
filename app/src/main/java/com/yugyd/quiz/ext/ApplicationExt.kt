package com.yugyd.quiz.ext

import android.app.ActivityManager
import android.app.Application
import android.os.Process

internal fun Application.isMainProcess(): Boolean {
    return packageName == getProcessName()
}

internal fun Application.getProcessName(): String? {
    val mypid = Process.myPid()
    val manager = getSystemService(Application.ACTIVITY_SERVICE) as ActivityManager
    val infos = manager.runningAppProcesses
    for (info in infos) {
        if (info.pid == mypid) {
            return info.processName
        }
    }
    return null
}

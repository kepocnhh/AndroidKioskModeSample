package test.android.kiosk

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent

class MainDeviceAdminReceiver : DeviceAdminReceiver() {
    private fun log(message: String) {
        println("[Main|DAR|${hashCode()}]: $message")
    }

    override fun onEnabled(context: Context, intent: Intent) {
        log("on -> enabled: $intent")
    }

    override fun onDisabled(context: Context, intent: Intent) {
        log("on -> disabled: $intent")
    }

    override fun onLockTaskModeEntering(context: Context, intent: Intent, pkg: String) {
        log("on -> lock enter: $pkg $intent")
    }

    override fun onLockTaskModeExiting(context: Context, intent: Intent) {
        log("on -> lock exit: $intent")
    }
}

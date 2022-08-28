package test.android.kiosk

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity : AppCompatActivity() {
    companion object {
//        private var manager: DevicePolicyManager? = null
        private var isLocked = false
    }

    private fun log(message: String) {
        println("[Main|Activity|${hashCode()}]: $message")
    }

    private fun switchLock(manager: DevicePolicyManager) {
        if (!manager.isLockTaskPermitted(packageName)) TODO()
        if (isLocked) {
            isLocked = false
            startLockTask()
        } else {
            isLocked = true
            stopLockTask()
        }
    }

    private fun switchSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            if (controller != null) {
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                controller.hide(WindowInsets.Type.systemBars())
            }
//            window.navigationBarColor = getColor(R.color.internal_black_semitransparent_light)
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    @Composable
    private fun Screen(manager: DevicePolicyManager) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Blue)
                        .clickable {
                            switchLock(manager)
                        }
                        .padding(8.dp)
                ) {
                    BasicText(
                        modifier = Modifier.align(Alignment.Center),
                        text = "lock"
                    )
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val manager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val name = ComponentName(this, MainDeviceAdminReceiver::class.java)
        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            log("on -> result: ${it.resultCode}")
            if (it.resultCode == Activity.RESULT_OK) {
                // todo
            } else {
                showToast("isAdminActive: false")
                finish()
            }
        }
        if (manager.isAdminActive(name)) {
            setContent {
                Screen(manager)
            }
        } else {
            launcher.launch(
                Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).also {
                    it.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, name)
                }
            )
        }
    }
}

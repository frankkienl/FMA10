package nl.fmfanboy.fma10

import android.app.Activity
import android.os.Bundle
import eu.chainfire.libsuperuser.Shell
import kotlinx.android.synthetic.main.demo_activity.*

class DemoActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity)
        btnTest.setOnClickListener {
            openFlitsmeister()
        }
    }

    fun openFlitsmeister() {
        Shell.Builder()
            .addCommand(
                "am start -n nl.flitsmeister" +
                        "/nl.flitsmeister.controllers.activities.launcher.LauncherActivity"
            )
            .useSU() //Must be root, otherwise 'Permission denial'.
            .openThreaded() //Run in thread, as shell can be blocking
    }
}
package nl.fmfanboy.fma10

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import eu.chainfire.libsuperuser.Shell


object DemoFmA10 {
    fun init(lpparam: XC_LoadPackage.LoadPackageParam) {
        //Thanks to ProGuard, the method names are obfuscated.
        val methodName = "b"
        XposedHelpers.findAndHookMethod(
            "nl.flitsmeister.services.autostart.AutoStartDetectionService",
            lpparam.classLoader,
            methodName, //see above
            "android.content.Context", //Parameter-type for hooked method
            object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun afterHookedMethod(param: MethodHookParam?) {
                    XposedBridge.log("[fma10] after hooked method")
                    startLauncherActivity(lpparam.packageName)
                }
            }
        )
    }
    fun startLauncherActivity(packageName: String) {
        //Start activity via Activity Manager
        Shell.Builder()
            .addCommand("am start -n $packageName/nl.flitsmeister.controllers.activities.launcher.LauncherActivity")
            .useSU() //Must be root, otherwise 'Permission denial'.
            .openThreaded() //Run in thread, as shell can be blocking

        XposedBridge.log("[fma10] should start activity now.")
    }











//    fun turnScreenOn(param: XC_MethodHook.MethodHookParam?) {
//        //Turn screen on
//        param?.let {
//            //https://stackoverflow.com/questions/32352750/how-do-i-programatically-unlock-screen-on-rooted-device
//            val context = param.args[0] as Context
//            val pm = context.getSystemService(POWER_SERVICE) as PowerManager
//            val wl = pm.newWakeLock(
//                PowerManager.FULL_WAKE_LOCK
//                        or PowerManager.ACQUIRE_CAUSES_WAKEUP
//                        or PowerManager.ON_AFTER_RELEASE, "fma10:Turn screen on"
//            )
//            wl.acquire(1000L)
//
//            //https://developer.android.com/reference/android/app/KeyguardManager.html#newKeyguardLock(java.lang.String)
//            //if screen is locked, try to unlock
//            //val km = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
//            //val kl = km.newKeyguardLock("name")
//            //kl.disableKeyguard()
//            //km.requestDismissKeyguard(null,null)
//        }
//    }


}//am start -n nl.flitsmeister/nl.flitsmeister.controllers.activities.launcher.LauncherActivity
package nl.fmfanboy.fma10

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainMod : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (lpparam == null) {
            XposedBridge.log("[fma10] lpparam == null")
            return
        }
        if (lpparam.packageName == null) {
            XposedBridge.log("[fma10] lpparam.packageName == null")
            return
        }

        //Check if we are in Android, for the permission injection
        if (lpparam.packageName == "android" && lpparam.processName == "android") {
            XposedBridge.log("[fma10] android found")
            //PermGrant.init(lpparam.classLoader)
        }

        //Check if we are in Flitsmeister
        if (lpparam.packageName == "nl.flitsmeister" || lpparam.packageName == "nl.flitsmeister.debug") {
            XposedBridge.log("[fma10] Package found " + lpparam.packageName)
            FmA10.init(lpparam)
        }
    }
}
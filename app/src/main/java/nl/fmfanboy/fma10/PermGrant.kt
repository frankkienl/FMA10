package nl.fmfanboy.fma10

/*
    Copyright (C) 2016-2017 Lawiusz Fras
    This file is part of lockscreenvisualizerxposed.
    lockscreenvisualizerxposed is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    lockscreenvisualizerxposed is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with lockscreenvisualizerxposed; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
import android.util.Log

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object PermGrant {
    private const val TAG = "fma10"
    private const val PERM_DISABLE_KEYGUARD = "android.permission.DISABLE_KEYGUARD"

    private const val CLASS_PACKAGE_MANAGER_SERVICE = "com.android.server.pm.PackageManagerService"
    private const val CLASS_PACKAGE_PARSER_PACKAGE = "android.content.pm.PackageParser.Package"

    fun init(loader: ClassLoader) {
        try {
            val pmServiceClass = XposedHelpers.findClass(
                CLASS_PACKAGE_MANAGER_SERVICE,
                loader
            )

            XposedHelpers.findAndHookMethod(pmServiceClass,
                "grantPermissionsLPw",
                CLASS_PACKAGE_PARSER_PACKAGE,
                Boolean::class.javaPrimitiveType,
                String::class.java,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                        val pkgName =
                            XposedHelpers.getObjectField(param!!.args[0], "packageName") as String

                        if (pkgName.startsWith("nl.flitsmeister")) {
                            val extras = XposedHelpers.getObjectField(param.args[0], "mExtras")
                            val ps = XposedHelpers.callMethod(extras, "getPermissionsState")
                            val grantedPerms = XposedHelpers.getObjectField(
                                param.args[0],
                                "requestedPermissions"
                            ) as List<String>
                            val settings =
                                XposedHelpers.getObjectField(param.thisObject, "mSettings")
                            val permissions = XposedHelpers.getObjectField(settings, "mPermissions")

                            if (!grantedPerms.contains(PERM_DISABLE_KEYGUARD)) {
                                val pModifyAudio = XposedHelpers
                                    .callMethod(permissions, "get", PERM_DISABLE_KEYGUARD)
                                XposedHelpers.callMethod(
                                    ps, "grantInstallPermission",
                                    pModifyAudio
                                )
                            }
                        }
                    }
                })
        } catch (t: Throwable) {
            Log.e(TAG, "error", t)
        }

    }
}
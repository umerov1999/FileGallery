package dev.ragnarok.filegallery.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import dev.ragnarok.filegallery.R


object AppPerms {
    fun hasReadWriteStoragePermission(context: Context): Boolean {
        if (!Utils.hasMarshmallow()) return true
        if (Utils.hasScopedStorage()) {
            return Environment.isExternalStorageManager()
        }
        val hasWritePermission = PermissionChecker.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val hasReadPermission = PermissionChecker.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return hasWritePermission == PackageManager.PERMISSION_GRANTED && hasReadPermission == PackageManager.PERMISSION_GRANTED
    }

    fun requestReadWritePermissionsResult(
        activity: AppCompatActivity,
        callback: onPermissionsResult
    ): doRequestPermissions {
        if (Utils.hasScopedStorage()) {
            val request = activity.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (Environment.isExternalStorageManager()) {
                    callback.granted()
                } else {
                    Utils.showRedTopToast(activity, R.string.not_permitted)
                    callback.not_granted()
                }
            }
            return object : doRequestPermissions {
                override fun launch() {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    val uri: Uri =
                        Uri.fromParts("package", activity.packageName, null)
                    intent.data = uri
                    //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    request.launch(intent)
                }
            }
        }
        val request = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result: Map<String, Boolean> ->
            if (Utils.checkValues(result.values)) {
                callback.granted()
            } else {
                Utils.showRedTopToast(activity, R.string.not_permitted)
                callback.not_granted()
            }
        }
        return object : doRequestPermissions {
            override fun launch() {
                request.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }
        }
    }

    interface doRequestPermissions {
        fun launch()
    }

    interface onPermissionsResult {
        fun granted()
        fun not_granted()
    }
}
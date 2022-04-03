package dev.ragnarok.filegallery.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.os.Process
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import dev.ragnarok.filegallery.R

object AppPerms {
    fun hasReadWriteStoragePermission(context: Context): Boolean {
        if (!Utils.hasMarshmallow()) return true
        if (Utils.hasScopedStorage()) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
                Process.killProcess(Process.myPid())
                return false
            }
            return true
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

    inline fun <reified T : Fragment> T.requestPermissionsAbs(
        permissions: Array<String>,
        crossinline granted: () -> Unit
    ): DoRequestPermissions {
        val request = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result: Map<String, Boolean> ->
            if (Utils.checkValues(result.values)) {
                granted.invoke()
            } else {
                Utils.showRedTopToast(requireActivity(), R.string.not_permitted)
            }
        }
        return object : DoRequestPermissions {
            override fun launch() {
                request.launch(permissions)
            }
        }
    }

    inline fun <reified T : AppCompatActivity> T.requestPermissionsAbs(
        permissions: Array<String>,
        crossinline granted: () -> Unit
    ): DoRequestPermissions {
        val request = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result: Map<String, Boolean> ->
            if (Utils.checkValues(result.values)) {
                granted.invoke()
            } else {
                Utils.showRedTopToast(this, R.string.not_permitted)
            }
        }
        return object : DoRequestPermissions {
            override fun launch() {
                request.launch(permissions)
            }
        }
    }

    inline fun <reified T : AppCompatActivity> T.requestPermissionsResultAbs(
        permissions: Array<String>,
        crossinline granted: () -> Unit,
        crossinline denied: () -> Unit
    ): DoRequestPermissions {
        val request = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result: Map<String, Boolean> ->
            if (Utils.checkValues(result.values)) {
                granted.invoke()
            } else {
                Utils.showRedTopToast(this, R.string.not_permitted)
                denied.invoke()
            }
        }
        return object : DoRequestPermissions {
            override fun launch() {
                request.launch(permissions)
            }
        }
    }

    inline fun <reified T : Fragment> T.requestPermissionsResultAbs(
        permissions: Array<String>,
        crossinline granted: () -> Unit,
        crossinline denied: () -> Unit
    ): DoRequestPermissions {
        val request = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result: Map<String, Boolean> ->
            if (Utils.checkValues(result.values)) {
                granted.invoke()
            } else {
                Utils.showRedTopToast(requireActivity(), R.string.not_permitted)
                denied.invoke()
            }
        }
        return object : DoRequestPermissions {
            override fun launch() {
                request.launch(permissions)
            }
        }
    }

    interface DoRequestPermissions {
        fun launch()
    }
}
package com.rjesture.kotbox.general

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rjesture.kotbox.R
import com.rjesture.kotbox.setLog
import com.rjesture.kotbox.showAlertDialog
import java.util.*

class Splash : AppCompatActivity() {
    private val permission_granted_code = 100
    private val appPermission = arrayOf(
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.INTERNET
    )
    lateinit var mActivity :Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mActivity = this@Splash
        if (getPermission()) {
            Handler().postDelayed({ setInitals() }, 3000)
        }
    }
    private fun setInitals() {
        startActivity(Intent(mActivity, Dashboard::class.java))
        finish();
    }
    fun getPermission(): Boolean {
        val listPermissionsNeedeFor: MutableList<String> = ArrayList()
        for (permission in appPermission) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                listPermissionsNeedeFor.add(permission)
            }
        }
        if (!listPermissionsNeedeFor.isEmpty()) {
            ActivityCompat.requestPermissions(
                mActivity,
                listPermissionsNeedeFor.toTypedArray(),
                permission_granted_code
            )
            return false
        }
        return true
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permission_granted_code) {
            val permisionResults = HashMap<String, Int>()
            var deniedPermissionCount = 0
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permisionResults[permissions[i]] = grantResults[i]
                    deniedPermissionCount++
                }
            }
            if (deniedPermissionCount == 0) {
                setInitals()
            } else {
                for ((permName, permResult) in permisionResults) {
                    setLog("MyPermissions", "denied  $permName")
                    if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permName)) {
                        showAlertDialog(
                            mActivity,
                            "",
                            "Do allow or permission to make application work fine",
                            "Yes, Grant Permission",
                            { dialogInterface, i ->
                                dialogInterface.dismiss()
                                getPermission()
                            },
                            "No, Exit app",
                            { dialogInterface, i ->
                                dialogInterface.dismiss()
                                finish()
                            },
                            false
                        )
                    } else {
                        showAlertDialog(
                            mActivity,
                            "",
                            "You have denied some permissons. Allow all permissions" +
                                    " at [Settings] > [Permissions]",
                            "Go to settings",
                            { dialogInterface, i ->
                                dialogInterface.dismiss()
                                val intent = Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", packageName, null)
                                )
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            },
                            "No, Exit app",
                            { dialogInterface, i ->
                                dialogInterface.dismiss()
                                finish()
                            },
                            false
                        )
                        break
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}
package com.example.flashlightapp

import android.content.Context
import android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.flashlightapp.ui.theme.MyGray

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {

        //Defining variable for camera service
        var cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager

        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MyGray
            ) {
                val context = LocalContext.current
                val cameraManager =
                    getSystemService(Context.CAMERA_SERVICE) as CameraManager
                val hasFlashFeature = cameraManager.cameraIdList.any { cameraId ->
                    cameraManager.getCameraCharacteristics(cameraId)
                        .get(FLASH_INFO_AVAILABLE) == true
                }

                if (hasFlashFeature) {
                    FlashLightApp(cameraManager = cameraManager)
                } else {
                    Toast.makeText(
                        context,
                        "Flash not available on device\nApp will close",
                        Toast.LENGTH_LONG
                    ).show()
                    (context as? ComponentActivity)?.finish()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @Composable
    private fun FlashLightApp(cameraManager: CameraManager) {
        var isFlashOn = remember {
            mutableStateOf(false)
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(color = MyGray)
        ) {

            Image(painter = if (isFlashOn.value) painterResource(id = R.drawable.flashlight_off) else painterResource(
                id = R.drawable.flashlight_off
            ), contentDescription = "Flash Off",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .clickable {
                        if (!isFlashOn.value) {
                            //getting the rear camera ID and storing at [0] position in list
                            val rearCamera = cameraManager.cameraIdList[0]
                            cameraManager.setTorchMode(rearCamera, true)
                            isFlashOn.value = true
                        } else {
                            val rearCamera = cameraManager.cameraIdList[0]
                            cameraManager.setTorchMode(rearCamera, false)
                            isFlashOn.value = false
                        }
                    })
        }
    }
}

package com.example.jawlah.presentation.component

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.jawlah.R
import java.io.File

@Composable
fun TakePictureIcon(
    directory: File? = null,
    onSetUri: (Uri) -> Unit = {},
    onPicReceived: (Bitmap, QuickPick?) -> Unit
) {
    val context = LocalContext.current
    val tempUri = remember { mutableStateOf<Uri?>(null) }
    val authority = stringResource(id = R.string.file_provider)
    val quickPickState = remember { mutableStateOf<QuickPick?>(null) }
    val takePictureState = remember { mutableStateOf<Boolean>(false) }

    val intent = (context as? Activity)?.intent
    val extraNavigateTo = remember { intent?.getStringExtra("navigateTo") }
    if (extraNavigateTo != null) {
        TakePicture(context = context, quickPick = QuickPick.LANDMARK_LENS) { bitmap, quickPick ->
            onPicReceived(bitmap, quickPick)
        }
    }

    fun getTempUri(): Uri? {
        directory?.let {
            it.mkdirs()
            val file = File.createTempFile(
                "image_" + System.currentTimeMillis().toString(),
                ".jpg",
                it
            )

            return FileProvider.getUriForFile(
                context,
                authority,
                file
            ) ?: throw IllegalStateException("Failed to create temporary Uri")
        }
        return null
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            it?.let {
                onSetUri.invoke(it)
            }
        }
    )

//    val takePhotoLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.TakePicture(),
//        onResult = { isSaved ->
//            tempUri.value?.let {
//                onSetUri.invoke(it)
//            }
//        }
//    )

    if (takePictureState.value) {
        TakePicture(context = context, quickPick = quickPickState.value) { bitmap, quickPick ->
            onPicReceived(bitmap, quickPick)
            takePictureState.value = false
        }
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    if (showBottomSheet) {
        PickImageBottomSheet(
            onDismiss = {
                showBottomSheet = false
            },
            onTakePhotoClick = { quickPick ->
                showBottomSheet = false
                quickPickState.value = quickPick
                takePictureState.value = true
            },
            onPhotoGalleryClick = {
                showBottomSheet = false
                imagePicker.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
        )
    }

    IconButton(
        onClick = {
            showBottomSheet = true
        },
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add Image"
        )
    }
}


@Composable
fun TakePicture(
    context: Context,
    quickPick: QuickPick?,
    onPicReceived: (Bitmap, QuickPick?) -> Unit
) {
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    val takePhotoLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                if (imageBitmap != null) {
                    onPicReceived(imageBitmap, quickPick)
                }
            }
        }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            takePhotoLauncher.launch(takePictureIntent)
        } else {
            // Permission is denied, handle it accordingly
        }
    }

    LaunchedEffect(Unit) {
        val permission = Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            takePhotoLauncher.launch(takePictureIntent)
        } else {
            cameraPermissionLauncher.launch(permission)
        }
    }
}
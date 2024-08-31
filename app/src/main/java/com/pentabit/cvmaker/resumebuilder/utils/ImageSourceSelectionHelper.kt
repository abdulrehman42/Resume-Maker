package com.pentabit.cvmaker.resumebuilder.utils

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdSize
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.ads_manager.ad_utils.CollapsibleOrientation
import com.pentabit.pentabitessentials.databinding.GeneralDialogLayoutBinding
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.request.ExplainScope
import com.permissionx.guolindev.request.ForwardScope

class ImageSourceSelectionHelper(val activity: AdBaseActivity) {

    private interface OnPermissionProvided {
        fun onPermissionProvided()
    }

    private var cameraPhotoUri: Uri? = null

    private val pickMedia =
        activity.registerForActivityResult<PickVisualMediaRequest, Uri>(
            ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            if (uri != null) {
                val intent = Intent(activity, MainCutOutActivity::class.java)
                intent.putExtra("path", uri.toString())
                activity.startActivity(intent)
            } else {
                AppsKitSDKUtils.makeToast(activity.getString(R.string.no_image_selected))
            }
        }

    private val takePhotoLauncher: ActivityResultLauncher<Uri> = activity.registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { result ->
        if (java.lang.Boolean.TRUE == result) {
            if (cameraPhotoUri != null) {
                val intent = Intent(activity, MainCutOutActivity::class.java)
                intent.putExtra("path", cameraPhotoUri.toString())
                activity.startActivity(intent)
            }
        }
    }

    private val requestPermissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (java.lang.Boolean.TRUE == isGranted) {
                createdialog()
            } else {
                askReadPermissionOnReject()
            }
        }

    private val permissionList: MutableList<String> = ArrayList()


    private fun takeImageThroughCamera() {
        if (ContextCompat.checkSelfPermission(
                activity, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) openCamera()
        else {
            askCameraPermission()
        }
    }

    private fun askCameraPermission() {
        permissionList.clear()
        permissionList.add(Manifest.permission.CAMERA)
        askForPermission(object : OnPermissionProvided {
            override fun onPermissionProvided() {
                takeImageThroughCamera()
            }
        })
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraPhotoUri = activity.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues()
        )
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoUri)
        takePhotoLauncher.launch(cameraPhotoUri)
    }


    fun onCreateWallpaperClicked() {
        setPermissionList()
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_MEDIA_IMAGES
            )
                    == PackageManager.PERMISSION_GRANTED) || ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            createdialog()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                askAbovePermission()
            } else askForPermission(object : OnPermissionProvided {
                override fun onPermissionProvided() {
                    createdialog()
                }
            })
        }
    }

    private fun setPermissionList() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun askAbovePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(
                Manifest.permission.READ_MEDIA_IMAGES
            )
        }
    }

    private fun askReadPermissionOnReject() {
        if (!activity.isFinishing) {
            val dialogBuilder = AlertDialog.Builder(activity)
            val alertDialog = dialogBuilder.create()
            alertDialog.show()
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dialogBinding: GeneralDialogLayoutBinding = GeneralDialogLayoutBinding.inflate(
                activity.layoutInflater
            )
            dialogBinding.yes.setBackgroundColor(activity.resources.getColor(R.color.background_bottom))
            alertDialog.window!!.setContentView(dialogBinding.getRoot())
            dialogBinding.title.setText("Permission Required")
            dialogBinding.descriptionTxt.setText(R.string.read_permission_required_msg)
            dialogBinding.yes.setOnClickListener {
                alertDialog.dismiss()
                val intent =
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.setData(Uri.fromParts("package", activity.packageName, null))
                activity.startActivity(intent)
            }
            dialogBinding.no.setOnClickListener { alertDialog.dismiss() }
        }
    }

    private fun askForPermission(callback: OnPermissionProvided) {
        PermissionX.init(activity).permissions(permissionList)
            .onExplainRequestReason { scope: ExplainScope, deniedList: List<String> ->
                scope.showRequestReasonDialog(
                    deniedList,
                    activity.getString(R.string.detail_reason_for_to_access_read_write_permission),
                    activity.getString(R.string.ok_text),
                    activity.getString(R.string.cancel)
                )
            }.explainReasonBeforeRequest()
            .onForwardToSettings { scope: ForwardScope, deniedList: List<String> ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    activity.getString(R.string.allow_permissions_manually),
                    activity.getString(R.string.ok_text),
                    activity.getString(R.string.cancel)
                )
            }
            .request { allGranted: Boolean, grantedList: List<String?>?, deniedList: List<String?>? ->
                if (!allGranted) {
                    AppsKitSDKUtils.makeToast(activity.getString(R.string.some_permission_denied))
                } else {
                    callback.onPermissionProvided()
                }
            }
    }

    private fun createdialog() {
        val dialogBinding = MainDialogChooserBinding.inflate(activity.layoutInflater)
        val dialog = AppsKitSDKUtils.showAlertDialog(
            activity,
            dialogBinding,
            false,
            dialogBinding.close,
            null
        )

        dialog.setOnDismissListener {
            AppsKitSDKAdsManager.loadBanner(
                activity,
                Utils.createAdKeyFromScreenId(ScreenIDs.BIG_BANNER_DIALOG),
                false,
                CollapsibleOrientation.BOTTOM,
                AdSize.MEDIUM_RECTANGLE
            )
        }
        AppsKitSDKAdsManager.showBanner(
            activity,
            dialogBinding.bannerContainer,
            null,
            Utils.createAdKeyFromScreenId(ScreenIDs.BIG_BANNER_DIALOG),
            false,
            null,
            AdSize.MEDIUM_RECTANGLE
        )
        dialogBinding.createFromGallery.setOnClickListener {
            imageSourceSelection()
            dialog.dismiss()
        }
        dialogBinding.createFromAi.setOnClickListener {
            AppsKitSDKUtils.makeToast(activity.getString(R.string.this_feature_is_coming_soon))
            dialog.dismiss()
        }
    }

    private fun imageSourceSelection() {
        val dialogBinding =
            ImageSelectionSourceSelectionDialogBinding.inflate(activity.layoutInflater)
        val dialog = AppsKitSDKUtils.showAlertDialog(
            activity,
            dialogBinding,
            false,
            dialogBinding.close,
            null
        )

        dialog.setOnDismissListener {
            AppsKitSDKAdsManager.loadBanner(
                activity,
                Utils.createAdKeyFromScreenId(ScreenIDs.BIG_BANNER_DIALOG),
                false,
                CollapsibleOrientation.BOTTOM,
                AdSize.MEDIUM_RECTANGLE
            )
        }
        AppsKitSDKAdsManager.showBanner(
            activity,
            dialogBinding.bannerContainer,
            null,
            Utils.createAdKeyFromScreenId(ScreenIDs.BIG_BANNER_DIALOG),
            false,
            null,
            AdSize.MEDIUM_RECTANGLE
        )
        dialogBinding.gallery.setOnClickListener(object :
            DebounceClickListener("SelectFromGallery") {
            override fun onDebouncedClick(v: View?) {
                pickMedia.launch(
                    PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        .build()
                )
                dialog.dismiss()
            }
        })

        dialogBinding.camera.setOnClickListener(object : DebounceClickListener("SelectFromCamera") {
            override fun onDebouncedClick(v: View?) {
                takeImageThroughCamera()
                dialog.dismiss()
            }
        })

    }

}
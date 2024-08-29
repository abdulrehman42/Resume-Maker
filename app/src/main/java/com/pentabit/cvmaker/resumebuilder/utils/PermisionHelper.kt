package com.pentabit.cvmaker.resumebuilder.utils

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.ForwardToSettingsCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.permissionx.guolindev.request.ExplainScope
import com.permissionx.guolindev.request.ForwardScope

object PermisionHelper {

    fun askForPermission(list: List<String>, activity: FragmentActivity, description: String) {
        PermissionX.init(activity)
            .permissions(list)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    description,
                    "OK",
                    "Cancel"
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "Please grant permissions manually in settings.",
                    "OK",
                    "Cancel"
                )
            }
            .request { allGranted, _, deniedList ->
                if (!allGranted) {
                    Toast.makeText(
                        activity,
                        "Permissions denied: ${deniedList?.joinToString()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
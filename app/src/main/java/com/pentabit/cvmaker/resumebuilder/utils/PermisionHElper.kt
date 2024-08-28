package com.pentabit.cvmaker.resumebuilder.utils

import androidx.fragment.app.FragmentActivity
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.ForwardToSettingsCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.permissionx.guolindev.request.ExplainScope
import com.permissionx.guolindev.request.ForwardScope

object PermisionHElper {

    fun askForPermission(list: List<String>, activity: FragmentActivity, description: String) {
        PermissionX.init(activity).permissions(list)
            .onExplainRequestReason(ExplainReasonCallback { scope: ExplainScope, deniedList: List<String?>? ->
                scope.showRequestReasonDialog(
                    deniedList as List<String>,
                    description,
                    activity.getString(R.string.ok_text),
                    activity.getString(R.string.cancel)
                )
            }).explainReasonBeforeRequest()
            .onForwardToSettings(ForwardToSettingsCallback { scope: ForwardScope, deniedList: List<String?>? ->
                scope.showForwardToSettingsDialog(
                    deniedList as List<String>,
                    activity.getString(R.string.allow_permissions_manually),
                    activity.getString(R.string.ok_text),
                    activity.getString(R.string.cancel)
                )
            })
            .request(RequestCallback { allGranted: Boolean, _: List<String?>?, deniedList: List<String?>? ->
                if (!allGranted) {
                    AppsKitSDKUtils.makeToast(
                        activity.getString(R.string.some_permission_denied)
                    )
                }
            })
    }
}
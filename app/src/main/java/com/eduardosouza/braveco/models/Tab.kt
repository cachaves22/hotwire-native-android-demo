package com.camilasouza.sampleapp.models

import androidx.annotation.IdRes
import com.camilasouza.sampleapp.R

data class Tab(
    val name: String,
    val path: String,
    @IdRes val menuId: Int,
    @IdRes val navigatorHostId: Int
) {
    companion object {
        val all = arrayOf(
            Tab(
                name = "home",
                path = "natives",
                menuId = R.id.bottom_nav_home,
                navigatorHostId = R.id.sampleapp_nav_host,
            ),
            Tab(
                name = "content",
                path = "natives/content",
                menuId = R.id.bottom_nav_content,
                navigatorHostId = R.id.content_nav_host,
            ),
            Tab(
                name = "groups",
                path = "natives/groups",
                menuId = R.id.bottom_nav_groups,
                navigatorHostId = R.id.groups_nav_host,
            ),
            Tab(
                name = "recordings",
                path = "natives/recordings",
                menuId = R.id.bottom_nav_account,
                navigatorHostId = R.id.recordings_nav_host,
            ),
            Tab(
                name = "account",
                path = "natives/account",
                menuId = R.id.bottom_nav_account,
                navigatorHostId = R.id.account_nav_host,
            )

        )
    }
}
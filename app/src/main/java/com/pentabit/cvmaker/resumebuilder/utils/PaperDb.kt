package com.pentabit.cvmaker.resumebuilder.utils

import io.paperdb.Paper


fun addToken(user_token: String?) {
    Paper.book(Constants.TOKEN).write("token", user_token.toString())
}


fun getToken(): String? {
    val keys = Paper.book(Constants.TOKEN).allKeys
    if (keys.size == 0) return ""
    return Paper.book(Constants.TOKEN).read("token")
}

fun deleteToken(){
    Paper.book(Constants.TOKEN).delete("token")
}

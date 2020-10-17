package com.norbertneudert.openmygarage.service.models

import com.squareup.moshi.Json

data class LoginViewModel (
    @Json(name = "Username")
    var username: String,

    @Json(name = "Password")
    var password: String
)
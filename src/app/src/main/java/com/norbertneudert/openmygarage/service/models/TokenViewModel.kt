package com.norbertneudert.openmygarage.service.models

import com.squareup.moshi.Json

data class TokenViewModel (
    @Json(name = "token")
    var token: String,

    @Json(name = "expiration")
    var expiration: String
)
package com.norbertneudert.openmygarage.service.models

import com.squareup.moshi.Json
import java.util.*

data class TokenViewModel (
    @Json(name = "token")
    var token: String,

    @Json(name = "expiration")
    var expiration: Date
)
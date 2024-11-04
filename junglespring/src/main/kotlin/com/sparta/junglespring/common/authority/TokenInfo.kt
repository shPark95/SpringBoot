package com.sparta.junglespring.common.authority

data class TokenInfo(
    val grantType: String,
    val accessToken: String,
)
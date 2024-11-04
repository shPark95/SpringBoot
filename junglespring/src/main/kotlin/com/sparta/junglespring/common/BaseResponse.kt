package com.sparta.junglespring.common

import com.sparta.junglespring.common.status.ResultCode

data class BaseResponse<T> (
    val resultCode: String = ResultCode.SUCCESS.name,
    val data: T? = null,
    val message: String = ResultCode.SUCCESS.msg,
)

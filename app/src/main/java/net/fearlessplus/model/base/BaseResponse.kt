package net.fearlessplus.model.base

abstract class BaseResponse {
    abstract var result: Result
    open fun sync() {}
}

data class Result(var code: Int? = 0, var message: String? = "", var accessToken: String = "")
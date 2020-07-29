package com.example.routingapp.model.FoursqureModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by Zohre Niayeshi on 07,July,2020 niayesh1993@gmail.com
 **/
class Meta {

    @SerializedName("code")
    @Expose
    private var code = 0
    @SerializedName("requestId")
    @Expose
    private var requestId: String? = null
    @SerializedName("errorType")
    @Expose
    private var errorType: String? = null
    @SerializedName("errorDetail")
    @Expose
    private var errorDetail: String? = null

    fun getErrorType(): String? {
        return errorType
    }

    fun getErrorDetail(): String? {
        return errorDetail
    }

    fun setErrorType(errorType: String?) {
        this.errorType = errorType
    }

    fun setErrorDetail(errorDetail: String?) {
        this.errorDetail = errorDetail
    }

    fun getCode(): Int {
        return code
    }

    fun getRequestId(): String? {
        return requestId
    }

    fun setCode(code: Int) {
        this.code = code
    }

    fun setRequestId(requestId: String?) {
        this.requestId = requestId
    }

}
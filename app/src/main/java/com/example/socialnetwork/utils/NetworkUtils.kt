package com.example.socialnetwork.utils

import android.content.Context
import com.example.socialnetwork.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.Response
import retrofit2.HttpException
import java.io.IOException

sealed class myResult<out T> {

    data class Success<out T>(val value: T) : myResult<T>()
    data class GenericError(val error: String?) : myResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success: $value"
            is GenericError -> "ErrorGeneric"

        }
    }
}

fun firebaseErrors(error: String?, context: Context) {

    when (error) {
        context.getString(R.string.networkError) ->
            makeToast(
                context,
                "Please check your conection"
            )
        context.getString(R.string.formatError) ->
            makeToast(
                context,
                "Please check your Email format"
            )
        context.getString(R.string.emailInUse) ->
            makeToast(
                context,
                "Email in use try to login with Google account"
            )
        context.getString(R.string.apiException) -> makeToast(
            context,
            "Choice an option"
        )
        context.getString(R.string.cancelByUser) ->
            makeToast(context, "cancel by user")
        else -> makeToast(
            context,
            context.getString(R.string.loginFailed)
        )
    }
}


suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ResultWrapper<T> {

    return withContext(dispatcher) {
        try {
            val response = apiCall.invoke()
            if (response is Response && !response.isSuccessful) {
                ResultWrapper.GenericError(
                    null,
                    response.message()
                )
            } else ResultWrapper.Success(response)

        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException ->
                    ResultWrapper.GenericError(
                        null,
                        "${throwable.localizedMessage}"
                    )
                is HttpException ->
                    ResultWrapper.NetworkError(
                        throwable
                    )
                else ->
                    ResultWrapper.GenericError(
                        null,
                        "${throwable.localizedMessage}"
                    )
            }
        }
    }
}

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: String? = null) :
        ResultWrapper<Nothing>()
    data class NetworkError(val throwable: HttpException) : ResultWrapper<Nothing>()
}
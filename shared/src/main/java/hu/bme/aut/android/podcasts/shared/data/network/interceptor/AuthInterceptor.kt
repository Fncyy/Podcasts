package hu.bme.aut.android.podcasts.shared.data.network.interceptor

import hu.bme.aut.android.podcasts.shared.BuildConfig
import okhttp3.Request

class AuthInterceptor : UrlInterceptor() {
    override fun Request.Builder.update(request: Request) {
        addHeader("X-ListenAPI-Key", BuildConfig.API_KEY)
    }
}
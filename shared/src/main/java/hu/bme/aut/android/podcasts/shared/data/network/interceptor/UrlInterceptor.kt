package hu.bme.aut.android.podcasts.shared.data.network.interceptor

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

abstract class UrlInterceptor : Interceptor {

    final override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url
        val newRequest = request.newBuilder().apply {
            url(
                url.newBuilder().apply {
                    update(url)
                }.build()
            )
            update(request)
        }.build()
        return chain.proceed(newRequest)
    }

    protected open fun HttpUrl.Builder.update(url: HttpUrl) {}

    protected open fun Request.Builder.update(request: Request) {}
}
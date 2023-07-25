package ai.voya.testapp.image_downloader

import ai.voya.testapp.cache.ImageCache
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import okhttp3.*
import java.io.IOException
import java.net.URL

interface ImageDownloader {
    fun downloadImage(url: URL, completion: (Result<Drawable?>) -> Unit)
}

class DefaultImageDownloader(val cache: ImageCache): ImageDownloader {

    private val client = OkHttpClient()
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    override fun downloadImage(url: URL, completion: (Result<Drawable?>) -> Unit) {
        val image = cache.getImage(url.toString())
        image?.let {
            completion(Result.success(image))
            return
        }

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                completion(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val body = response.body?.byteStream()
                    if (!response.isSuccessful || body == null) {
                        mainThreadHandler.run {
                            completion(Result.failure(IOException("Unexpected response $response")))
                        }
                    } else {
                        try {
                            val drawable = Drawable.createFromStream(body, "Beer image")
                            mainThreadHandler.run {
                                completion(Result.success(drawable))
                            }
                        } catch (e: Exception) {
                            mainThreadHandler.run {
                                completion(Result.failure(IOException("Failed to parse $e")))
                            }
                        }
                    }
                }
            }
        })
    }
}
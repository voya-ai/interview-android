package ai.voya.testapp.model

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException

class ApiClient {

    private val client = OkHttpClient()
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    fun fetchBeers(completion: (Result<List<Beer>>) -> Unit) {
        val request = Request.Builder()
            .url("https://api.punkapi.com/v2/beers")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                completion(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val body = response.body?.string()
                    if (!response.isSuccessful || body == null) {
                        mainThreadHandler.post {
                            completion(Result.failure(IOException("Unexpected response $response")))
                        }
                    } else {
                        try {
                            val beerListType = object : TypeToken<List<Beer>>() {}.type
                            val beers = Gson().fromJson<List<Beer>>(body, beerListType)
                            mainThreadHandler.post {
                                completion(Result.success(beers))
                            }
                        } catch (e: Exception) {
                            mainThreadHandler.post {
                                completion(Result.failure(IOException("Failed to parse $e")))
                            }
                        }
                    }
                }
            }
        })
    }
}
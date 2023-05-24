package ai.voya.testapp.cache

import android.graphics.drawable.Drawable

interface ImageCache {
    fun setImage(image: Drawable?, key: String)
    fun getImage(key: String): Drawable?
}

class DefaultImageCache: ImageCache {

    private var cache = hashMapOf<String, Drawable>()

    override fun setImage(image: Drawable?, key: String) {
        image?.let {
            cache[key] = it
        } ?: run {
            cache.remove(key)
        }
    }

    override fun getImage(key: String): Drawable? {
        return cache[key]
    }
}

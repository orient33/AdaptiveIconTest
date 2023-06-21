package a.test.adaptive

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.AdaptiveIconDrawable2
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IconVM(val app: Application) : AndroidViewModel(app) {

    val mLiveData = MutableLiveData<List<DrawableData>>()

    fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<DrawableData>()
            createAdaptiveIcon2(list)
            mLiveData.postValue(list)
        }
    }

    //create adaptiveIcon2
    private fun createAdaptiveIcon2(list: MutableList<DrawableData>) {
        val resources = app.resources
        val density = resources.displayMetrics.density
        val bgSize = 108 * density
        val size = 72 * density
        val padding = 18 * density
        val fgBitmap = Bitmap.createBitmap(bgSize.toInt(), bgSize.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(fgBitmap)
        val paint = Paint()
        canvas.drawColor(Color.GREEN)
        paint.color = Color.BLUE
        canvas.drawRect(padding, padding, padding + size, padding + size, paint)
        paint.color = Color.RED
        canvas.drawCircle(bgSize / 2f, bgSize / 2f, size / 2f, paint)
        canvas.setBitmap(null)
        val bg = Bitmap.createBitmap(fgBitmap.width, fgBitmap.height, Bitmap.Config.ARGB_4444)
        canvas.setBitmap(bg)
        canvas.drawColor(Color.WHITE)
        canvas.setBitmap(null)

        val background = BitmapDrawable(resources, bg)
        val foreground = BitmapDrawable(resources, fgBitmap)
        val drawable2 = AdaptiveIconDrawable2(background, foreground)
        val adaptiveIconDrawable = AdaptiveIconDrawable(background, foreground)

        list.add(DrawableData("square.mask", drawable2))
        list.add(DrawableData("miui.mask", adaptiveIconDrawable))
    }
}
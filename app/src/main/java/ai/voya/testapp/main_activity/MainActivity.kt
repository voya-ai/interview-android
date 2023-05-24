package ai.voya.testapp.main_activity

import ai.voya.testapp.databinding.ActivityMainBinding
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {

    private lateinit var mainAdapter: MainActivityAdapter
    private lateinit var mainActivityPresenter: MainActivityPresenter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainAdapter = MainActivityAdapter(this)

        binding.recyclerView.apply {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            divider.setDrawable(ShapeDrawable().apply {
                paint.color = Color.GRAY
                intrinsicHeight = 1
            })
            addItemDecoration(divider)
        }
    }
}
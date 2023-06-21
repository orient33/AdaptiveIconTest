package a.test.adaptive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var mRv: RecyclerView
    private lateinit var mVM: IconVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRv = findViewById(R.id.recycler_view)
        mVM = ViewModelProvider(this)[IconVM::class.java]
        val adapter = AAdapter(object : DiffUtil.ItemCallback<DrawableData>() {
            override fun areItemsTheSame(o: DrawableData, n: DrawableData): Boolean {
                return o.name == n.name
            }

            override fun areContentsTheSame(o: DrawableData, n: DrawableData): Boolean {
                return o.name == n.name && o.drawable.equals(n.drawable)
            }
        })
        mRv.adapter = adapter
        mRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRv.addItemDecoration(DividerItemDecoration(this, LinearLayout.HORIZONTAL))

        mVM.mLiveData.observe(this) {
            adapter.submitList(it)
        }

        mVM.load()
    }

    class ImageVH(r: View) : RecyclerView.ViewHolder(r) {
        private val imageView: ImageView = r.findViewById(R.id.image)
        private val textView: TextView = r.findViewById(R.id.text)
        fun bind(bd: DrawableData) {
            imageView.setImageDrawable(bd.drawable)
            textView.text = bd.name
        }
    }

    class AAdapter(diff: DiffUtil.ItemCallback<DrawableData>) :
        ListAdapter<DrawableData, ImageVH>(diff) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageVH {
            return ImageVH(
                LayoutInflater.from(parent.context).inflate(R.layout.item_bitmap, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ImageVH, position: Int) {
            holder.bind(getItem(position))
        }
    }
}
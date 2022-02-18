package net.fearlessplus.ui.main.gallery.viewer

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.map
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import io.sbox.library.extensions.pageChangeListener
import io.sbox.library.extensions.safeClick
import io.sbox.library.extensions.trace
import io.sbox.library.view.paging.PagingAdapter
import kotlinx.android.synthetic.main.fragment_gallery_browser.*
import kotlinx.android.synthetic.main.view_gallery_gif.view.*
import kotlinx.android.synthetic.main.view_gallery_image.view.*
import kotlinx.android.synthetic.main.view_gallery_image.view.gallery_thumb
import kotlinx.android.synthetic.main.view_gallery_video.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.fearlessplus.R
import net.fearlessplus.extensions.load
import net.fearlessplus.model.GalleryContentType
import net.fearlessplus.model.GalleryItem
import net.fearlessplus.ui.base.BaseFragment
import net.fearlessplus.ui.main.gallery.GalleryListFragment
import net.fearlessplus.ui.main.gallery.paging.GalleryViewModel
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import java.io.*


@Suppress("DEPRECATION")
class GalleryBrowserFragment : BaseFragment {


    companion object {

        @JvmStatic
        fun newInstance(bundle: Bundle?) =
            GalleryBrowserFragment().apply {
                arguments = bundle
            }

        @JvmStatic
        fun getBundle(
            position: Int
        ) = bundleOf(
            "position" to position
        )

        var isStart = false
        var selectPosition = 0
    }


    private var allImages: List<GalleryItem> = listOf()
    private var animeContx: Context? = null

    constructor() {
        this.allImages = GalleryListFragment.allImages
        animeContx = context
    }

    constructor(allImages: List<GalleryItem>, imagePosition: Int, anim: Context?) {
        this.allImages = allImages
        selectPosition = imagePosition
        animeContx = anim
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery_browser, container, false)
    }

//    private val viewModel by activityViewModels<PagingAdapter.PagingViewModel<GalleryItem>>()
    private val viewModel by activityViewModels<GalleryViewModel>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            selectPosition = it.getInt("position", 0)
        }


        viewpager.pageChangeListener({
            isStart = false
//                this@GalleryBrowserFragment.dispatcherEvent("VIDEO_STATE_CHANAGE")
        }, {
            selectPosition = it
            if (allImages.get(it).isVideoType) {
                isStart = true
            }
            val v: View? = viewpager.findViewWithTag(selectPosition)
            v?.gallery_video?.let { videoView ->
                videoView.start()
            }
        })
        viewpager?.adapter = PagerAdapter(allImages)
        viewpager?.setCurrentItem(selectPosition, false)

        btn_content_download.visibility = View.GONE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            btn_content_download.visibility = View.VISIBLE

            btn_content_download.safeClick {

                downloadManager =
                    activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

                val intentFilter = IntentFilter()
                intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
                activity?.registerReceiver(onDownloadComplete, intentFilter)


                downloadFile(allImages.get(selectPosition))
            }
        }

    }

    private var downloadId: Long = -1L
    private lateinit var downloadManager: DownloadManager

    @RequiresApi(Build.VERSION_CODES.N)
    fun downloadFile(item: GalleryItem) {
//        val file = File(activity?.getExternalFilesDir(null), "dev_submit.mp4")

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString(), item.name
        )
        val youtubeUrl = item.contentUrl
        val request = DownloadManager.Request(Uri.parse(youtubeUrl))
            .setTitle("Downloading a content")
            .setDescription("Downloading....")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationUri(Uri.fromFile(file))
            .setRequiresCharging(false)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        downloadId = downloadManager.enqueue(request)
    }

    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.action)) {
                if (downloadId == id) {
                    val query: DownloadManager.Query = DownloadManager.Query()
                    query.setFilterById(id)
                    var cursor = downloadManager.query(query)
                    if (!cursor.moveToFirst()) {
                        return
                    }

                    var columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    var status = cursor.getInt(columnIndex)
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        Toast.makeText(context, "Download succeeded", Toast.LENGTH_SHORT).show()
                    } else if (status == DownloadManager.STATUS_FAILED) {
                        Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.action)) {
                Toast.makeText(context, "Notification clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }


    class PagerAdapter(private val vo: List<GalleryItem>) :
        androidx.viewpager.widget.PagerAdapter() {
        override fun getCount(): Int {
            return vo.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object` as View
        }


        override fun destroyItem(containerCollection: ViewGroup, position: Int, view: Any) {
            (containerCollection as ViewPager).removeView(view as View)
        }


        override fun instantiateItem(containerCollection: ViewGroup, position: Int): Any {
            val layoutinflater: LayoutInflater = containerCollection.getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
            ) as LayoutInflater

            val data = vo.get(position)
            val view: View = when (vo.get(position).contentType) {
                GalleryContentType.VIDEO.index -> {
                    layoutinflater.inflate(R.layout.view_gallery_video, null).apply {
                        gallery_video.apply {
                            val controller = MediaController(context)
                            setMediaController(controller)
                            requestFocus()

                            setOnPreparedListener {
                                if (isStart && selectPosition == position) {
                                    start()
                                }
                            }
                            setOnCompletionListener {
                            }
                            setVideoURI(Uri.parse(data.contentUrl))
                            seekTo(0)
                        }
                    }
                }
                GalleryContentType.GIF.index -> {
                    layoutinflater.inflate(R.layout.view_gallery_gif, null).apply {
                        gallery_thumb.load(data.contentThumb)

                        Glide.with(gallery_gif)
                            .asGif()
                            .load(data.contentUrl)
                            .apply(RequestOptions().fitCenter())
                            .into(gallery_gif)
                    }
                }
                else -> {
                    layoutinflater.inflate(R.layout.view_gallery_image, null).apply {
                        gallery_thumb.load(data.contentThumb)

                        Glide.with(this)
                            .asBitmap()
                            .load(data.contentUrl)
                            .apply(RequestOptions().fitCenter())
                            .listener(object : RequestListener<Bitmap> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Bitmap>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Bitmap?,
                                    model: Any?,
                                    target: Target<Bitmap>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    resource?.let {
                                        CoroutineScope(Main).launch {
                                            gallery_thumb.visibility = View.GONE
                                            gallery_content.setImageBitmap(it)
                                        }
                                    }
                                    return false
                                }
                            }).submit()
                    }
                }
            }

            view.setTag(position)

            (containerCollection as ViewPager).addView(view)
            return view
        }

    }


}
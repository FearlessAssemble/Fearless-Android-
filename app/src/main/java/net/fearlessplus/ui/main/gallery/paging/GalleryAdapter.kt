package net.fearlessplus.ui.main.gallery.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.sbox.library.extensions.trace
import kotlinx.android.synthetic.main.cell_gallery_date.view.*
import net.fearlessplus.databinding.*
import net.fearlessplus.ui.main.gallery.GalleryListFragment

class GalleryAdapter constructor(val clickListener: GalleryListFragment.OnClickItemListener) : PagingDataAdapter<GalleryModel, RecyclerView.ViewHolder>(diffCallback) {

    companion object {
        val TOP_BOTTOM = 0
        val DATA = 1
        val SEPARATOR_LINE = 2
        val SEPARATOR_DATE = 3

        private val diffCallback = object : DiffUtil.ItemCallback<GalleryModel>() {
            override fun areItemsTheSame(oldItem: GalleryModel, newItem: GalleryModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: GalleryModel, newItem: GalleryModel): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        try {
            val item = getItem(position) ?: return -1


//        return item.index
            return when (item) {
                is GalleryModel.TopBottom -> TOP_BOTTOM
                is GalleryModel.Data -> DATA
                is GalleryModel.SeparatorLine -> SEPARATOR_LINE
                is GalleryModel.SeparatorDate -> SEPARATOR_DATE
            }
        } catch (e: Exception) {

        }
        return -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            DATA -> PagingContentViewHolder(CellGalleryListBinding.inflate(layoutInflater, parent, false))

            TOP_BOTTOM -> PagingHeaderViewHolder(
                CellGalleryHeaderBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
            SEPARATOR_LINE -> PagingSeparatorLineViewHolder(
                CellGalleryLineBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
            SEPARATOR_DATE -> PagingSeparatorDateViewHolder(
                CellGalleryDateBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
            else -> throw Exception()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            when (holder) {
                is PagingHeaderViewHolder -> holder.bind(item as GalleryModel.TopBottom)
                is PagingContentViewHolder -> holder.bind(item as GalleryModel.Data, clickListener)
                is PagingSeparatorDateViewHolder -> holder.bind(item as GalleryModel.SeparatorDate)
            }
        }
    }


}


class PagingHeaderViewHolder(
    private val binding: CellGalleryHeaderBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: GalleryModel.TopBottom) {
        binding.headerTitle.text = data.title
    }
}

class PagingContentViewHolder(
    private val binding: CellGalleryListBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: GalleryModel.Data, clickListener: GalleryListFragment.OnClickItemListener) {
        binding.data = data.value
        binding.listener = clickListener
    }
}

class PagingSeparatorLineViewHolder(binding: CellGalleryLineBinding) :
    RecyclerView.ViewHolder(binding.root)

class PagingSeparatorDateViewHolder(val binding: CellGalleryDateBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: GalleryModel.SeparatorDate) {
        if(data.date.contains("9999"))
            binding.root.gallery_date.text = "Notice"
        else
            binding.root.gallery_date.text = data.date
    }
}
package io.sbox.library.view.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ObservableArrayList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter<T : ViewDataBinding, D>(
    val context: Context,
    @LayoutRes val cell: Int,
    val bindingVariableId: Int? = null,
    val bindingListener: (Pair<Int, Any>)? = null
) : RecyclerView.Adapter<RecyclerViewHolder<T>>() {

    init {
        setHasStableIds(true)
    }

    var items: ArrayList<D>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        object : RecyclerViewHolder<T>(cell, parent, bindingVariableId, bindingListener) {}

    override fun onBindViewHolder(holder: RecyclerViewHolder<T>, position: Int) {
        items?.let {
            holder.onBindViewHolder(it.get(position))
        }
    }

    override fun onViewRecycled(holder: RecyclerViewHolder<T>) {
        super.onViewRecycled(holder)
//        holder.binding?.root?.destroy()
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun addItem(item: ObservableArrayList<D>?) {
        items = item
    }
}

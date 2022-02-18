package io.sbox.library.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class PagedAdapter<T : ViewDataBinding, D : Any>(
    val context: Context,
    @LayoutRes val cell: Int,
    val bindingVariableId: Int? = null,
    val bindingListener: (Pair<Int, Any>)? = null
) :
    PagingDataAdapter<D, RecyclerViewHolder<T>>(object : DiffUtil.ItemCallback<D>() {
        override fun areItemsTheSame(oldItem: D, newItem: D): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: D, newItem: D): Boolean =
            oldItem == newItem
    }) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        object : RecyclerViewHolder<T>(cell, parent, bindingVariableId, bindingListener) {}

    override fun onBindViewHolder(holder: RecyclerViewHolder<T>, position: Int) {

        val item = getItem(position)
        if(removeItems.contains(item)) {
            holder.binding?.root?.visibility = View.GONE
            holder.binding?.root?.layoutParams = RecyclerView.LayoutParams(0, 0)
        } else {
            holder.binding?.root?.visibility = View.VISIBLE
            holder.binding?.root?.layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )

            holder.onBindViewHolder(item)
        }
    }

    private val removeItems = arrayListOf<D>()
    fun removeItem(item: D) {
        removeItems.add(item)
    }

    var itemCountReal: Int = 0
        get() = super.getItemCount() - removeItems.size


    override fun getItemCount(): Int {
        return super.getItemCount()
    }



/*
    override fun submitList(pagedList: PagedList<D>?) {
        pagedList?.addWeakCallback(dataSet, object : PagedList.Callback() {
            override fun onChanged(position: Int, count: Int) {
                dataSet.clear()
                dataSet.addAll(pagedList)
            }

            override fun onInserted(position: Int, count: Int) {
                dataSet.clear()
                dataSet.addAll(pagedList)
            }

            override fun onRemoved(position: Int, count: Int) {
                dataSet.clear()
                dataSet.addAll(pagedList)
            }
        })
        super.submitList(pagedList)
    }
*/


}
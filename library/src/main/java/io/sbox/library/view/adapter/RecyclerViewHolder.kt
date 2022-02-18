package io.sbox.library.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class RecyclerViewHolder<T : ViewDataBinding>(
    @LayoutRes cell: Int,
    parent: ViewGroup,
    val bindingVariableId: Int? = null,
    val bindingListener: (Pair<Int, Any>)? = null
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(cell, parent, false)) {
    val binding: T? = DataBindingUtil.bind(itemView)
    fun onBindViewHolder(item: Any?) {
        try {
            bindingVariableId?.let {
                binding?.setVariable(it, item)
            }
            bindingListener?.let {
                bindingListener.first?.let {
                    binding?.setVariable(it, bindingListener.second)
                }
            }
        } catch (e: Exception) {
        }
    }
}
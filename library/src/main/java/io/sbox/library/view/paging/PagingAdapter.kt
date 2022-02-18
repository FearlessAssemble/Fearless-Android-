package io.sbox.library.view.paging

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/*
private val pagingAdapter by lazy {
        PagingAdapter<CellBinding, VO>(
            R.layout.cell, BR.data
        )
    }
 */
class PagingAdapter<T : ViewDataBinding, V : Any>(
    @LayoutRes val cellResId: Int,
    val bindingVariableId: Int? = null,
    val bindingListener: (Pair<Int, Any>)? = null
) :
    PagingDataAdapter<V, PagingAdapter.RecyclerViewHolder<T>>(object : DiffUtil.ItemCallback<V>() {
        override fun areItemsTheSame(oldItem: V, newItem: V): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: V, newItem: V): Boolean =
            oldItem == newItem
    }) {

    class PagingLoadStateAdapter(
        private val retry: () -> Unit
    ) : LoadStateAdapter<PagingLoadStateAdapter.PagingLoadStateViewHolder>() {

        class PagingLoadStateViewHolder internal constructor(
            private val textView: TextView,
            private val retry: () -> Unit
        ) : RecyclerView.ViewHolder(textView) {

            fun bind(state: LoadState) {
                textView.text = when {
                    state is LoadState.Error -> {
                        textView.setOnClickListener { retry() }
                        (state as? LoadState.Error)?.error?.message ?: ""
                    }
                    state is LoadState.Loading -> "Loading..."
                    else -> ""
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
            PagingLoadStateViewHolder(TextView(parent.context), retry)

        override fun onBindViewHolder(holder: PagingLoadStateViewHolder, loadState: LoadState) =
            holder.bind(loadState)
    }

    init {
        withLoadStateHeaderAndFooter(
            PagingLoadStateAdapter { retry() },
            PagingLoadStateAdapter { retry() }
        )
    }


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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        object : RecyclerViewHolder<T>(cellResId, parent, bindingVariableId, bindingListener) {}

    override fun onBindViewHolder(holder: RecyclerViewHolder<T>, position: Int) {
        val item = getItem(position)
        if (removeItems.contains(item)) {
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


    /*
    viewModel.data.observe(this@MainActivity, Observer {
            lifecycleScope.launch {
                adapter.submitData(it)
            }
        })
     */

    class PagingViewModel<V : Any> : ViewModel() {
        init {
            viewModelScope.launch {

            }
        }

        val data: MutableLiveData<PagingData<V>> = MutableLiveData()

        fun getPagingData(pagingSource: PagingSource<V>, pageSize: Int): Flow<PagingData<V>> {
            return Pager(PagingConfig(pageSize = pageSize)) {
                pagingSource
            }.flow.cachedIn(viewModelScope)
        }
    }

    /*
//        fragment.lifecycleScope.launch {
//            PagingViewModel(pagingSource, pageSize).pagingData.collectLatest {
//                this@PagingAdapter.submitData(it)
//            }
//        }
    class PagingViewModel<V : Any> constructor(pagingSource: PagingSource<V>, pageSize: Int) :
        ViewModel() {
        val pagingData = Pager(PagingConfig(pageSize = pageSize)) {
            pagingSource
        }.flow.cachedIn(viewModelScope)
    }
    */

    fun addPagingSource(
        fragment: Fragment,
        pagingSource: PagingSource<V>,
        pageSize: Int
    ): PagingAdapter<T, V> {
        fragment.lifecycleScope.launch {
            fragment.viewModels<PagingViewModel<V>>().value.apply {
                getPagingData(pagingSource, pageSize)
                    .collectLatest {
                        this@PagingAdapter.submitData(it)
                        data.postValue(it)
                    }
            }
        }
        return this
    }

    private val removeItems = arrayListOf<V>()
    fun removeItem(item: V) {
        removeItems.add(item)
    }

    var itemCountReal: Int = 0
        get() = super.getItemCount() - removeItems.size


    fun create() {

    }


}

/*
class PagingViewModel<V : Any> constructor(context: Fragment, pagingSource: PagingSource<V>): ViewModel() {
    init {
        context.lifecycleScope.launch {
        }
    }
    fun create(simpleType: Boolean = true) {
    }
}
lifecycleScope.launch {
    viewModel.getPagingData(tag).collectLatest {
        pagingAdapter.submitData(it)
    }
}


*/

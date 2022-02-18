package net.fearlessplus.ui.main.information

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_schedule_header.view.*
import net.fearlessplus.databinding.CellScheduleListBinding
import net.fearlessplus.databinding.ViewScheduleHeaderBinding
import net.fearlessplus.model.ScheduleItem

class ScheduleAdapter(
    private val items: ObservableArrayList<ScheduleItem>
) : RecyclerView.Adapter<ScheduleAdapter.StickyHeaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StickyHeaderViewHolder(
        CellScheduleListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: StickyHeaderViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class StickyHeaderViewHolder(
        private val binding: CellScheduleListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ScheduleItem) {

            binding.data = item
            if(item.isNewMonth)
                binding.root.view_schedule_header_month.text = "${item.month}월"
            if(item.isNewDay)
                binding.root.view_schedule_header_day.text = item.day
            binding.root.view_schedule_header_layout.visibility = if (item.isHeader) View.VISIBLE else View.INVISIBLE

        }
    }

    fun isHeader(position: Int) = items[position].isHeader

    fun getHeaderView(list: RecyclerView, position: Int): View? {
        val item = items[position]

        val binding = ViewScheduleHeaderBinding.inflate(LayoutInflater.from(list.context), list, false)
        binding.root.view_schedule_header_month.text = "${item.month}월"
        binding.root.view_schedule_header_day.text = item.day

        binding.data = item

        return binding.root
    }
}
package org.wordpress.android.ui.jetpack.scan.details.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import org.wordpress.android.ui.jetpack.common.JetpackListItemState
import org.wordpress.android.ui.jetpack.common.ViewType
import org.wordpress.android.ui.jetpack.common.viewholders.JetpackDescriptionViewHolder
import org.wordpress.android.ui.jetpack.common.viewholders.JetpackIconViewHolder
import org.wordpress.android.ui.jetpack.common.viewholders.JetpackViewHolder
import org.wordpress.android.ui.utils.UiHelpers
import org.wordpress.android.util.image.ImageManager

class ThreatDetailsAdapter(
    private val imageManager: ImageManager,
    private val uiHelpers: UiHelpers
) : Adapter<JetpackViewHolder>() {
    private val items = mutableListOf<JetpackListItemState>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JetpackViewHolder {
        return when (viewType) {
            ViewType.ICON.id -> JetpackIconViewHolder(imageManager, parent)
            ViewType.DESCRIPTION.id -> JetpackDescriptionViewHolder(uiHelpers, parent)
            else -> throw IllegalArgumentException("Unexpected view type in ${this::class.java.simpleName}")
        }
    }

    override fun onBindViewHolder(holder: JetpackViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemViewType(position: Int) = items[position].type.id

    override fun getItemId(position: Int): Long = items[position].longId()

    override fun getItemCount() = items.size

    fun update(newItems: List<JetpackListItemState>) {
        val diffResult = DiffUtil.calculateDiff(ThreatDetailsDiffCallback(items.toList(), newItems))
        items.clear()
        items.addAll(newItems)

        diffResult.dispatchUpdatesTo(this)
    }

    class ThreatDetailsDiffCallback(
        private val oldList: List<JetpackListItemState>,
        private val newList: List<JetpackListItemState>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            if (oldItem::class != newItem::class) {
                return false
            }
            return oldItem.longId() == newItem.longId()
        }

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
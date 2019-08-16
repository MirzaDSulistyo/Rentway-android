package id.rent.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import id.rent.android.R
import id.rent.android.model.Favorite
import id.rent.android.ui.common.DataBoundListAdapter
import id.rent.android.databinding.ContentFavoriteBinding
import id.rent.android.utility.AppExecutors

class FavoriteAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val clickCallback: ((Favorite) -> Unit?)
) : DataBoundListAdapter<Favorite, ContentFavoriteBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Favorite>() {
        override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem.user == newItem.user
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ContentFavoriteBinding {

        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.content_favorite,
            parent,
            false,
            dataBindingComponent
        )
    }

    override fun bind(binding: ContentFavoriteBinding, item: Favorite, position: Int) {
        binding.product = item.product
        binding.position = position

        binding.contentView.setOnClickListener {
            binding.product?.let {
                clickCallback.invoke(item)
            }
        }

    }
}
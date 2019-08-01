package id.rent.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import id.rent.android.R
import id.rent.android.databinding.ContentProductBinding
import id.rent.android.model.Product
import id.rent.android.ui.common.DataBoundListAdapter
import id.rent.android.utility.AppExecutors
import timber.log.Timber

class ProductAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val clickCallback: ((Product) -> Unit?)
) : DataBoundListAdapter<Product, ContentProductBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.price == newItem.price && oldItem.brand == newItem.brand
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ContentProductBinding {
        val binding = DataBindingUtil.inflate<ContentProductBinding>(
            LayoutInflater.from(parent.context),
            R.layout.content_product,
            parent,
            false,
            dataBindingComponent
        )

        binding.cvProduct.setOnClickListener {
            binding.product?.let {
                clickCallback.invoke(it)
            }
        }

        return binding
    }

    override fun bind(binding: ContentProductBinding, item: Product, position: Int) {
        binding.product = item
        binding.position = position

        Timber.d("product ${item.createdAt}")
        Timber.d("product ${item.updatedAt}")
    }
}
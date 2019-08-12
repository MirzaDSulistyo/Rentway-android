package id.rent.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import id.rent.android.R
import id.rent.android.databinding.ContentProductMasterBinding
import id.rent.android.model.Product
import id.rent.android.ui.common.DataBoundListAdapter
import id.rent.android.utility.AppExecutors

class MasterProductAdapter(
    appExecutors: AppExecutors,
    private val clickCallback: ((Product) -> Unit?)
) : DataBoundListAdapter<Product, ContentProductMasterBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ContentProductMasterBinding {
        val binding = DataBindingUtil.inflate<ContentProductMasterBinding>(
            LayoutInflater.from(parent.context),
            R.layout.content_product_master,
            parent,
            false
        )

        binding.contentView.setOnClickListener {
            binding.data?.let {
                clickCallback.invoke(it)
            }
        }

        return binding
    }

    override fun bind(binding: ContentProductMasterBinding, item: Product, position: Int) {
        binding.data = item
        binding.position = position

    }
}
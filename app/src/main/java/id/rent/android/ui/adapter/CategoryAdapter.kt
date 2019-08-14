package id.rent.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import id.rent.android.R
import id.rent.android.databinding.ContentSelectCategoryBinding
import id.rent.android.model.Category
import id.rent.android.ui.common.DataBoundListAdapter
import id.rent.android.utility.AppExecutors

class CategoryAdapter(
        appExecutors: AppExecutors,
        private val clickCallback: ((Category) -> Unit?)
) : DataBoundListAdapter<Category, ContentSelectCategoryBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.id == newItem.id && oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.name == newItem.name
            }
        }
) {

    override fun createBinding(parent: ViewGroup): ContentSelectCategoryBinding {
        val binding = DataBindingUtil.inflate<ContentSelectCategoryBinding>(
                LayoutInflater.from(parent.context),
                R.layout.content_select_category,
                parent,
                false
        )

        binding.contentView.setOnClickListener {
            binding.category?.let {
                clickCallback.invoke(it)
            }
        }

        return binding
    }

    override fun bind(binding: ContentSelectCategoryBinding, item: Category, position: Int) {
        binding.category = item
        binding.position = position

    }
}
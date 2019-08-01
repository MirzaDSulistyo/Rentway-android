package id.rent.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import id.rent.android.di.Injectable
import id.rent.android.utility.AppExecutors
import javax.inject.Inject
import id.rent.android.data.binding.FragmentDataBindingComponent
import id.rent.android.databinding.FragmentHomeBinding
import id.rent.android.model.Auth
import id.rent.android.model.Product
import id.rent.android.ui.adapter.ProductAdapter
import id.rent.android.utility.autoCleared
import id.rent.android.utility.getAuth
import id.rent.android.viewmodel.ProductViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber
import id.rent.android.R

class HomeFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ProductViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentHomeBinding>()

    private var adapter by autoCleared<ProductAdapter>()

    private var auth: Auth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = activity?.getAuth()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false, dataBindingComponent)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ProductViewModel::class.java)
        viewModel.setAuth(auth?.token)

        binding.lifecycleOwner = viewLifecycleOwner
        initRecyclerView()

        val rvAdapter = ProductAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors
        ) {  product -> details(product) }

        binding.rvProducts.adapter = rvAdapter
        adapter = rvAdapter
    }

    private fun initRecyclerView() {
        refresh_products.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        refresh_products.setOnRefreshListener {
            viewModel.refresh()
        }

        binding.products = viewModel.products
        viewModel.products.observe(viewLifecycleOwner, Observer { result ->
            Timber.d("status : %s", result.status)
            Timber.d("message : %s", result.message)
            Timber.d("message : ${result.data?.size}")
            adapter.submitList(result?.data)
            refresh_products.isRefreshing = false
        })
    }

    private fun details(product: Product) {
        Timber.d("product ${product.name}")
    }

}
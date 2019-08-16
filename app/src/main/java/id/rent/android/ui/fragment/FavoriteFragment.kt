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
import id.rent.android.databinding.FragmentFavoriteBinding
import id.rent.android.model.Auth
import id.rent.android.model.Favorite
import id.rent.android.model.Profile
import id.rent.android.ui.adapter.FavoriteAdapter
import id.rent.android.utility.autoCleared
import id.rent.android.utility.getAuth
import id.rent.android.utility.getProfile
import kotlinx.android.synthetic.main.fragment_favorite.*
import id.rent.android.viewmodel.FavoriteViewModel
import id.rent.android.R
import id.rent.android.data.vo.Status
import timber.log.Timber

class FavoriteFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private lateinit var viewModel: FavoriteViewModel

    var binding by autoCleared<FragmentFavoriteBinding>()

    var adapter by autoCleared<FavoriteAdapter>()

    private var auth: Auth? = null

    private var profile: Profile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = activity?.getAuth()

        profile = activity?.getProfile()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false, dataBindingComponent)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(FavoriteViewModel::class.java)
        viewModel.setAuth(auth?.token)

        binding.lifecycleOwner = viewLifecycleOwner
        initRecyclerView()

        val rvAdapter = FavoriteAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors
        ) { item -> details(item) }

        binding.rvProducts.adapter = rvAdapter
        adapter = rvAdapter

    }

    override fun onResume() {
        super.onResume()

        viewModel.refresh()
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

        refresh_products.visibility = View.GONE
//        shimmer.startShimmer()

        //binding.products = viewModel.data
        viewModel.data.observe(viewLifecycleOwner, Observer { result ->
            adapter.submitList(result?.data)
            refresh_products.isRefreshing = false
            refresh_products.visibility = View.VISIBLE
//            shimmer.visibility = View.GONE
//            shimmer.stopShimmer()
            if (result.status == Status.ERROR) {
                Timber.d("message : ${result.message}")
            }
        })
    }

    private fun details(data: Favorite) {
        //
    }

}
package id.rent.android.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import id.rent.android.di.Injectable
import id.rent.android.viewmodel.ProductViewModel
import javax.inject.Inject
import id.rent.android.data.binding.FragmentDataBindingComponent
import id.rent.android.databinding.FragmentProfileBinding
import id.rent.android.model.Auth
import id.rent.android.model.Profile
import id.rent.android.ui.activity.HomeActivity
import id.rent.android.ui.activity.LoginActivity
import id.rent.android.utility.*
import id.rent.android.R

class ProfileFragment: Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ProductViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private var binding by autoCleared<FragmentProfileBinding>()

    private var auth: Auth? = null
    private var profile: Profile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity = activity as HomeActivity

        auth = activity.getAuth()

        profile = activity.getProfile()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false, dataBindingComponent)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ProductViewModel::class.java)
        viewModel.setAuth(auth?.token)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.profile = profile

        binding.store.setOnClickListener {
            //startActivity(Intent(activity, StoreActivity::class.java))
        }

        binding.logout.setOnClickListener {
            activity?.setAuth(null)
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }
}
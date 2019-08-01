package id.rent.android.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.kaopiz.kprogresshud.KProgressHUD
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import id.rent.android.R
import id.rent.android.data.vo.Status
import id.rent.android.databinding.ActivityHomeBinding
import id.rent.android.model.Auth
import id.rent.android.utility.AppExecutors
import id.rent.android.utility.getAuth
import id.rent.android.utility.setHud
import id.rent.android.utility.setProfile
import id.rent.android.viewmodel.UserViewModel
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_home.*
import timber.log.Timber
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var binding: ActivityHomeBinding

    private lateinit var viewModel: UserViewModel

    private var hud: KProgressHUD? = null

    private var auth: Auth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        binding.lifecycleOwner = this

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(UserViewModel::class.java)

        auth = this.getAuth()

        hud = this.setHud()

        if (auth == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            viewModel.setAuth(auth?.token)

            tv_home_two.text = Gson().toJson(auth)

            viewModel.profile.observe(this, Observer {
                if (it.status == Status.SUCCESS) {
                    Timber.d("profile ${Gson().toJson(it.data)}")

                    tv_home.text = Gson().toJson(it.data)

                    this.setProfile(it.data)
                }
            })
        }
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}

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
import com.google.android.material.snackbar.Snackbar
import com.kaopiz.kprogresshud.KProgressHUD
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import id.rent.android.R
import id.rent.android.data.vo.Resource
import id.rent.android.data.vo.Status
import id.rent.android.databinding.ActivityLoginBinding
import id.rent.android.model.Auth
import id.rent.android.utility.*
import id.rent.android.viewmodel.UserViewModel
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var viewModel: UserViewModel

    private lateinit var binding: ActivityLoginBinding

    private var hud: KProgressHUD? = null

    private var auth: Auth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.lifecycleOwner = this

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(UserViewModel::class.java)

        hud = this.setHud()

        auth = this.getAuth()

        binding.login.setOnClickListener { onLogin() }
    }

    private fun onLogin() {
        Helper.hideSoftKeyboard(this@LoginActivity)

        if (InternetConnection.checkConnection(applicationContext)) {
            hud?.show()

            val email = email.text.toString()
            val pass = password.text.toString()

            viewModel.login(email, pass)
                .observe(this, Observer<Resource<Auth>> {
                    if (it.status == Status.SUCCESS) {
                        Timber.d("auth token : %s", it.data?.token)

                        this.setAuth(it.data)

                        hud?.dismiss()

                        startActivity(Intent(this, HomeActivity::class.java))
                    }

                    if (it.status == Status.ERROR) {
                        hud?.dismiss()
                        Timber.d("message : %s", it.message)
                    }
                })
        } else {
            val snackBar = Snackbar.make(login_layout, getString(R.string.connection_not_available), Snackbar.LENGTH_INDEFINITE)
            snackBar.show()
        }
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}

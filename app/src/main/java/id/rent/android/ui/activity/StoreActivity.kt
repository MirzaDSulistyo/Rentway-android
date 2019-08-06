package id.rent.android.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.kaopiz.kprogresshud.KProgressHUD
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import id.rent.android.R
import id.rent.android.databinding.ActivityStoreBinding
import id.rent.android.model.Auth
import id.rent.android.utility.AppExecutors
import id.rent.android.utility.getAuth
import id.rent.android.utility.setHud
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class StoreActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var binding: ActivityStoreBinding

    //private lateinit var viewModel: UserViewModel

    private var hud: KProgressHUD? = null

    private var auth: Auth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_store)

        binding.lifecycleOwner = this

        //viewModel = ViewModelProviders.of(this, viewModelFactory)
            //.get(UserViewModel::class.java)

        auth = this.getAuth()

        hud = this.setHud()

        // Handle Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.store)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.form, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_close -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}

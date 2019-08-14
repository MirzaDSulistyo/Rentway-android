package id.rent.android.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
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
import id.rent.android.databinding.ActivityProductBinding
import id.rent.android.model.Auth
import id.rent.android.model.Profile
import id.rent.android.ui.adapter.MasterProductAdapter
import id.rent.android.utility.AppExecutors
import id.rent.android.utility.getAuth
import id.rent.android.utility.getProfile
import id.rent.android.utility.setHud
import id.rent.android.viewmodel.ProductViewModel
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_product.*
import org.jetbrains.anko.startActivity
import timber.log.Timber
import javax.inject.Inject

class ProductActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ProductViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var binding: ActivityProductBinding

    private var hud: KProgressHUD? = null

    private var auth: Auth? = null

    private var profile: Profile? = null

    private lateinit var adapter: MasterProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)

        binding.lifecycleOwner = this

        auth = this.getAuth()

        profile = this.getProfile()

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ProductViewModel::class.java)
        viewModel.setAuth(auth?.token)

        viewModel.setStoreId(profile?.stores!![0].id)

        hud = this.setHud()

        // Handle Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.products)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_chevron_left)

        adapter = MasterProductAdapter(
            appExecutors = appExecutors
        ) {
            this.startActivity<FormProductActivity>("json" to Gson().toJson(it))
        }

        binding.rvProducts.adapter = adapter

        refresh_products.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        refresh_products.setOnRefreshListener {
            viewModel.refresh()
        }

        binding.products = viewModel.productsByStore

        getProductsData()

        binding.btnAddProduct.setOnClickListener {
            this.startActivity<FormProductActivity>()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.product, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.action_filter -> {
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        viewModel.refresh()
    }

    private fun getProductsData() {
        if (profile != null) {
            if (profile!!.stores != null && profile!!.stores!!.isNotEmpty()) {
                viewModel.productsByStore.observe(this, Observer {
                    if (it.status == Status.SUCCESS) {

                        Timber.d("Success get products by store ${it?.data?.size} ${Gson().toJson(it?.data)}")

                        adapter.submitList(it?.data)
                    } else {
                        Timber.d("message : ${it.message}")
                    }
                })
            }
        }
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}

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
import id.rent.android.data.vo.Resource
import id.rent.android.data.vo.Status
import id.rent.android.databinding.ActivityFormProductBinding
import id.rent.android.di.Injectable
import id.rent.android.model.Auth
import id.rent.android.model.Category
import id.rent.android.model.Product
import id.rent.android.model.Profile
import id.rent.android.ui.dialog.DeleteConfirmationDialog
import id.rent.android.utility.AppExecutors
import id.rent.android.utility.getAuth
import id.rent.android.utility.getProfile
import id.rent.android.utility.setHud
import id.rent.android.viewmodel.ProductViewModel
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_form_product.*
import okhttp3.MultipartBody
import timber.log.Timber
import javax.inject.Inject

class FormProductActivity : AppCompatActivity(), HasSupportFragmentInjector, Injectable, DeleteConfirmationDialog.NoticeDialogListener {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ProductViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var binding: ActivityFormProductBinding

    private var auth: Auth? = null

    private var profile: Profile? = null

    private var hud: KProgressHUD? = null

    private var product: Product? = null

    private var categories = ArrayList<Category>()

    private var category: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_form_product)

        if (intent.getStringExtra("json") != null) {
            product = Gson().fromJson(intent.getStringExtra("json"), Product::class.java)

            binding.product = product

            binding.categoryData = product?.category
        }

        auth = getAuth()

        hud = setHud()

        profile = getProfile()

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ProductViewModel::class.java)
        viewModel.setAuth(auth?.token)
//        viewModel.setStoreId(auth?.stores!![0].id)

        viewModel.master.observe(this, Observer {
            if (it.status == Status.SUCCESS) {
                Timber.d("data master ${Gson().toJson(it.data?.rentway)}")
                it.data?.categories?.let { res -> categories.addAll(res) }
            }
        })

        binding.lifecycleOwner = this

        binding.close.setOnClickListener { finish() }

        save.setOnClickListener {
            if (product == null)
                onSaveData()
            else
                onUpdateData()
        }

        delete.setOnClickListener { onDelete() }

        binding.containerCategory.setOnClickListener {
            val intent = Intent(this, SelectCategoryActivity::class.java)
            intent.putExtra("data", Gson().toJson(categories))
            startActivityForResult(intent, CATEGORY_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == CATEGORY_REQUEST_CODE) {
                category = data?.getParcelableExtra("data")

                binding.categoryData = category
            }
        }
    }

    private fun onSaveData() {
        hud?.show()

        val name = name.text.toString()
        val sku = sku.text.toString()
        val brand = brand.text.toString()
        val description = descriptions.text.toString()
        val price = price.text.toString()
        val available = available.text.toString()
        val commission = commission.text.toString()
        val storeId = profile?.stores!![0].id

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        builder.addFormDataPart("name", name)
        builder.addFormDataPart("description", description)
        builder.addFormDataPart("sku", sku)
        builder.addFormDataPart("brand", brand)
        builder.addFormDataPart("price", price)
        builder.addFormDataPart("store_id", storeId!!)
        builder.addFormDataPart("commission", commission)
        builder.addFormDataPart("available", available)
        builder.addFormDataPart("rent_way_id", "5d39a9cb8139182067c66fe7")
        if (category != null)
            builder.addFormDataPart("category_id", category?.id!!)

        val body = builder.build()

        Timber.d("form token ${auth!!.token}")

        viewModel.saveProduct(auth?.token!!, body)
            .observe(this, Observer<Resource<Product>> { response ->
                if (response.status == Status.SUCCESS) {
                    hud!!.dismiss()
                    finish()
                }

                if (response.status == Status.ERROR) {
                    hud!!.dismiss()
                    Timber.d("message : %s", response.message)
                }

            })
    }

    private fun onUpdateData() {
        hud?.show()

        val name = name.text.toString()
        val sku = sku.text.toString()
        val brand = brand.text.toString()
        val description = descriptions.text.toString()
        val price = price.text.toString()
        val storeId = profile?.stores!![0].id

        val fieldMap = HashMap<String, String>()

        fieldMap["name"] = name
        fieldMap["description"] = description
        fieldMap["sku"] = sku
        fieldMap["brand"] = brand
        fieldMap["price"] = price
        fieldMap["store"] = storeId.toString()

        viewModel.updateProduct(auth!!.token!!, product?.id!!, fieldMap)
            .observe(this, Observer<Resource<Product>> { response ->
                if (response.status == Status.SUCCESS) {
                    Timber.d("message : %s", Gson().toJson(response.data))
                    hud?.dismiss()
                    finish()
                }

                if (response.status == Status.ERROR) {
                    hud?.dismiss()
                    Timber.d("message : %s", response.message)
                }

            })
    }

    override fun onDeleteItem() {
        hud?.show()

        viewModel.deleteProduct(auth!!.token!!, product!!.id)
            .observe(this, Observer<Resource<Product>> { response ->
                if (response.status == Status.SUCCESS) {
                    hud!!.dismiss()
                    finish()
                }

                if (response.status == Status.ERROR) {
                    Timber.d("message : %s", response.message)
                    hud!!.dismiss()
                }

            })
    }

    private fun onDelete() {
        val fragment = DeleteConfirmationDialog.newInstance(product?.name!!)
        fragment.show(supportFragmentManager, "delete")
    }

    private companion object {
        private const val CATEGORY_REQUEST_CODE = 1001
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

}
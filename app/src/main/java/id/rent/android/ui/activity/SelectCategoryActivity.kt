package id.rent.android.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import id.rent.android.R
import id.rent.android.data.database.converter.CategoryListConverter
import id.rent.android.databinding.ActivitySelectCategoryBinding
import id.rent.android.di.Injectable
import id.rent.android.model.Category
import id.rent.android.ui.adapter.CategoryAdapter
import id.rent.android.utility.AppExecutors
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_select_category.*
import javax.inject.Inject

class SelectCategoryActivity : AppCompatActivity(), HasSupportFragmentInjector, Injectable {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var binding: ActivitySelectCategoryBinding

    private var categories = ArrayList<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_category)

        // Handle Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.select_a_category)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_chevron_left)

        val json = intent.getStringExtra("data")
        val list = CategoryListConverter.stringToCategoryList(json)
        categories = list!!.toCollection(ArrayList())

        val adapter = CategoryAdapter(
                appExecutors = appExecutors
        ) {
            setResultData(it)
        }

        rv_categories.adapter = adapter

        adapter.submitList(categories)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setResultData(category: Category) {
        val intent = Intent(this, FormProductActivity::class.java)
        intent.putExtra("data", category)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}

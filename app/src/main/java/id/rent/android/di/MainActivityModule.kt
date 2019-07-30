package id.rent.android.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import id.rent.android.ui.activity.*

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): HomeActivity

}
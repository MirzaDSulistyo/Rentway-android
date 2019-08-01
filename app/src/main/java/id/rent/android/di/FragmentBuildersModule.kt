package id.rent.android.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import id.rent.android.ui.fragment.HomeFragment

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment
}
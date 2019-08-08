package id.rent.android.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import id.rent.android.ui.fragment.HomeFragment
import id.rent.android.ui.fragment.MoreMenuFragment

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): MoreMenuFragment
}
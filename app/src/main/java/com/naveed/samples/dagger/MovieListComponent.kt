package com.naveed.samples.dagger

import androidx.annotation.Nullable
import com.naveed.samples.view.MoviesListActivity
import dagger.Component


@Component
interface MovieListComponent{

    fun inject(activity:MoviesListActivity)

}

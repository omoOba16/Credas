package com.example.credas.di

import com.example.credas.data.repositories.MenuRepositoryImp
import com.example.credas.domain.repositories.MenuRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMoviesRepository(
        moviesRepositoryImp: MenuRepositoryImp
    ): MenuRepository
}
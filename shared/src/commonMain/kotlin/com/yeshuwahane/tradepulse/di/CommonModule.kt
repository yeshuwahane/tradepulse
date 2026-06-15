package com.yeshuwahane.tradepulse.di

import com.russhwolf.settings.Settings
import com.yeshuwahane.tradepulse.data.repository.ProductRepositoryImpl
import com.yeshuwahane.tradepulse.data.repository.UserRepositoryImpl
import com.yeshuwahane.tradepulse.domain.repository.ProductRepository
import com.yeshuwahane.tradepulse.domain.repository.UserRepository
import com.yeshuwahane.tradepulse.domain.usecase.AddProductUseCase
import com.yeshuwahane.tradepulse.domain.usecase.ApproveProductUseCase
import com.yeshuwahane.tradepulse.domain.usecase.GetProductByIdUseCase
import com.yeshuwahane.tradepulse.domain.usecase.GetProductsUseCase
import com.yeshuwahane.tradepulse.domain.usecase.GetUsersUseCase
import com.yeshuwahane.tradepulse.domain.usecase.LoginUseCase
import com.yeshuwahane.tradepulse.domain.usecase.PlaceBidUseCase
import com.yeshuwahane.tradepulse.domain.usecase.RejectProductUseCase
import com.yeshuwahane.tradepulse.presentation.admin.AdminViewModel
import com.yeshuwahane.tradepulse.presentation.detail.ProductDetailViewModel
import com.yeshuwahane.tradepulse.presentation.login.LoginViewModel
import com.yeshuwahane.tradepulse.presentation.marketplace.MarketplaceViewModel
import com.yeshuwahane.tradepulse.presentation.supplier.SupplierViewModel
import com.yeshuwahane.tradepulse.data.db.DatabaseDriverFactory
import com.yeshuwahane.tradepulse.data.db.TradePulseDb
import com.yeshuwahane.tradepulse.data.db.ProductDao
import com.yeshuwahane.tradepulse.data.db.ProductDaoImpl
import com.yeshuwahane.tradepulse.data.db.RoomDatabase
import org.koin.dsl.module

import com.yeshuwahane.tradepulse.domain.usecase.RegisterUseCase
import com.yeshuwahane.tradepulse.domain.usecase.UploadProductImageUseCase
import com.yeshuwahane.tradepulse.domain.usecase.GetSettingsUserUseCase
import com.yeshuwahane.tradepulse.domain.usecase.LogoutUseCase
import com.yeshuwahane.tradepulse.domain.usecase.DeleteUserUseCase
import com.yeshuwahane.tradepulse.domain.usecase.UpdateUserUseCase
import com.yeshuwahane.tradepulse.domain.usecase.UpdateProductUseCase

val commonModule = module {
    // Settings & Database
    single { Settings() }
    single { DatabaseDriverFactory() }
    single { TradePulseDb(get<DatabaseDriverFactory>().createDriver()) }
    single { RoomDatabase(get()) }
    single<ProductDao> { ProductDaoImpl(get()) }

    // Repositories
    single<ProductRepository> { ProductRepositoryImpl(get(), get<ProductDao>(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    // Use Cases
    single { GetProductsUseCase(get()) }
    single { GetUsersUseCase(get()) }
    single { ApproveProductUseCase(get()) }
    single { RejectProductUseCase(get()) }
    single { LoginUseCase(get()) }
    single { GetProductByIdUseCase(get()) }
    single { PlaceBidUseCase(get()) }
    single { AddProductUseCase(get()) }
    single { RegisterUseCase(get()) }
    single { UploadProductImageUseCase(get()) }
    single { GetSettingsUserUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { DeleteUserUseCase(get()) }
    single { UpdateUserUseCase(get()) }
    single { UpdateProductUseCase(get()) }

    // ViewModels
    factory { LoginViewModel(get(), get()) }
    factory { MarketplaceViewModel(get()) }
    factory { ProductDetailViewModel(get(), get(), get()) }
    factory { SupplierViewModel(get(), get(), get(), get(), get(), get()) }
    factory { AdminViewModel(get(), get(), get(), get(), get(), get(), get()) }
}
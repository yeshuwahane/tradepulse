package com.yeshuwahane.tradepulse.di

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

// Data layer Repositories
import com.yeshuwahane.tradepulse.data.repository.ProductRepositoryImpl
import com.yeshuwahane.tradepulse.data.repository.UserRepositoryImpl
import com.yeshuwahane.tradepulse.domain.repository.ProductRepository
import com.yeshuwahane.tradepulse.domain.repository.UserRepository

// Domain layer Use Cases
import com.yeshuwahane.tradepulse.domain.usecase.AddProductUseCase
import com.yeshuwahane.tradepulse.domain.usecase.ApproveProductUseCase
import com.yeshuwahane.tradepulse.domain.usecase.GetProductByIdUseCase
import com.yeshuwahane.tradepulse.domain.usecase.GetProductsUseCase
import com.yeshuwahane.tradepulse.domain.usecase.GetUsersUseCase
import com.yeshuwahane.tradepulse.domain.usecase.LoginUseCase
import com.yeshuwahane.tradepulse.domain.usecase.PlaceBidUseCase
import com.yeshuwahane.tradepulse.domain.usecase.RejectProductUseCase

// Presentation layer ViewModels
import com.yeshuwahane.tradepulse.presentation.login.LoginViewModel
import com.yeshuwahane.tradepulse.presentation.marketplace.MarketplaceViewModel
import com.yeshuwahane.tradepulse.presentation.detail.ProductDetailViewModel
import com.yeshuwahane.tradepulse.presentation.supplier.SupplierViewModel
import com.yeshuwahane.tradepulse.presentation.admin.AdminViewModel


fun appModule() = listOf(networkModule, commonModule)

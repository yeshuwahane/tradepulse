package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.model.Product
import com.yeshuwahane.tradepulse.domain.repository.ProductRepository

class GetProductsUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(forceRefresh: Boolean = false): DataResource<List<Product>> {
        return productRepository.getProducts(forceRefresh)
    }

    suspend fun getCached(): List<Product> {
        return productRepository.getCachedProducts()
    }
}

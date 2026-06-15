package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.model.Product
import com.yeshuwahane.tradepulse.domain.repository.ProductRepository

class GetProductByIdUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(id: String): DataResource<Product> {
        return productRepository.getProductById(id)
    }

    suspend fun getCached(id: String): Product? {
        return productRepository.getCachedProductById(id)
    }
}

package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.model.Product
import com.yeshuwahane.tradepulse.domain.repository.ProductRepository

class AddProductUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(
        title: String,
        description: String,
        price: Double,
        category: String,
        supplierId: String,
        isAuction: Boolean,
        durationHours: Int
    ): DataResource<Product> {
        return productRepository.addProduct(title, description, price, category, supplierId, isAuction, durationHours)
    }
}

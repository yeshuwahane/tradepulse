package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.repository.ProductRepository

class PlaceBidUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(productId: String, amount: Double, bidderName: String): DataResource<String> {
        return productRepository.placeBid(productId, amount, bidderName)
    }
}

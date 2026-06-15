package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.repository.ProductRepository

class ApproveProductUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(productId: String): DataResource<String> {
        return productRepository.approveProduct(productId)
    }
}

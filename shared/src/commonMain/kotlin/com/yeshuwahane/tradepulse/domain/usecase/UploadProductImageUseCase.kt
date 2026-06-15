package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.repository.ProductRepository

class UploadProductImageUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(imageBytes: ByteArray): DataResource<String> {
        return productRepository.uploadProductImage(imageBytes)
    }
}

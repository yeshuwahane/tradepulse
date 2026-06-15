package com.yeshuwahane.tradepulse.presentation.marketplace

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.model.Product

data class MarketplaceUiState(
    val productsResource: DataResource<List<Product>> = DataResource.initial()
)

sealed interface MarketplaceIntent {
    object LoadProducts : MarketplaceIntent
}

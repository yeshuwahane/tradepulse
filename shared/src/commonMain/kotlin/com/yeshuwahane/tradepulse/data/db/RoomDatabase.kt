package com.yeshuwahane.tradepulse.data.db

import com.yeshuwahane.tradepulse.data.repository.ProductDto

interface ProductDao {
    suspend fun clear()
    suspend fun insertAll(products: List<ProductDto>)
    suspend fun getAll(): List<ProductDto>
}

class ProductDaoImpl(private val database: TradePulseDb) : ProductDao {
    private val productQueries = database.tradePulseDbQueries

    override suspend fun clear() {
        productQueries.clearProducts()
    }

    override suspend fun insertAll(products: List<ProductDto>) {
        products.forEach { p ->
            productQueries.insertProduct(
                p.id, p.title, p.description, p.price, p.imageUrl, p.supplierId,
                if (p.isApproved) 1L else 0L, p.currentHighestBid, p.highestBidderName, p.auctionEndTimeMillis
            )
        }
    }

    override suspend fun getAll(): List<ProductDto> {
        return productQueries.getAllProducts().executeAsList().map {
            ProductDto(
                id = it.id,
                title = it.title,
                description = it.description,
                price = it.price,
                imageUrl = it.imageUrl,
                supplierId = it.supplierId,
                isApproved = it.isApproved != 0L,
                currentHighestBid = it.currentHighestBid,
                highestBidderName = it.highestBidderName,
                auctionEndTimeMillis = it.auctionEndTimeMillis
            )
        }
    }
}

class RoomDatabase(private val database: TradePulseDb) {
    val productDao: ProductDao = ProductDaoImpl(database)
}

  

package com.coinbase.resources.prices;

import com.coinbase.ApiConstants;
import com.coinbase.CoinbaseResponse;
import com.coinbase.network.ApiCall;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * API interface for getting prices of supported crypto currencies.
 *
 * @see PricesResource
 */
public interface PricesApi {

    @GET(ApiConstants.PRICES + "/{base_currency}-" + "{fiat_currency}/" + ApiConstants.BUY)
    ApiCall<CoinbaseResponse<Price>> getBuyPrice(@Path("base_currency") String baseCurrency,
                                                 @Path("fiat_currency") String fiatCurrency);

    @GET(ApiConstants.PRICES + "/{base_currency}-" + "{fiat_currency}/" + ApiConstants.SELL)
    ApiCall<CoinbaseResponse<Price>> getSellPrice(@Path("base_currency") String baseCurrency,
                                                  @Path("fiat_currency") String fiatCurrency);

    @GET(ApiConstants.PRICES + "/{base_currency}-" + "{fiat_currency}/" + ApiConstants.SPOT)
    ApiCall<CoinbaseResponse<Price>> getSpotPrice(@Path("base_currency") String baseCurrency,
                                                         @Path("fiat_currency") String fiatCurrency,
                                                         @Query("date") String date);

    @GET(ApiConstants.PRICES + "/{fiat_currency}/" + ApiConstants.SPOT)
    ApiCall<CoinbaseResponse<List<Price>>> getSpotPrices(@Path("fiat_currency") String fiatCurrency,
                                                         @Query("date") String date);
}

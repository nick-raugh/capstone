  

package com.coinbase.resources.prices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coinbase.CoinbaseResponse;
import com.coinbase.network.ApiCall;
import com.coinbase.resources.buys.BuysResource;
import com.coinbase.resources.sells.SellsResource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;


public class PricesResource {

    private final PricesApi pricesApi;
    private final PricesApiRx pricesApiRx;

    private final SimpleDateFormat dayDateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public PricesResource(PricesApi pricesApi, PricesApiRx pricesApiRx) {
        this.pricesApi = pricesApi;
        this.pricesApiRx = pricesApiRx;
    }

    // region Call methods.

    public ApiCall<CoinbaseResponse<Price>> getBuyPrice(@NonNull String baseCurrency,
                                                        @NonNull String fiatCurrency) {
        return pricesApi.getBuyPrice(baseCurrency, fiatCurrency);
    }

    public ApiCall<CoinbaseResponse<Price>> getSellPrice(@NonNull String baseCurrency,
                                                         @NonNull String fiatCurrency) {
        return pricesApi.getSellPrice(baseCurrency, fiatCurrency);
    }

    public ApiCall<CoinbaseResponse<Price>> getSpotPrice(@NonNull String baseCurrency,
                                                                @NonNull String fiatCurrency,
                                                                @Nullable Date date) {
        final String dateParam = date != null ? dayDateFormatter.format(date) : null;
        //System.out.println("&&&&&&&&"+pricesApi.getSpotPrice(baseCurrency, fiatCurrency, dateParam));
        return pricesApi.getSpotPrice(baseCurrency, fiatCurrency, dateParam);

    }

    public ApiCall<CoinbaseResponse<Price>> getSpotPrice(@NonNull String baseCurrency,
                                                         @NonNull String fiatCurrency) {
        return pricesApi.getSpotPrice(baseCurrency, fiatCurrency, null);
    }

    public ApiCall<CoinbaseResponse<List<Price>>> getSpotPrices(@NonNull String fiatCurrency,
                                                                @Nullable Date date) {
        final String dateParam = date != null ? dayDateFormatter.format(date) : null;
        return pricesApi.getSpotPrices(fiatCurrency, dateParam);
    }

    public ApiCall<CoinbaseResponse<List<Price>>> getSpotPrices(@NonNull String fiatCurrency) {
        return pricesApi.getSpotPrices(fiatCurrency, null);
    }

    // endregion

    //region Rx methods

    public Single<CoinbaseResponse<Price>> getBuyPriceRx(@NonNull String baseCurrency,
                                                         @NonNull String fiatCurrency) {
        return pricesApiRx.getBuyPrice(baseCurrency, fiatCurrency);
    }

    public Single<CoinbaseResponse<Price>> getSellPriceRx(@NonNull String baseCurrency,
                                                          @NonNull String fiatCurrency) {
        return pricesApiRx.getSellPrice(baseCurrency, fiatCurrency);
    }

    public Single<CoinbaseResponse<Price>> getSpotPriceRx(@NonNull String baseCurrency,
                                                          @NonNull String fiatCurrency,
                                                          @Nullable Date date) {
        final String dateParam = date != null ? dayDateFormatter.format(date) : null;
        return pricesApiRx.getSpotPrice(baseCurrency, fiatCurrency, dateParam);
    }

    public Single<CoinbaseResponse<Price>> getSpotPriceRx(@NonNull String baseCurrency,
                                                          @NonNull String fiatCurrency) {
        return pricesApiRx.getSpotPrice(baseCurrency, fiatCurrency, null);
    }

    public Single<CoinbaseResponse<List<Price>>> getSpotPricesRx(@NonNull String fiatCurrency,
                                                                 @Nullable Date date) {
        final String dateParam = date != null ? dayDateFormatter.format(date) : null;
        return pricesApiRx.getSpotPrices(fiatCurrency, dateParam);
    }

    public Single<CoinbaseResponse<List<Price>>> getSpotPricesRx(@NonNull String fiatCurrency) {
        return pricesApiRx.getSpotPrices(fiatCurrency, null);
    }

    //endregion
}

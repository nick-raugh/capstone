  

package com.coinbase.resources.sells;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coinbase.ApiConstants;
import com.coinbase.CoinbaseResponse;
import com.coinbase.PagedResponse;
import com.coinbase.PaginationParams;
import com.coinbase.network.ApiCall;
import com.coinbase.resources.trades.Trade.ExpandField;
import com.coinbase.resources.trades.TradesApi;
import com.coinbase.resources.trades.TradesApiRx;
import com.coinbase.resources.trades.TradesResource;
import com.coinbase.resources.trades.TransferOrderBody;

import io.reactivex.Single;

public class SellsResource extends TradesResource<Sell> {

    public SellsResource(TradesApi tradesApi, TradesApiRx tradesApiRx) {
        super(tradesApi, tradesApiRx, ApiConstants.SELLS);
    }

    //region ApiCall methods

    public ApiCall<PagedResponse<Sell>> listSells(@NonNull String accountId,
                                                  @NonNull PaginationParams paginationParams,
                                                  @Nullable ExpandField... expandFields) {
        return listTrades(accountId, paginationParams, expandFields);
    }

    public ApiCall<PagedResponse<Sell>> listSells(@NonNull String accountId,
                                                  @Nullable ExpandField... expandFields) {
        return listTrades(accountId, expandFields);
    }

    public ApiCall<CoinbaseResponse<Sell>> showSell(@NonNull String accountId,
                                                    @NonNull String sellId,
                                                    @Nullable ExpandField... expandFields) {
        return showTrade(accountId, sellId, expandFields);
    }

    public ApiCall<CoinbaseResponse<Sell>> placeSellOrder(@NonNull String accountId,
                                                          @NonNull TransferOrderBody body,
                                                          @Nullable ExpandField... expandFields) {
        return placeTradeOrder(accountId, body, expandFields);
    }


    public ApiCall<CoinbaseResponse<Sell>> commitSellOrder(@NonNull String accountId,
                                                           @NonNull String sellId,
                                                           @Nullable ExpandField... expandFields) {
        return commitTradeOrder(accountId, sellId, expandFields);
    }

    //endregion

    //region Rx methods

    public Single<PagedResponse<Sell>> listSellsRx(@NonNull String accountId,
                                                   @NonNull PaginationParams paginationParams,
                                                   @Nullable ExpandField... expandFields) {
        return listTradesRx(accountId, paginationParams, expandFields);
    }

    public Single<PagedResponse<Sell>> listSellsRx(@NonNull String accountId,
                                                   @Nullable ExpandField... expandFields) {
        return listTradesRx(accountId, expandFields);
    }

    public Single<CoinbaseResponse<Sell>> showSellRx(@NonNull String accountId,
                                                     @NonNull String sellId,
                                                     @Nullable ExpandField... expandFields) {
        return showTradeRx(accountId, sellId, expandFields);
    }

    public Single<CoinbaseResponse<Sell>> placeSellOrderRx(@NonNull String accountId,
                                                           @NonNull TransferOrderBody body,
                                                           @Nullable ExpandField... expandFields) {
        return placeTradeOrderRx(accountId, body, expandFields);
    }

    public Single<CoinbaseResponse<Sell>> commitSellOrderRx(@NonNull String accountId,
                                                            @NonNull String sellId,
                                                            @Nullable ExpandField... expandFields) {
        return commitTradeOrderRx(accountId, sellId, expandFields);
    }

    //endregion

}

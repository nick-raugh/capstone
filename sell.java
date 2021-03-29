

package com.coinbase.resources.sells;

import com.coinbase.resources.trades.Trade;
import com.coinbase.resources.transactions.MoneyHash;

public class Sell extends Trade {

    private MoneyHash total;
    private Boolean instant;

    /**
     * Fiat amount with fees.
     *
     * @return fiat amount with fees
     */
    public MoneyHash getTotal() {
        return total;
    }

    /**
     * Was this sell executed instantly?
     *
     * @return {@code true} for an instant sell.
     */
    public Boolean getInstant() {
        return instant;
    }
}

  

package com.coinbase.resources.prices;

/**
 * Represents a price of a specified crypto currency (base) in a specified fiat currency.
 */
public class Price {


    private String amount;
    private String base;
    private String currency;

    /**
     * Code of the base crypto currency for which this price is for.
     *
     * @return code of crypto currency for which this price is for.
     */
    public String getBase() {
        return base;
    }

    /**
     * The amount in {@link #getCurrency() fiat currrency}.
     *
     * @return the amount in fiat currency.
     * @see #getCurrency()
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Fiat currency in which amount is in.
     *
     * @return fiat currency.
     */
    public String getCurrency() {
        return currency;
    }

}

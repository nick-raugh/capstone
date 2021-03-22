  

package com.coinbase.resources.accounts;

import com.coinbase.CoinbaseResponse;
import com.coinbase.network.ApiCall;
import com.coinbase.resources.base.DynamicResource;
import com.coinbase.resources.prices.Price;
import com.coinbase.resources.prices.PricesApi;
import com.coinbase.resources.prices.PricesResource;
import com.coinbase.resources.transactions.MoneyHash;


import java.util.Date;

public class Account extends DynamicResource {


    /**
     * Current Date
     */
    public static final Date dateD = new Date();
    public static final String date = dateD.toString();
    //region Type

    /**
     * Wallet account. Only this account type can be set as primary.
     */
    public static final String TYPE_WALLET = "wallet";

    /**
     * Vault account.
     */
    public static final String TYPE_VAULT = "vault";

    /**
     * Fiat account.
     */
    public static final String TYPE_FIAT = "fiat";

    public static final String TYPE_MULTISIG_VAULT = "multisig_vault";
    public static final String TYPE_MULTISIG = "multisig";

    //endregion

    private String name;
    private Boolean primary;
    private String type;
    private Currency currency;
    private MoneyHash balance;
    private Date createdAt;
    private Date updatedAt;
    private Double convertedBalance;
    private String fiatValue;

    /**
     * User or system defined name.
     *
     * @return name of this account.
     */
    public String getName() {
        return name;
    }

    /**
     * Whether this account is primary.
     *
     * @return {@code true} if this account is <b>primary</b> and {@code false} otherwise.
     */
    public Boolean getPrimary() {
        return primary;
    }

    public String getType() {
        return type;
    }

    /**
     * Account currency.
     *
     * @return account currency.
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Balance of this account in it's currency.
     *
     * @return balance of this account
     *
     */
    public MoneyHash getBalance() {
        return balance;
    }

    /**
     * When account was created.
     *
     * @return timestamp when account was created.
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * When account was updated.
     *
     * @return timestamp when account was updated.
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Converted balance of an account for sorting purposes.
     *
     * @return converted balance of account
     */
    public double getConvertedBalance() {
        String temp=balance.getAmount();
        float convertedBalance = Float.parseFloat(temp);
        return convertedBalance;
    }

    public ApiCall<CoinbaseResponse<Price>> getFiatValue(){

        ApiCall<CoinbaseResponse<Price>> temp = PricesResource.getSpotPrice(currency.getCode(),"USD",date);

        return temp;
    }

}

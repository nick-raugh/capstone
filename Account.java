  

package com.coinbase.resources.accounts;

import com.coinbase.resources.base.DynamicResource;
import com.coinbase.resources.transactions.MoneyHash;

import java.util.Date;

/**
 * Account resource represents all of a user’s accounts, including bitcoin,
 * bitcoin cash, litecoin and ethereum wallets, fiat currency accounts, and vaults.
 * This is represented in the type field.
 * It’s important to note that new types can be added over time
 * so you want to make sure this won’t break your implementation.
 * <p>
 * User can only have one primary account and its type can only be {@code wallet}.
 */
public class Account extends DynamicResource {

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
    private Float convertedBalance;

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

    /**
     * Type of this account.
     * <p>
     * It can be <pre>wallet, vault, fiat, multisig or multisig_vault.</pre>
     * <p>
     * <b>NOTE:</b><br/>
     * Use constants to determine the type of this account.
     *
     * @return type of this account.
     * @see #TYPE_FIAT
     * @see #TYPE_VAULT
     * @see #TYPE_WALLET
     * @see #TYPE_MULTISIG
     * @see #TYPE_MULTISIG_VAULT
     */
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
     * @see MoneyHash
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
        System.out.println("****"+convertedBalance+"******\n");
        return convertedBalance;
    }

}

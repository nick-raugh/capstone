  

package com.coinbase.resources.transactions;

/**
 * Information about bitcoin, bitcoin cash, litecoin or ethereum network including network
 * transaction hash if transaction was on-blockchain. Only available for certain types of transactions.
 */
public class Network {

    //region Status

    public static final String STATUS_OFF_BLOCKCHAIN = "off_blockchain";
    public static final String STATUS_CONFIRMED = "confirmed";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_UNCONFIRMED = "unconfirmed";

    //endregion

    private String status;
    private Integer confirmations;
    private MoneyHash transactionFee;
    private MoneyHash transactionAmount;
    private String hash;

    public String getStatus() {
        return status;
    }

    public Integer getConfirmations() {
        return confirmations;
    }

    public MoneyHash getTransactionFee() {
        return transactionFee;
    }

    public MoneyHash getTransactionAmount() {
        return transactionAmount;
    }

    public String getHash() {
        return hash;
    }

}

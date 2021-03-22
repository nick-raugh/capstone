

package com.coinbase.resources.transactions;

import com.coinbase.resources.addresses.Address;
import com.coinbase.resources.base.BaseResource;
import com.coinbase.resources.base.DynamicResource;
import com.coinbase.resources.buys.Buy;
import com.coinbase.resources.sells.Sell;

import java.util.Date;

public class Transaction extends BaseResource {

    //region Status

    /**
     * Pending transactions (e.g. a send or a buy)
     */
    public static final String STATUS_PENDING = "pending";

    /**
     * Completed transactions (e.g. a send or a buy)
     */
    public static final String STATUS_COMPLETED = "completed";

    /**
     * Failed transactions (e.g. failed buy)
     */
    public static final String STATUS_FAILED = "failed";

    /**
     * Conditional transaction expired due to external factors
     */
    public static final String STATUS_EXPIRED = "expired";

    /**
     * Transaction was canceled
     */
    public static final String STATUS_CANCELED = "canceled";

    /**
     * Vault withdrawal is waiting for approval
     */
    public static final String STATUS_WAITING_FOR_SIGNATURE = "waiting_for_signature";

    /**
     * Vault withdrawal is waiting to be cleared
     */
    public static final String STATUS_WAITING_FOR_CLEARING = "waiting_for_clearing";

    //endregion

    //region Type

    /**
     * Sent bitcoin/bitcoin cash/litecoin/ethereum to a bitcoin/bitcoin cash/litecoin/ethereum address or email (documentation)
     */
    public static final String TYPE_SEND = "send";

    /**
     * Requested bitcoin/bitcoin cash/litecoin/ethereum from a user or email (documentation)
     */
    public static final String TYPE_REQUEST = "request";

    /**
     * Transfered funds between two of a user’s accounts (documentation)
     */
    public static final String TYPE_TRANSFER = "transfer";

    /**
     * Bought bitcoin, bitcoin cash, litecoin or ethereum (documentation)
     */
    public static final String TYPE_BUY = "buy";

    /**
     * Sold bitcoin, bitcoin cash, litecoin or ethereum (documentation)
     */
    public static final String TYPE_SELL = "sell";

    /**
     * Deposited funds into a fiat account from a financial institution (documentation)
     */
    public static final String TYPE_FIAT_DEPOSIT = "fiat_deposit";

    /**
     * Withdrew funds from a fiat account (documentation)
     */
    public static final String TYPE_FIAT_WITHDRAWAL = "fiat_withdrawal";

    /**
     * Deposited money into GDAX
     */
    public static final String TYPE_EXCHANGE_DEPOSIT = "exchange_deposit";

    /**
     * Withdrew money from GDAX
     */
    public static final String TYPE_EXCHANGE_WITHDRAWAL = "exchange_withdrawal";

    /**
     * Withdrew funds from a vault account
     */
    public static final String TYPE_VAULT_WITHDRAWAL = "vault_withdrawal";

    //endregion

    private String type;
    private String status;
    private MoneyHash amount;
    private MoneyHash nativeAmount;
    private String description;
    private Date createdAt;
    private Date updatedAt;
    private Network network;
    private String idem;
    private Boolean instantExchange;
    private DynamicResource to;
    private DynamicResource from;
    private Buy buy;
    private Sell sell;
    private Details details;
    private Address address;
    private Application application;

    //region Getters

    /**
     * Returns type of this transaction.
     *
     * @return type of this transaction.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns status of this transaction.
     *
     * @return status of this transaction.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Amount in bitcoin, bitcoin cash, litecoin or ethereum.
     *
     * @return amount in bitcoin, bitcoin cash, litecoin or ethereum.
     */
    public MoneyHash getAmount() {
        return amount;
    }

    /**
     * Amount in user’s native currency
     *
     * @return amount in user’s native currency.
     */
    public MoneyHash getNativeAmount() {
        return nativeAmount;
    }

    /**
     * User defined description.
     *
     * @return user defined description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Transaction creation timestamp.
     *
     * @return transaction creation timestamp
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    
    public Date getUpdatedAt() {
        return updatedAt;
    }

    
    public Network getNetwork() {
        return network;
    }

    public String getIdem() {
        return idem;
    }

    public Boolean getInstantExchange() {
        return instantExchange;
    }

 
    public DynamicResource getTo() {
        return to;
    }

 
    public DynamicResource getFrom() {
        return from;
    }

    public Buy getBuy() {
        return buy;
    }

    public Sell getSell() {
        return sell;
    }

    public Details getDetails() {
        return details;
    }

    public Application getApplication() {
        return application;
    }

    public Address getAddress() {
        return address;
    }

    //endregion

    //region Inner classes.

    public static class Details {

        private String title;
        private String subtitle;
        private String paymentMethodName;

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public String getPaymentMethodName() {
            return paymentMethodName;
        }
    }

    /**
     * Expand options for Transaction object.
     */
    public enum ExpandField implements com.coinbase.resources.ExpandField {

        /**
         * Show all fields of {@link Transaction#getFrom()} object.
         */
        FROM("from"),

        /**
         * Show all fields of {@link Transaction#getTo()} ()} object.
         */
        TO("to"),

        /**
         * Show all fields of {@link Transaction#getBuy()} object.
         */
        BUY(TYPE_BUY),

        /**
         * Show all fields of {@link Transaction#getSell()} object.
         */
        SELL(TYPE_SELL),
        /**
         * Show all fields of {@link Transaction#getApplication()} object.
         */
        APPLICATION("application"),

        /**
         * Expand all available fields.
         */
        ALL(com.coinbase.resources.ExpandField.ALL_FIELDS);

        private final String value;

        ExpandField(String value) {
            this.value = value;
        }

        @Override
        public String getCode() {
            return value;
        }
    }

    //endregion

}

  

package com.coinbase.network;

public interface Callback<T> {

    void onSuccess(T result);

    void onFailure(Throwable t);
}

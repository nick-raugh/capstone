

package com.coinbase.network;

import java.io.IOException;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Response;

public class ApiCall<T> {

    private Call<T> originalCall;
    private Executor callbackExecutor;

    ApiCall(Call<T> originalCall, Executor callbackExecutor) {
        this.originalCall = originalCall;
        this.callbackExecutor = callbackExecutor;
    }

    public T execute() throws IOException {
        return originalCall.execute().body();
    }

    public void enqueue(Callback<T> callback) {
        originalCall.enqueue(new retrofit2.Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                callbackExecutor.execute(() -> callback.onSuccess(response.body()));
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callbackExecutor.execute(() -> callback.onFailure(t));
            }
        });
    }

    public boolean isExecuted() {
        return originalCall.isExecuted();
    }

    public void cancel() {
        originalCall.cancel();
    }

    public boolean isCanceled() {
        return originalCall.isCanceled();
    }

}

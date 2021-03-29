

package com.coinbase.network;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;


public class ApiCallAdapterFactory extends CallAdapter.Factory {

    public static ApiCallAdapterFactory create() {
        return new ApiCallAdapterFactory();
    }

    @Override
    public CallAdapter<?, ApiCall<?>> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != ApiCall.class) {
            return null;
        }
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalStateException(ApiCall.class.getCanonicalName() + " must be parametrized with response type");
        }
        Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);
        return new CoinbaseCallAdapter(responseType, retrofit.callbackExecutor());
    }

    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private static class CoinbaseCallAdapter implements CallAdapter<Object, ApiCall<?>> {

        private final Type responseType;
        private final Executor callbackExacutor;

        CoinbaseCallAdapter(Type responseType, Executor excetor) {
            this.responseType = responseType;
            this.callbackExacutor = excetor;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public ApiCall<?> adapt(Call<Object> call) {
            return new ApiCall<>(call, callbackExacutor);
        }
    }

}

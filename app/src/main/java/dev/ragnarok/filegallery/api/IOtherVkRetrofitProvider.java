package dev.ragnarok.filegallery.api;

import io.reactivex.rxjava3.core.Single;


public interface IOtherVkRetrofitProvider {
    Single<RetrofitWrapper> provideLocalServerRetrofit();
}

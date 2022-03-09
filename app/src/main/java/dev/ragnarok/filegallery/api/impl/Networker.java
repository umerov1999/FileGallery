package dev.ragnarok.filegallery.api.impl;

import dev.ragnarok.filegallery.api.IOtherVkRetrofitProvider;
import dev.ragnarok.filegallery.api.OtherVkRetrofitProvider;
import dev.ragnarok.filegallery.api.interfaces.ILocalServerApi;
import dev.ragnarok.filegallery.api.interfaces.INetworker;
import dev.ragnarok.filegallery.api.services.ILocalServerService;
import dev.ragnarok.filegallery.settings.ISettings;

public class Networker implements INetworker {

    private final IOtherVkRetrofitProvider otherVkRetrofitProvider;

    public Networker(ISettings.IMainSettings settings) {
        otherVkRetrofitProvider = new OtherVkRetrofitProvider(settings);
    }

    @Override
    public ILocalServerApi localServerApi() {
        return new LocalServerApi(() -> otherVkRetrofitProvider.provideLocalServerRetrofit().map(wrapper -> wrapper.create(ILocalServerService.class)));
    }
}

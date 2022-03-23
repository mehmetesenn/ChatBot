package com.mehmetesen.chatbot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitAPI {
    @GET
    Call<MSGModal> getMessage(@Url String url);
}

package com.elluminati.charge.parser;


import com.elluminati.charge.models.responsemodels.ParticularStationsResponse;
import com.elluminati.charge.models.responsemodels.StationsResponse;
import com.elluminati.charge.utils.Const;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ApiInterface {


    @Headers("Content-Type:application/json;charset=UTF-8")
    @GET("station")
    Call<StationsResponse> getStations(@QueryMap Map<String, String> options);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @GET("station/{id}")
    Call<ParticularStationsResponse> getParticularStation(@Path(Const.Params.ID) String id);

    @GET("api/distancematrix/json?")
    Call<ResponseBody> getGoogleDistanceMatrix(@QueryMap Map<String, String> stringMap);

}
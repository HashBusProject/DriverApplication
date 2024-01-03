package com.hashimte.hashbusdriver.api;

import com.google.gson.Gson;
import com.hashimte.hashbusdriver.model.ChangePassword;
import com.hashimte.hashbusdriver.model.DataSchedule;
import com.hashimte.hashbusdriver.model.DriverData;
import com.hashimte.hashbusdriver.model.Point;
import com.hashimte.hashbusdriver.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public class ServicesImp implements Services {

    private final static String HTTPS = "http://localhost:8080";
    private final static String HTTPS1 = "https://global-memento-407716.uc.r.appspot.com/";

    private final static ServicesImp instance = new ServicesImp();

    private Retrofit retrofit = getRetrofit();

    private ServicesImp() {

    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(HTTPS1)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public static ServicesImp getInstance() {
        return instance;
    }

    @GET("/Driver/GetScheduleData")
    public Call<List<DataSchedule>> getDataSchedulesByBusId(@Query("busId") Integer busId) {
        return retrofit.create(Services.class).getDataSchedulesByBusId(busId);
    }

    @GET("/Driver/GetAllPointsForJourney")
    public Call<List<Point>> getAllPointsForJourney(@Query("journeyId") Integer journeyId) {
        return retrofit.create(Services.class).getAllPointsForJourney(journeyId);
    }

    @POST("/Driver/UpdateNextPointIndex")
    public Call<Boolean> updateNextPointIndexByScheduleId(
            @Query("scheduleId") Integer scheduleId,
            @Query("previousIndex") Integer previousIndex) {
        return retrofit.create(Services.class).updateNextPointIndexByScheduleId(scheduleId, previousIndex);
    }

    @POST("/Driver/UpdateLocation")
    public Call<Boolean> updateLocation(
            @Query("busId") Integer busId,
            @Query("latitude") Double latitude,
            @Query("longitude") Double longitude) {
        return retrofit.create(Services.class).updateLocation(busId, latitude, longitude);
    }

    @POST("/Driver/setScheduleAsFinished")
    public Call<Boolean> setScheduleAsFinished(@Query("scheduleId") Integer scheduleId) {
        return retrofit.create(Services.class).setScheduleAsFinished(scheduleId);
    }

    @POST("/Driver/Login")
    public Call<DriverData> login(@Body User user){
        return retrofit.create(Services.class).login(user);
    }

    @PUT("/Driver/ChangePassword")
    public Call<Boolean> changePassword(@Body ChangePassword changePassword) {
        return retrofit.create(Services.class).changePassword(changePassword);
    }

    @PUT("Driver/ChangeEmail")
    public Call<Boolean> changeEmail(@Body User user) {
        return retrofit.create(Services.class).changeEmail(user);
    }

}

package com.hashimte.hashbusdriver.api;

import com.hashimte.hashbusdriver.model.DataSchedule;
import com.hashimte.hashbusdriver.model.Point;
import com.hashimte.hashbusdriver.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface Services {
    @POST("/User/Login")
    Call<User> login(@Body User user);

    @GET("/Driver/GetScheduleData")
    Call<List<DataSchedule>> getDataSchedulesByBusId(@Query("busId") Integer busId);

    @POST("/Driver/setScheduleAsFinished")
    Call<Boolean> setScheduleAsFinished(@Query("scheduleId") Integer scheduleId);

    @GET("/Driver/GetAllPointsForJourney")
    Call<List<Point>> getAllPointsForJourney(@Query("journeyId") Integer journeyId);

    @POST("/Driver/UpdateNextPointIndex")
    Call<Boolean> updateNextPointIndexByScheduleId(@Query("scheduleId") Integer scheduleId, @Query("previousIndex") Integer previousIndex);
}

package com.example.android.api;

import com.example.android.json.AccountsArray;
import com.example.android.json.Amount;
import com.example.android.json.Balance;
import com.example.android.json.FeedsArray;
import com.example.android.json.NewSavingGoal;
import com.example.android.json.SavingGoalResponse;
import com.example.android.json.SavingsGoalsArray;
import com.example.android.json.TransferResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StarlingService {

    @Headers("Accept:application/json")
    @GET("accounts")
    Call<AccountsArray> getAccounts(@Header("Authorization") String authorization);

    @Headers("Accept:application/json")
    @GET("accounts/{uuid}/balance")
    Call<Balance> getAccountBalance(@Header("Authorization") String authorization,@Path("uuid") String accountUid);

    @Headers("Accept:application/json")
    @GET("feed/account/{uuid}/category/{category}")
    Call<FeedsArray> getAccountFeed(@Header("Authorization") String authorization, @Path("uuid") String accountUid, @Path("category") String categoryUid, @Query("changesSince") String changesSince);

    @Headers("Accept:application/json")
    @GET("account/{uuid}/savings-goals")
    Call<SavingsGoalsArray> getSavingGoals(@Header("Authorization") String authorization, @Path("uuid") String accountUid);

    @PUT("account/{uuid}/savings-goals")
    Call<SavingGoalResponse> addSavingGoal(@Header("Authorization") String authorization, @Path("uuid") String accountUid, @Body NewSavingGoal savingGoal);

    @PUT("account/{uuid}/savings-goals/{savingsGoalUid}/add-money/{transferUid}")
    Call<TransferResponse> transferMoney(@Header("Authorization") String authorization, @Path("uuid") String accountUid, @Path("savingsGoalUid") String savingsGoalUid, @Path("transferUid") String transferUid, @Body Amount amount);
}

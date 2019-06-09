package com.example.android.savemoney;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.api.StarlingService;
import com.example.android.json.Account;
import com.example.android.json.AccountsArray;
import com.example.android.json.Amount;
import com.example.android.json.Balance;
import com.example.android.json.Feed;
import com.example.android.json.FeedsArray;
import com.example.android.json.NewSavingGoal;
import com.example.android.json.SavingGoal;
import com.example.android.json.SavingGoalResponse;
import com.example.android.json.SavingsGoalsArray;
import com.example.android.json.TransferResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * The ideal interface would be to call the get accounts method and show the user all his accounts with a reciclerView.
 * Then the user would select one account and we would use that account to calculate the balance. since this a test app we will skip
 * that and use the first account of the list and use that.
 */
public class MainActivity extends AppCompatActivity {

    //This constants shoulnt be here, but this is a test so they are staying here
    public static final String BASE_URL = "https://api-sandbox.starlingbank.com/api/v2/";
    public static final String ACCESS_TOKEN = "Bearer YBmpruEUbbWXUqmgNyDxLb5htZnwyyKmTBPquDBjbUo6SlZDgEcxdUUY9g3Jjjhu";
    public static final String WEEKLY_SAVING_GOAL = "Weekly Savings";

    // No translations here
    public static final String ACCOUNT_ERROR = "We couldn't find your accounts. Sorry!!";
    public static final String BALANCE_ERROR = "We couldn't find your account balance. Sorry!!";
    public static final String FEED_ERROR = "We couldn't find your transactions. Sorry!!";
    public static final String SAVING_GOAL_ERROR = "We couldn't find your weekly saving goal. Sorry!!";
    public static final String TRANSFER_GOAL_ERROR = "We couldn't do the transfer. Sorry!!";

    private TextView text;
    Button transferButton;

    private StarlingService service;

    private Account account = null;
    double amountToTransfer = 0.0;
    private String savingsGoalUid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.balance);

        transferButton=findViewById(R.id.transfer_button);
        transferButton.setOnClickListener(v -> {
            text.setText("Transferring money");
            transferButton.setVisibility(View.INVISIBLE);
            transferMoney();
        });

        // custom gson to parse dates
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .create();

        // added an interceptor to log the httprequest (had problems because I copied the wrong url)
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        service = retrofit.create(StarlingService.class);

        getAccounts();
    }

    /**
     * Gets the list of all accounts the user has
     */
    private void getAccounts() {

        text.setText("Finding accounts: ");
        Call<AccountsArray> call = service.getAccounts(ACCESS_TOKEN);

        call.enqueue(new Callback<AccountsArray>() {
            @Override
            public void onResponse(Call<AccountsArray> call, Response<AccountsArray> response) {
                if (!response.isSuccessful()) {
                    text.setText("We couldn't find your accounts. Sorry!!");
                    return;
                }

                AccountsArray accountsArray = response.body();

                if (accountsArray == null ) {
                    text.setText(ACCOUNT_ERROR);
                    return;
                }
                Account[] accounts = accountsArray.getAccounts();

                if (accounts == null || accounts.length ==0) {
                    // He doesnt have accounts??
                    text.setText(ACCOUNT_ERROR);
                    return;
                }
                account = accounts[0];
                getAccountBalance();

                /*
                account list for testing purposes
                for (Accounts account : accounts.getAccounts()) {
                    StringBuilder data = new StringBuilder( );

                    data.append("uuid: " + account.getAccountUid() + "\n");
                    data.append("User ID: " + account.getCategory() + "\n");
                    data.append("Title: " + account.getCurrency() + "\n");
                    data.append("Text: " + account.getCreatedAt() + "\n\n");

                    text.append(data.toString());
                }*/
            }

            @Override
            public void onFailure(Call<AccountsArray> call, Throwable t) {
                text.setText(ACCOUNT_ERROR);
            }
        });
    }

    /**
     * Gets the balance of the user account. I thought it would be a nice touch to show the user
     * how much money he has instead of doing the transfer directly. After getting the balance we get all the transfer feeds
     */
    private void getAccountBalance() {

        if (account == null) {
            text.setText(BALANCE_ERROR);
            return;
        }

        Call<Balance> call = service.getAccountBalance(ACCESS_TOKEN, account.getAccountUid());

        call.enqueue(new Callback<Balance>() {
            @Override
            public void onResponse(Call<Balance> call, Response<Balance> response) {


                if (!response.isSuccessful()) {
                    text.setText(BALANCE_ERROR);
                    return;
                }

                Balance balance = response.body();

                StringBuilder data = new StringBuilder( );
                data.append("YOUR BALANCE:\n");
                data.append(balance.getEffectiveBalance().getMinorUnits() + " " + balance.getEffectiveBalance().getCurrency() + "\n");
                text.append(data.toString());

                getFeed();
            }

            @Override
            public void onFailure(Call<Balance> call, Throwable t) {
                text.setText(BALANCE_ERROR);
            }
        });
    }

    /**
     * Gets all the transactions of the last week
     */
    private void getFeed() {

        if (account == null) {
            text.append("\n" + FEED_ERROR);
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        Date date = cal.getTime();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String change =format.format(date);

        Call<FeedsArray> call = service.getAccountFeed(ACCESS_TOKEN,
                account.getAccountUid(),account.getCategory(), change);

        call.enqueue(new Callback<FeedsArray>() {
            @Override
            public void onResponse(Call<FeedsArray> call, Response<FeedsArray> response) {
                if (!response.isSuccessful()) {
                    text.append("\n" + FEED_ERROR);
                    return;
                }

                FeedsArray feedsArray = response.body();

                if (feedsArray == null ) {
                    text.append("\n" + FEED_ERROR );
                    return;
                }
                Feed[] feeds = feedsArray.getFeedItems();

                if (feeds == null || feeds.length ==0) {
                    // He doesnt have feeds??
                    text.append("\n" + FEED_ERROR);
                    return;
                }

                double totalSpent = 0;

                for (Feed feed : feeds) {

                    if ("OUT".equals(feed.getDirection())) {
                        //I will asume here that the currency is always GBP because I don´t want to create a currency converter
                        //Also.. should I check the status? I will also assume not
                        totalSpent += feed.getAmount().getAmount();
                    }
                    // If I take into account the income my variable will problably be negatva (I hope so for the sake of the user)
                    // so won't include this
                    /*else  if ("IN".equals(feed.getDirection())) {
                        totalSpent -= feed.getAmount().getMinorUnits();
                    }
                    */
                }
                int roundUp = (int) Math.ceil(totalSpent);
                amountToTransfer = roundUp - totalSpent;

                text.append("\n" + String.format(Locale.UK,"You spent %.2f GPD this week and the roundUp is %d", totalSpent, roundUp));
                text.append("\n" + String.format(Locale.UK,"Do you want to transfer  %.2f GPD to your weekly savings?", amountToTransfer));

                getSavingGoal();
            }

            @Override
            public void onFailure(Call<FeedsArray> call, Throwable t) {
                text.append("\n" + FEED_ERROR);
            }
        });
    }

    /**
     * Gets the list of all the saving goals of the account and tries to find one with the name Weekly Savings and uses that for the transfer.
     * If the goal doesn't exitst, creates a new saving goal named weekly savings and then transfers the money.
     */
    private void getSavingGoal() {

        if (account == null) {
            text.append("\n" + SAVING_GOAL_ERROR);
            return;
        }

        Call<SavingsGoalsArray> call = service.getSavingGoals(ACCESS_TOKEN,account.getAccountUid());

        call.enqueue(new Callback<SavingsGoalsArray>() {
            @Override
            public void onResponse(Call<SavingsGoalsArray> call, Response<SavingsGoalsArray> response) {
                if (!response.isSuccessful()) {
                    text.append("\n" + SAVING_GOAL_ERROR);
                    return;
                }

                SavingsGoalsArray savingsGoalArray = response.body();
                SavingGoal[] savingsGoals = null;
                SavingGoal weeklySavingGoal = null;

                if (savingsGoalArray != null) {
                    savingsGoals = savingsGoalArray.getSavingGoals();
                }

                //I should check this on more detail (could these ever be null and reach this part of the code??)
                if (savingsGoals != null && savingsGoals.length > 0) {
                    for (SavingGoal savingGoal :savingsGoals) {
                        if (weeklySavingGoal != null) {
                            break;
                        }

                        if (WEEKLY_SAVING_GOAL.equals(savingGoal.getName())) {
                            weeklySavingGoal = savingGoal;
                        }
                    }
                }

                if (weeklySavingGoal != null) {
                    //transfer the money
                    savingsGoalUid = weeklySavingGoal.getSavingsGoalUid();
                    transferButton.setVisibility(View.VISIBLE);
                } else {
                    //Create new saving goal
                    addSavingGoal();
                }
            }

            @Override
            public void onFailure(Call<SavingsGoalsArray> call, Throwable t) {
                text.append("\n" + SAVING_GOAL_ERROR);
            }
        });
    }

    /**
     * Creates a new saving goal
     */
    private void addSavingGoal() {

        if (account == null) {
            text.append("\n" + SAVING_GOAL_ERROR);
            return;
        }

        NewSavingGoal savingGoal = new NewSavingGoal(WEEKLY_SAVING_GOAL,"GBP","GBP",1000,"");

        Call<SavingGoalResponse> call = service.addSavingGoal(ACCESS_TOKEN,account.getAccountUid(),savingGoal);
        call.enqueue(new Callback<SavingGoalResponse>() {
            @Override
            public void onResponse(Call<SavingGoalResponse> call, Response<SavingGoalResponse> response) {

                if (!response.isSuccessful()) {
                    text.append("\n" + SAVING_GOAL_ERROR);
                    return;
                }

                SavingGoalResponse savingGoalResponse = response.body();
                if (savingGoalResponse == null ) {
                    text.append("\n" + SAVING_GOAL_ERROR + "\n");
                    return;
                }

                if (savingGoalResponse.isSuccess()) {
                    //transfer the money
                    savingsGoalUid = savingGoalResponse.getSavingsGoalUid();
                    transferButton.setVisibility(View.VISIBLE);
                } else {
                    text.append("\n" + SAVING_GOAL_ERROR + "\n");
                    return;
                }
            }

            @Override
            public void onFailure(Call<SavingGoalResponse> call, Throwable t) {
                text.append("\n" + SAVING_GOAL_ERROR);
            }
        });
    }

    /**
     * Transfers the money to the saving goal
     */
    private void transferMoney() {

        if (account == null || savingsGoalUid == null || "".equals(savingsGoalUid) || amountToTransfer == 0.0 ) {
            text.setText(TRANSFER_GOAL_ERROR);
            return;
        }

        // we multiply by 100 to get the minor unit
        Amount amount = new Amount("GBP",amountToTransfer*100);
        String transferUuid =  UUID.randomUUID().toString();

        Call<TransferResponse> call = service.transferMoney(ACCESS_TOKEN,account.getAccountUid(),savingsGoalUid,transferUuid, amount);

        call.enqueue(new Callback<TransferResponse>() {
            @Override
            public void onResponse(Call<TransferResponse> call, Response<TransferResponse> response) {
                if (!response.isSuccessful()) {
                    text.setText(TRANSFER_GOAL_ERROR);
                    return;
                }

                TransferResponse transferResponse = response.body();
                //Should be checking this more in detail to see if the transfer was made
                // This should never be null unless it was a coding problem
                if (transferResponse == null ) {
                    text.setText(TRANSFER_GOAL_ERROR);
                    return;
                }

                if (transferResponse.isSuccess()) {
                    text.setText("Transfered");
                } else {
                    text.setText(TRANSFER_GOAL_ERROR);
                    return;
                }


            }

            @Override
            public void onFailure(Call<TransferResponse> call, Throwable t) {
                text.setText(TRANSFER_GOAL_ERROR);
            }
        });
    }
}

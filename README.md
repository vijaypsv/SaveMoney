# SaveMoney
Starling Bank Technical Challenge from Vijay Sabnani Vaswani.

## Project Overwiew
This project is a native Android App That gets a user account and calculates all the money spent in the last week. Then rounds up that quantity to the nearest pound shows a button to the user to transfer the difference between those two amounts to a saving goal.

## App functions
1. First the app loads all the user accounts and gets the first account on the list (On an ideal app it would show a list of accounts to the user so he can select one).
2. Then it gets the balance of that account to show it to the user.
3. After that call it gets the transaction feed of the last week and calculates the amount to transfer.
4. Then it loads all the saving goals from that account and looks a goal named "Weekly Savings". If it doesn't have one it creates it.
5. If everything went well it will show the user the transfer button.
6. When the user press the button it will do the transfer.

## Libraries used
The main library used is retrofit https://square.github.io/retrofit/ for handling all the http requests.
I have also used https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor for logging all the request for debugging purposes.

## Usage
Since this is a test app I havent put a lot of effort into the architecture so there are few things harcoded. The main harcoded value that should be updated is the API access token header that is hardcoded in a constant named ACCESS_TOKEN. Once that value is updated we can execute the app normally.

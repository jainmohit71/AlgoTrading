# AlgoTrading
## Overview

This application uses Zerodha api to put a buy and sell order for trade.
Basically I have developed this application mainly for scalping where you have to 
put your buy and sell order (as a limit order). 
When putting a buy order along with sell limit order, a gtt order is placed on zerodha for the
sell order after buy order is completed. This gtt order gets completed and then our sell order is placed 
and then it should also get completed to completely execute our sell order.
This is taking too much time as if we are trading in options, the fluctuation can reach to the sell order limit and gets
back below it due to which our gtt order only gets completed and sell order remains opened and we can hit our stop loss most
of the time.
Doing this manually (placing buy order and then placing sell order) from zerodha application takes a long time as we have to
hit the orders from UI and we can even go in loss most of the time.

In this application, I will put a buy order and when this order gets completed, application will automatically put a sell order
limit (say +2) which is configurable and and this order will be executed in a very minute timeframe without any gtt order in between.

This has been very helpful for me mainly for scalping.
You can modify the application according to your needs. 
I haven't put anything related to stoploss in application but this is a small change which you can put as per your requirement.

## Setup requirements
1. Open a zerodha account.

2. Go to https://developers.kite.trade 
   Create a new app by selecting 'Connect' type. By selecting this, you will be purchasing zerodha api for Rs. 2000/month.
   Now you will be able to put buy, sell order and get data related to trades.

3. When the above app is created, an api key and api secret will be provide to you. 
   API key is the code used to authenticate a user (authenticates that the user is valid).

4. Clone repository 'AlogTrading' on your local. A jar file 'kiteconnect.jar' is also provided here, add it to the dependencies.

5. A certification KiteCert.cer should also be added to javakeystore otherwise below erro will be shown:
   HTTP FAILED: javax.net.ssl.SSLHandshakeException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
   unable to find valid certification path to requested target
   Follow below command to add certificate:
   Save a certificate from chrome by going into settings for the kite url hit and put it at location C:\code\KiteCert.cer
   Now hit below command to add certificate to Javakeystore
   keytool -import -alias MyCert -keystore "C:\Program Files\Java\jdk1.8.0_333\jre\lib\security\cacerts" -storepass changeit -file C:\code\KiteCert.cer

6. To get request token, hit url https://kite.zerodha.com/connect/login?v=3&api_key=<your_api_key>
   and then you will get a session token as request token is valid for a very small time frame.

7. Now when hitting any api, use above session token and api key along with the request data.




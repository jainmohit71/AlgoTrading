# AlgoTrading
## Overview

This application uses Zerodha api to put a buy and sell order for your trade.
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

## Setup reqirements




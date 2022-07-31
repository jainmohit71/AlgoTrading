import Const.AppConstants;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.OrderParams;
import com.zerodhatech.ticker.KiteTicker;
import com.zerodhatech.ticker.OnOrderUpdate;

import java.io.*;
import java.util.List;
import java.util.Properties;

public class MJClientConnect {
    private static KiteConnect kiteConnect;

    public static void main(String[] args) throws IOException, KiteException {
        String ordersFileName = AppConstants.ORDER_FILENAME;
        String keysFileName = AppConstants.TOKEN_FILENAME;
        Properties ordersProp = new Properties();
        InputStream is = new FileInputStream(ordersFileName);
        ordersProp.load(is);
        is.close();

        Properties keysProp = new Properties();
        InputStream is2 = new FileInputStream(keysFileName);
        keysProp.load(is2);
        is2.close();

        String apiKey = keysProp.getProperty("apiKey");
        String accessToken = keysProp.getProperty("accessToken");

        kiteConnect = new KiteConnect(apiKey, true);
        kiteConnect.setAccessToken(accessToken);

        // order parameters
        OrderParams orderParams = new OrderParams();
        orderParams.quantity = Integer.valueOf(ordersProp.getProperty("orderQuantity"));
        orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
        orderParams.tradingsymbol = ordersProp.getProperty("optionSymbol");
        //orderParams.product = Constants.PRODUCT_CNC;
        orderParams.product = Constants.PRODUCT_NRML;
        orderParams.exchange = Constants.EXCHANGE_NFO;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.triggerPrice = 0.0;
        orderParams.tag = "myTag"; //tag is optional and it cannot be more than 8 characters and only alphanumeric is allowed

        // List<Instrument> instruments = kiteConnect.getInstruments("NFO");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter price to buy :");
        Double price = Double.valueOf(bufferedReader.readLine());
        orderParams.price = price;
        Order order = kiteConnect.placeOrder(orderParams, Constants.VARIETY_REGULAR);
        System.out.println(order.orderId);

        KiteTicker tickerProvider = new KiteTicker(accessToken, apiKey);
        /** Set listener to get order updates.*/
        tickerProvider.setOnOrderUpdateListener(new OnOrderUpdate() {
            @Override
            public void onOrderUpdate(Order order) {
                System.out.println("order update " + order.orderId);
                Order newOrder;
                try {
                    List<Order> orders = kiteConnect.getOrderHistory(order.orderId);
                    int lastOrderUpdate = orders.size() - 1;
                    // If buy order is completed, put sell order and exit
                    if (orders.get(lastOrderUpdate).status.equals(Constants.ORDER_COMPLETE)) {
                        orderParams.price = price + 2;
                        orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;
                        newOrder = kiteConnect.placeOrder(orderParams, Constants.VARIETY_REGULAR);
                        System.out.println("newOrderId" + newOrder.orderId);
                        tickerProvider.disconnect();
                    }
                    // If orders are cancelled or Rejected, exit
                    else if (orders.get(lastOrderUpdate).status.equals(Constants.ORDER_CANCELLED) ||
                            orders.get(lastOrderUpdate).status.equals(Constants.ORDER_REJECTED)) {
                        tickerProvider.disconnect();
                    }
                } catch (KiteException | IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        // Make sure this is called before calling connect.
        tickerProvider.setTryReconnection(true);
        //maximum retries and should be greater than 0
        tickerProvider.setMaximumRetries(10);
        //set maximum retry interval in seconds
        tickerProvider.setMaximumRetryInterval(30);
        tickerProvider.connect();

        /** You can check, if websocket connection is open or not using the following method.*/
        boolean isConnected = tickerProvider.isConnectionOpen();
        System.out.println("tickerProvider connected - " + isConnected);
    }
}

import Const.AppConstants;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.models.User;

import java.io.*;
import java.util.Properties;

/**
 * @author mohit.jain
 * @contact email: mohit.mohit.jain34@gmail.com
 * @since Jul 31, 2022 8:37 PM
 */
public class AccessTokenGenerator {
    public static void main(String[] args) throws IOException {
        String fileName = AppConstants.TOKEN_FILENAME;
        Properties prop = new Properties();
        InputStream is = new FileInputStream(fileName);
        prop.load(is);
        is.close();

        String requestToken = prop.getProperty("requestToken");
        String apiKey = prop.getProperty("apiKey");
        String apiSecret = prop.getProperty("apiSecret");
        KiteConnect kiteConnect = new KiteConnect(apiKey, true);

        User user;
        try {
            user = kiteConnect.generateSession(requestToken, apiSecret);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        prop.setProperty("accessToken", user.accessToken);
        OutputStream os = new FileOutputStream(fileName);
        prop.store(os, null); // FileOutputStream
        os.close();
    }
}

package adcar.com.network;

import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by amitb on 03/02/16.
 */
public class NetworkUtils {

    public static String appendUrlParams(String url, HashMap<String, String> params)
            throws UnsupportedEncodingException
    {
        if(params == null) {
            return url;
        }
        StringBuilder result = new StringBuilder(url);
        result.append("?");
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}

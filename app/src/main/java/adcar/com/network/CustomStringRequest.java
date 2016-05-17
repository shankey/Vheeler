package adcar.com.network;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Collections;
import java.util.Map;

/**
 * Created by amitb on 02/02/16.
 */
public class CustomStringRequest extends StringRequest {
    private int mStatusCode;
    private Map<String, String> headerParams = null;
    private Map<String, String> bodyParams = null;

    public CustomStringRequest(int method, Map<String, String> headerParams, Map<String, String> bodyParams,
                               String url, Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.headerParams = headerParams;
        this.bodyParams = bodyParams;
        this.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public CustomStringRequest(int method, String url, Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public int getStatusCode() {
        return mStatusCode;
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if(bodyParams == null || bodyParams.equals(Collections.emptyMap())) {
            return super.getParams();
        }
        return bodyParams;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        mStatusCode = response.statusCode;
        return super.parseNetworkResponse(response);
    }
}

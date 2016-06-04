package adcar.com.handler;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import adcar.com.cache.Cache;
import adcar.com.database.dao.CampaignRunDAO;
import adcar.com.factory.Factory;
import adcar.com.model.CampaignRun;
import adcar.com.model.servertalkers.CampaignSchedule;
import adcar.com.model.servertalkers.ExhaustedCampaign;
import adcar.com.model.servertalkers.ExhaustedCampaignResponse;
import adcar.com.model.servertalkers.GetCampaignScheduleResponse;
import adcar.com.model.servertalkers.GetExhaustedCampaignRequest;
import adcar.com.model.servertalkers.GetExhaustedCampaignResponse;
import adcar.com.network.CustomStringRequest;
import adcar.com.network.UrlPaths;

/**
 * Created by adinema on 22/05/16.
 */
public class ExhaustedCampaignRunHandler extends Handler {

    private CampaignRunDAO campaignRunDAO = (CampaignRunDAO)Factory.getInstance().get(Factory.DAO_CAMPAIGN_RUN);

    public void SyncExhaustedCampaignRuns(){
        final List<ExhaustedCampaign> lastRunCampaigns = new ArrayList<ExhaustedCampaign>();
        lastRunCampaigns.addAll(Cache.getCache().getCampaignRunSet());

        if(lastRunCampaigns.size() == 0){
            return;
        }

        Log.i("EXHAUSTEDRUN", "input = " + lastRunCampaigns.toString());
        GetExhaustedCampaignRequest getExhaustedCampaignRequest = new GetExhaustedCampaignRequest();
        getExhaustedCampaignRequest.setExhaustedCampaignList(lastRunCampaigns);
        String url = UrlPaths.GET_EXHAUSTED_CAMPAIGN_RUNS+ "?" + "json="+ URLEncoder.encode(gson.toJson(getExhaustedCampaignRequest));
        Log.i("EXHAUSTEDRUN", "url = " + url);
        CustomStringRequest csr = new CustomStringRequest(Request.Method.GET, null, null,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        GetExhaustedCampaignResponse allExhaustedCampaigns = gson.fromJson(response.toString(), GetExhaustedCampaignResponse.class);
                        Log.i("EXHAUSTEDRUN", "Raw Response = " + response.toString());
                        Log.i("EXHAUSTEDRUN", "Response = " + allExhaustedCampaigns.toString());

                        //transform incoming data to database savable format
                        for(ExhaustedCampaignResponse exhaustedCampaignResponse: allExhaustedCampaigns.getExhaustedCampaignList()){
                            Log.i("EXHAUSTEDRUN", "Interator = " + allExhaustedCampaigns.toString());
                            campaignRunDAO.updateCampaignRuns(exhaustedCampaignResponse.getCampaignInfoId(), exhaustedCampaignResponse.getDate(), exhaustedCampaignResponse.getActive());
                        }
                        Cache.getCache().getCampaignRunSet().removeAll(lastRunCampaigns);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("EXHAUSTEDRUN", "Response = " + error.getMessage());
                    }
                });

        networkManager.addToRequestQueue(csr);
    }
}

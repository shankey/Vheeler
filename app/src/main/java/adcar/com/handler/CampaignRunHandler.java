package adcar.com.handler;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URLEncoder;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adcar.com.database.dao.CampaignInfoDAO;
import adcar.com.database.dao.CampaignRunDAO;
import adcar.com.factory.Factory;
import adcar.com.model.CampaignInfo;
import adcar.com.model.CampaignRun;
import adcar.com.model.servertalkers.CampaignInfoIdentifier;
import adcar.com.model.servertalkers.GetCampaignScheduleRequest;
import adcar.com.model.servertalkers.CampaignSchedule;
import adcar.com.model.servertalkers.GetCampaignScheduleResponse;
import adcar.com.network.CustomStringRequest;
import adcar.com.network.UrlPaths;

/**
 * Created by adinema on 07/05/16.
 */
public class CampaignRunHandler extends Handler {

    private static CampaignRunDAO campaignRunDAO = (CampaignRunDAO) Factory.getInstance().get(Factory.DAO_CAMPAIGN_RUN);
    private static CampaignInfoDAO campaignInfoDAO = (CampaignInfoDAO) Factory.getInstance().get(Factory.DAO_CAMPAIGN_INFO);
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    public synchronized static void StartSyncCampaignRuns(){
        List<CampaignInfo> campaignInfos = campaignInfoDAO.getUnSyncedCampaigns();
        Log.i("CAMPAIGNRUN", "campaigns to sync " + campaignInfos.size() + " ---" + campaignInfos.toString());
        SyncCampaignRuns(campaignInfos);
    }

    public void manipulateData(GetCampaignScheduleResponse allCampaigns){


    }

    private static Map<Integer, List<CampaignInfo>> aggregateByCampaigns(List<CampaignInfo> campaignInfoList){
        Map<Integer, List<CampaignInfo>> map = new HashMap<Integer, List<CampaignInfo>>();

        for(CampaignInfo campaignInfo: campaignInfoList){
            if(map.containsKey(campaignInfo.getCampaignId())){
                map.get(campaignInfo.getCampaignId()).add(campaignInfo);
            }else{
                List<CampaignInfo> list = new ArrayList<CampaignInfo>();
                list.add(campaignInfo);
                map.put(campaignInfo.getCampaignId(), list);
            }
        }
        return map;
    }

    public static void SyncCampaignRuns(final List<CampaignInfo> campaignInfoList){

        Map<Integer, List<CampaignInfo>> IdCampaignInfomap = aggregateByCampaigns(campaignInfoList);
        Log.i("CAMPAIGNRUN", "aggregated campaigns" + IdCampaignInfomap);
        for(Integer campaignId: IdCampaignInfomap.keySet()){
            //provision to send and get a list of campaignareaads
            final List<CampaignInfoIdentifier> campaignInfoIdentifiers = new ArrayList<CampaignInfoIdentifier>();
            List<CampaignInfo> campaignInfoSubList = IdCampaignInfomap.get(campaignId);

            for(CampaignInfo campaignInfo: campaignInfoSubList){
                CampaignInfoIdentifier campaignInfoIdentifier = new CampaignInfoIdentifier();
                campaignInfoIdentifier.setCampaignInfoId(campaignInfo.getCampaignInfoId());
                campaignInfoIdentifiers.add(campaignInfoIdentifier);

            }


            GetCampaignScheduleRequest getCampaignScheduleRequest = new GetCampaignScheduleRequest();
            getCampaignScheduleRequest.setCampaignInfoIds(campaignInfoIdentifiers);
            Log.i("CAMPAIGNRUN", "incoming request = "+gson.toJson(getCampaignScheduleRequest));
            String url = UrlPaths.GET_CAMPAIGN_SCHEDULE+ "?" + "json="+ URLEncoder.encode(gson.toJson(getCampaignScheduleRequest));
            Log.i("CAMPAIGNRUN", "hitting the url = "+url);
            Map<String, String> map = new HashMap<String, String>();


            CustomStringRequest csr = new CustomStringRequest(Request.Method.GET, map, null,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            GetCampaignScheduleResponse allCampaigns = gson.fromJson(response.toString(), GetCampaignScheduleResponse.class);

                            //transform incoming data to database savable format
                            for(CampaignSchedule campaignSchedule: allCampaigns.getCampaignSchedules()){
                                for(CampaignRun campaignRun: campaignSchedule.getSchedule()){
                                    campaignRun.setCampaignInfoId(campaignSchedule.getCampaignInfoId());
                                }
                            }

                            Log.i("CAMPAIGNRUN", "Raw Response = " + response.toString());
                            Log.i("CAMPAIGNRUN", "Response = " + allCampaigns.toString());

                            for(CampaignSchedule campaignSchedule: allCampaigns.getCampaignSchedules()){
                                campaignRunDAO.deleteCampaignRunsForCampaignInfo(campaignSchedule.getCampaignInfoId());
                                campaignRunDAO.addCampaignRuns(campaignSchedule.getSchedule());
                                campaignInfoDAO.updateCampaignStatus(campaignSchedule.getCampaignInfoId(), 1);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("SYNCHANDLER", "Response = " + error.getMessage());
                        }
                    });

            networkManager.addToRequestQueue(csr);
        }
    }
}

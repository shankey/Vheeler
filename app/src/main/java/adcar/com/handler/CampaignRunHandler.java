package adcar.com.handler;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adcar.com.database.dao.CampaignInfoDAO;
import adcar.com.database.dao.CampaignRunDAO;
import adcar.com.factory.Factory;
import adcar.com.model.Ad;
import adcar.com.model.CampaignRun;
import adcar.com.model.servertalkers.Ads;
import adcar.com.model.servertalkers.AreaAd;
import adcar.com.model.CampaignInfo;
import adcar.com.model.servertalkers.CampaignAreaAd;
import adcar.com.model.servertalkers.Campaigns;
import adcar.com.model.servertalkers.GetCampaignScheduleRequest;
import adcar.com.model.servertalkers.GetCampaigns;
import adcar.com.model.servertalkers.Schedule;
import adcar.com.network.CustomStringRequest;
import adcar.com.network.UrlPaths;

/**
 * Created by adinema on 07/05/16.
 */
public class CampaignRunHandler extends Handler {

    private static CampaignRunDAO campaignRunDAO = (CampaignRunDAO) Factory.getInstance().get(Factory.DAO_CAMPAIGN_RUN);
    private static CampaignInfoDAO campaignInfoDAO = (CampaignInfoDAO) Factory.getInstance().get(Factory.DAO_CAMPAIGN_INFO);

    public synchronized static void StartSyncCampaignRuns(){
        List<CampaignInfo> campaignInfos = campaignInfoDAO.getUnSyncedCampaigns();
        Log.i("SYNCHANDLER", "campaigns to sync " + campaignInfos.size() + " ---" + campaignInfos.toString());
        SyncCampaignRuns(campaignInfos);
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

        Map<Integer, List<CampaignInfo>> IdInfoCampaignmap = aggregateByCampaigns(campaignInfoList);
        Log.i("SYNCHANDLER", "aggregated campaigns" + IdInfoCampaignmap);
        for(Integer campaignId: IdInfoCampaignmap.keySet()){
            //provision to send and get a list of campaignareaads
            final List<CampaignAreaAd> campaignAreaAds = new ArrayList<CampaignAreaAd>();
            List<CampaignInfo> campaignInfoSubList = IdInfoCampaignmap.get(campaignId);

            for(CampaignInfo campaignInfo: campaignInfoSubList){
                CampaignAreaAd campaignAreaAd = new CampaignAreaAd();

                campaignAreaAd.setCampaignId(campaignInfo.getCampaignId());
                campaignAreaAd.setAreaId(campaignInfo.getAreaId());
                campaignAreaAd.setAdId(campaignInfo.getAdId());

                campaignAreaAds.add(campaignAreaAd);

            }


            GetCampaignScheduleRequest getCampaignScheduleRequest = new GetCampaignScheduleRequest();
            getCampaignScheduleRequest.setCampaigns(campaignAreaAds);
            Log.i("SYNCHANDLER", "incoming request = "+gson.toJson(getCampaignScheduleRequest));
            String url = UrlPaths.GET_CAMPAIGN_SCHEDULE+ "?" + "json="+ URLEncoder.encode(gson.toJson(getCampaignScheduleRequest));
            Log.i("SYNCHANDLER", "hitting the url = "+url);
            Map<String, String> map = new HashMap<String, String>();


            CustomStringRequest csr = new CustomStringRequest(Request.Method.GET, map, null,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            GetCampaigns allCampaigns = gson.fromJson(response.toString(), GetCampaigns.class);

                            Log.i("SYNCHANDLER", "Raw Response = " + response.toString());
                            Log.i("SYNCHANDLER", "Response = " + allCampaigns.toString());



                            for(Campaigns camps: allCampaigns.getCampaigns()){
                                Integer campaignId = camps.getCampaignId();

                                for(AreaAd areaAd: camps.getAreaAds()){
                                    List<CampaignRun> campaignRunList = new ArrayList<CampaignRun>();
                                    CampaignInfo campaignInfo = campaignInfoDAO.getCampaign(campaignId.toString(), areaAd.getAdId().toString(), areaAd.getAreaId().toString());


                                    for(Schedule schedule :areaAd.getSchedule()){
                                        CampaignRun campaignRun = new CampaignRun();
                                        campaignRun.setCampaignInfoId(campaignInfo.getId());
                                        campaignRun.setDate(Date.valueOf(schedule.getDate()));
                                        campaignRun.setActive(1);
                                        campaignRunList.add(campaignRun);

                                    }
                                    campaignRunDAO.deleteCampaignRunsForCampaignInfo(campaignId, areaAd.getAreaId(), areaAd.getAdId());
                                    campaignRunDAO.addCampaignRuns(campaignRunList);
                                    campaignInfo.setStatus(1);
                                    campaignInfoDAO.updateCampaignStatus(campaignId, areaAd.getAreaId(), areaAd.getAdId(), 1);
                                    Log.i("DATABASE", campaignRunList.toString());
                                }
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

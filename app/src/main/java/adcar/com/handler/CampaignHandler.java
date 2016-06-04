package adcar.com.handler;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import adcar.com.database.dao.AdDAO;
import adcar.com.database.dao.CampaignInfoDAO;
import adcar.com.database.dao.CampaignRunDAO;
import adcar.com.factory.Factory;
import adcar.com.model.Ad;
import adcar.com.model.servertalkers.Ads;

import adcar.com.model.servertalkers.AreaAd;
import adcar.com.model.CampaignInfo;
import adcar.com.model.servertalkers.Campaigns;
import adcar.com.model.servertalkers.CampaignSchedule;
import adcar.com.model.servertalkers.ExpiredCampaignResponse;
import adcar.com.model.servertalkers.GetActiveCampaignResponse;
import adcar.com.model.servertalkers.GetExpiredCampaignRequest;
import adcar.com.model.servertalkers.GetExpiredCampaignResponse;
import adcar.com.network.CustomStringRequest;
import adcar.com.network.UrlPaths;

/**
 * Created by aditya on 09/02/16.
 */
public class CampaignHandler extends Handler {

    public static AdDAO adDAO = (AdDAO) Factory.getInstance().get(Factory.DAO_AD);
    public static CampaignInfoDAO campaignInfoDAO = (CampaignInfoDAO) Factory.getInstance().get(Factory.DAO_CAMPAIGN_INFO);
    public static CampaignRunDAO campaignRunDAO = (CampaignRunDAO) Factory.getInstance().get(Factory.DAO_CAMPAIGN_RUN);


    public void SyncCampaigns(){
        Log.i("CAMPAIGNSYNC", "Trying to sync campaigns");
        CustomStringRequest csr = new CustomStringRequest(Request.Method.GET,
                UrlPaths.GET_ALL_CAMPAIGNS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        GetActiveCampaignResponse activeCampaigns = gson.fromJson(response.toString(), GetActiveCampaignResponse.class);
                        Ads ads = gson.fromJson(response.toString(), Ads.class);
                        Log.i("ADSSYNC", "Raw Response = " + response.toString());
                        Log.i("ADSSYNC", "Response = " + activeCampaigns.toString());
                        Log.i("ADSSYNC", "Response = " + ads.toString());

                        List<CampaignInfo> campaignList = new ArrayList<CampaignInfo>();
                        for(Campaigns camps: activeCampaigns.getCampaigns()){
                            Integer campaignId = camps.getCampaignId();

                            for(CampaignInfo campaignInfo: camps.getCampaignInfos()){

                                CampaignInfo dbCampaign = campaignInfoDAO.getCampaign(campaignInfo.getCampaignInfoId().toString());
                                if(dbCampaign==null || dbCampaign.getVersion() < campaignInfo.getVersion()){
                                    CampaignInfo campaign = new CampaignInfo();
                                    campaign.setCampaignId(campaignId);
                                    campaign.setCampaignInfoId(campaignInfo.getCampaignInfoId());
                                    campaign.setAdId(campaignInfo.getAdId());
                                    campaign.setAreaId(campaignInfo.getAreaId());
                                    campaign.setVersion(campaignInfo.getVersion());
                                    if(campaignInfo.getActive()==1){
                                        campaign.setStatus(0);
                                    }else{
                                        campaign.setStatus(1);
                                        campaignRunDAO.updateActiveCampaignRuns(campaignInfo.getCampaignInfoId(), 0);
                                    }
                                    campaign.setActive(campaignInfo.getActive());
                                    campaignList.add(campaign);
                                }else{
                                    Log.i("ADSSYNC", "Skipping ADSSYNC");
                                }
                            }
                        }
                        Log.i("ADSSYNC", "campaign list to save or update = " + campaignList.toString());
                        campaignInfoDAO.saveOrUpdate(campaignList);


                        for(Ad ad: ads.getAds()){
                            Ad dbAd = adDAO.getAd(ad.getAdId());
                            if(dbAd == null){
                                ad.setStatus(0);
                                adDAO.addAd(ad);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("CAMPAIGNSYNC", "Error while syncing campaigns", error);
                    }
                });

        networkManager.addToRequestQueue(csr);
    }

    public void SyncExpiredCampaigns(){
        Log.i("EXPIREDCAMPAIGNSYNC", "Trying to sync expired campaigns");
        GetExpiredCampaignRequest getExpiredCampaignRequest = new GetExpiredCampaignRequest();
        List<Integer> campaignIds = campaignInfoDAO.getActiveCampaignIds();
        getExpiredCampaignRequest.setCampaignIds(campaignIds);
        Log.i("EXPIREDCAMPAIGNSYNC", "Input for expired campaigns sync = "+ campaignIds.toString());
        String url = UrlPaths.GET_ACTIVE_CAMPAIGNS+ "?" + "json="+ URLEncoder.encode(gson.toJson(getExpiredCampaignRequest));
        CustomStringRequest csr = new CustomStringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        GetExpiredCampaignResponse activeCampaigns = gson.fromJson(response.toString(), GetExpiredCampaignResponse.class);

                        Log.i("EXPIREDCAMPAIGNSYNC", "Raw Response = " + response.toString());
                        Log.i("EXPIREDCAMPAIGNSYNC", "Response = " + activeCampaigns.toString());

                        for(ExpiredCampaignResponse expiredResponse : activeCampaigns.getExpiredCampaigns()){

                            if(expiredResponse.getActive() == 0){
                                campaignInfoDAO.updateCampaignActive(expiredResponse.getCampaignId(), expiredResponse.getActive());
                                campaignRunDAO.deleteByCampaignId(expiredResponse.getCampaignId().toString());
                                campaignInfoDAO.deleteFromCampaignId(expiredResponse.getCampaignId().toString());
                            }


                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("EXPIREDCAMPAIGNSYNC", "Error while syncing campaigns", error);
                    }
                });

        networkManager.addToRequestQueue(csr);
    }
}

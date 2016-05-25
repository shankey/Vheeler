package adcar.com.handler;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import adcar.com.database.dao.AdDAO;
import adcar.com.database.dao.CampaignInfoDAO;
import adcar.com.factory.Factory;
import adcar.com.model.Ad;
import adcar.com.model.servertalkers.Ads;

import adcar.com.model.servertalkers.AreaAd;
import adcar.com.model.CampaignInfo;
import adcar.com.model.servertalkers.Campaigns;
import adcar.com.model.servertalkers.CampaignSchedule;
import adcar.com.model.servertalkers.GetActiveCampaignResponse;
import adcar.com.network.CustomStringRequest;
import adcar.com.network.UrlPaths;

/**
 * Created by aditya on 09/02/16.
 */
public class CampaignHandler extends Handler {

    public static AdDAO adDAO = (AdDAO) Factory.getInstance().get(Factory.DAO_AD);
    public static CampaignInfoDAO campaignInfoDAO = (CampaignInfoDAO) Factory.getInstance().get(Factory.DAO_CAMPAIGN_INFO);

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
                                        // TODO : update campain runs to active state 0 - no API calling
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
}

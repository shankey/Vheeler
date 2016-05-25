package adcar.com.algorithms;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import adcar.com.database.dao.AdDAO;
import adcar.com.database.dao.CampaignInfoDAO;
import adcar.com.factory.Factory;
import adcar.com.model.Ad;
import adcar.com.model.CampaignInfo;

/**
 * Created by adinema on 17/05/16.
 */
public class AdAlgorithms {

    private static CampaignInfoDAO campaignInfoDAO = (CampaignInfoDAO)Factory.getInstance().get(Factory.DAO_CAMPAIGN_INFO);
    public static Random random = new Random();

    public static CampaignInfo getAdIdToDisplay(Integer areaId, String time){

        List<CampaignInfo> campaignInfoList = campaignInfoDAO.getAdsForArea(areaId, time);

        if(campaignInfoList.size()==0){
            return null;
        }

        Collections.sort(campaignInfoList);

        int size = campaignInfoList.size();
        int no = getRandomNoBetween(0, size-1);
        return campaignInfoList.get(no);
    }

    public static Integer getRandomNoBetween(Integer a, Integer b){
        return  random.nextInt(b-a+1) + a;
    }

}

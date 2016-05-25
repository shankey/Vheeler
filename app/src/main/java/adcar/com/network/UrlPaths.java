package adcar.com.network;

/**
 * Created by amitb on 29/01/16.
 */
public class UrlPaths {

    public static final String BASE_URL = "http://vhee.us-west-2.elasticbeanstalk.com";

    public static final String GET_AREAS = BASE_URL + "/area";
    public static final String POST_COORDINATES = BASE_URL + "/coordinate";
    public static final String POST_COORDINATES_BATCH = BASE_URL + "/coordinate_batch";
    public static final String GET_VERSIONS = BASE_URL + "/versions";
    public static final String GET_ALL_CAMPAIGNS = BASE_URL + "/get_all_campaigns";
    public static final String GET_CAMPAIGN_SCHEDULE = BASE_URL + "/get_campaign_schedule";
    public static final String GET_EXHAUSTED_CAMPAIGN_RUNS = BASE_URL + "/get_exhausted_campaign_runs";

}

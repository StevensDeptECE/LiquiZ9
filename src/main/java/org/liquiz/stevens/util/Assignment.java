package org.liquiz.stevens.util;

public class Assignment {
    private String lis_outcome_service_url;
    private String lis_result_sourcedid;

    public Assignment(String url, String sourcedid){
        this.lis_outcome_service_url = url;
        this.lis_result_sourcedid = url;
    }

    public String getLis_outcome_service_url(){
        return lis_outcome_service_url;
    }

    public void setLis_outcome_service_url(String url){
        lis_outcome_service_url = url;
    }

    public String getLis_result_sourcedid(){
        return lis_result_sourcedid;
    }

    public void setLis_result_sourcedid(String sourcedid){
        lis_result_sourcedid = sourcedid;
    }
}

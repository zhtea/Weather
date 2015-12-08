package cn.ed.pku.sean.weather.bean;

import java.io.Serializable;

/**
 * Created by Bryce on 2015/10/4.
 */
public class TodayWeather implements Serializable{
    private String city;
    private String updatetime;
    private String wendu;
    private String shidu;
    private String pm25 ;
    private String quality;
    private String fengxiang;
    private String fengli;
    private String date;
    private String high;
    private String low;
    private String type;
    private String fdate0="123";
    private String fhigh0="123";
    private String flow0="123";
    private String ftype0="123";
    private String ffengxiang0="123";


    public String getFdate0() {
        return fdate0;
    }

    public void setFdate0(String fdate0) {
        this.fdate0 = fdate0;
    }

    public String getFhigh0() {
        return fhigh0;
    }

    public void setFhigh0(String fhigh0) {
        this.fhigh0 = fhigh0;
    }

    public String getFlow0() {
        return flow0;
    }

    public void setFlow0(String flow0) {
        this.flow0 = flow0;
    }

    public String getFtype0() {
        return ftype0;
    }

    public void setFtype0(String ftype0) {
        this.ftype0 = ftype0;
    }

    public String getFfengxiang0() {
        return ffengxiang0;
    }

    public void setFfengxiang0(String ffengxiang0) {
        this.ffengxiang0 = ffengxiang0;
    }




    public String getCity(){
        return city;
    }
    public void setCity(String city){
        this.city = city;
    }
    public String getUpdatetime(){
        return updatetime;
    }
    public void setUpdatetime(String updatetime){
        this.updatetime = updatetime;
    }
    public String getWendu(){
        return wendu;
    }
    public void setWendu(String wendu){
        this.wendu = wendu;
    }
    public String getPm25(){
        return pm25;
    }
    public void setPm25(String pm25){
        this.pm25 = pm25;
    }
    public String getShidu(){
        return shidu;
    }
    public void setShidu(String shidu){
        this.shidu = shidu;
    }
    public String getQuality(){
        return quality;
    }
    public void setQuality(String quality){
        this.quality = quality;
    }
    public String getFengxiang(){
        return fengxiang;
    }
    public void setFengxiang(String fengxiang){
        this.fengxiang = fengxiang;
    }
    public String getFengli(){
        return fengli;
    }
    public void setFengli(String fengli){
        this.fengli = fengli;
    }
    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date = date;
    }
    public String getHigh(){
        return high;
    }
    public void setHigh(String high){
        this.high = high;
    }
    public String getLow(){
        return low;
    }
    public void setLow(String low){
        this.low = low;
    }
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }


    public String toString(){
         return "TodayWeather{ "+"city='"+city+'\''+", updatetime='"+updatetime+'\''
                 +", wendu='"+wendu+'\''+", shidu='"+shidu+'\''+", pm2.5='"+pm25+'\''
                 +", quality='"+quality+'\''+", fengxiang'"+fengxiang+'\''+
                 ", fengli='"+fengli+'\''+", date='"+date+'\''+", high='"+high+'\''
                 +", low='"+low+'\''+", type='"+type+'\''+'}';
    }
}

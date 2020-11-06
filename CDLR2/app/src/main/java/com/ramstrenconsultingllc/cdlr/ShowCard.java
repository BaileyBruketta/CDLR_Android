package com.ramstrenconsultingllc.cdlr;

public class ShowCard {
    public int icon;
    public String activeName;
    public String currentBid;
    public Boolean yourPlace;
    public Boolean hotel;
    public String serviceLength;
    public String timeFrame;
    public String bidTimeLeft;
    public String bidderCount;
    public String ratingCount;
    public ShowCard(){
        super();
    }

    public ShowCard(int icon, String activeName, String currentBid, Boolean yourPlace, Boolean hotel, String serviceLength, String timeFrame, String bidTimeLeft, String bidderCount, String ratingCount ){
        super();
        this.icon = icon;
        this.activeName = activeName;
        this.currentBid = currentBid;
        this.yourPlace = yourPlace;
        this.hotel = hotel;
        this.serviceLength = serviceLength;
        this.timeFrame = timeFrame;
        this.bidTimeLeft = bidTimeLeft;
        this.bidderCount = bidderCount;
        this.ratingCount = ratingCount;
    }
}

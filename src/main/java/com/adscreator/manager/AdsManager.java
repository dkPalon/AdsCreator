package com.adscreator.manager;

import com.adscreator.models.Campaign;
import com.adscreator.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdsManager {
    private final Logger LOGGER = LoggerFactory.getLogger(AdsManager.class);
    private final CampaignManager campaignManager;

    @Autowired
    public AdsManager(CampaignManager campaignManager) {
        this.campaignManager = campaignManager;
    }

    public Campaign createCampaign(Campaign campaignHttp) {
        campaignManager.addCampaign(campaignHttp);
        return campaignHttp;
    }

    public Product serveAd(String category) {
        return campaignManager.getProductWithHighestBidding(category);
    }
}

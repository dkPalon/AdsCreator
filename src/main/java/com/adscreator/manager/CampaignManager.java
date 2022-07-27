package com.adscreator.manager;

import com.adscreator.models.Campaign;
import com.adscreator.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CampaignManager {

    private final ProductManager productManager;
    private final List<Campaign> campaignList = new ArrayList<>();

    @Autowired
    public CampaignManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void addCampaign(Campaign campaign) {
        campaignList.add(campaign);
    }

    // TODO handle null (give highest bid)
    // TODO handle time 10 days
    public Product getProductWithHighestBidding(String category) {
        String bidProductID = null;
        double highestBid = 0;
        for (Campaign campaign : this.campaignList) {
            if (campaign.bid() > highestBid) {
                for (String productID : campaign.productIDs()) {
                    if (this.productManager.getProduct(productID).category().equals(category)) {
                        bidProductID = productID;
                        highestBid = campaign.bid();
                        break;
                    }
                }
            }
        }
        return this.productManager.getProduct(bidProductID);
    }
}

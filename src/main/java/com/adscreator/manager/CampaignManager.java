package com.adscreator.manager;

import com.adscreator.models.Campaign;
import com.adscreator.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
public class CampaignManager {

    private final ProductManager productManager;
    private final List<Campaign> campaignList = new ArrayList<>();
    private final Logger LOGGER = LoggerFactory.getLogger(CampaignManager.class);

    @Autowired
    public CampaignManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void addCampaign(Campaign campaign) {
        boolean nameIsFree = campaignList.stream().noneMatch(t -> t.name().equals(campaign.name()));
        boolean validIDs = productManager.getIDs().containsAll(Arrays.asList(campaign.productIDs()));
        if (!nameIsFree || campaign.bid() < 0 || !validIDs) {
            LOGGER.info("Received invalid Campaign");
            LOGGER.info("Name check: " + nameIsFree);
            LOGGER.info("Bid: " + campaign.bid());
            LOGGER.info("Valid IDs: " + validIDs);
            throw new IllegalArgumentException();
        }
        campaignList.add(campaign);
    }

    // TODO handle null (give highest bid)
    public Product getProductWithHighestBidding(String category) {
        String bidProductID = null;
        double highestBid = 0;
        for (Campaign campaign : this.campaignList) {
            if (campaign.bid() > highestBid && (DAYS.between(campaign.startTime(), LocalDate.now())) <= 10) {
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

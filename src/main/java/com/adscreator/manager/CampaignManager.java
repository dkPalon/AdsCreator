package com.adscreator.manager;

import com.adscreator.models.Campaign;
import com.adscreator.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
public class CampaignManager {

    private final ProductManager productManager;
    private final List<Campaign> campaignList = new ArrayList<>();
    private final HashMap<String, List<Campaign>> categoryMap = new HashMap<>();
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
        addToCategoryMap(campaign);
    }

    private void addToCategoryMap(Campaign campaign) {
        for (String productID: campaign.productIDs()) {
            Product product = this.productManager.getProduct(productID);
            if (this.categoryMap.get(product.category()) != null) {
                this.categoryMap.get(product.category()).add(campaign);
            } else {
                List<Campaign> categoryList = new LinkedList<>();
                categoryList.add(campaign);
                this.categoryMap.put(product.category(), categoryList);
            }
        }
    }

    public Product getProductWithHighestBidding(String category) {
        List<Campaign> categoryList = this.categoryMap.get(category);
        Product product = iterateOverCampaigns(categoryList, category, false);
        if (product == null) {
            product = iterateOverCampaigns(this.campaignList, category, true);
        }
        return product;
    }

    public Product iterateOverCampaigns(List<Campaign> campaignList, String category,
                                                Boolean ignoreCategory) {
        String bidProductID = null;
        double highestBid = 0;
        for (Campaign campaign : campaignList) {
            if (campaign.bid() > highestBid && (DAYS.between(campaign.startTime(), LocalDate.now())) <= 10) {
                for (String productID : campaign.productIDs()) {
                    if (this.productManager.getProduct(productID).category().equals(category) || ignoreCategory) {
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

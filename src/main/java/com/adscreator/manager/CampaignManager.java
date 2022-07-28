package com.adscreator.manager;

import com.adscreator.data.CampaignRepository;
import com.adscreator.models.Campaign;
import com.adscreator.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class CampaignManager {

    private final Logger LOGGER = LoggerFactory.getLogger(CampaignManager.class);
    private final CampaignRepository campaignRepository;
    private final ProductManager productManager;

    @Autowired
    public CampaignManager(CampaignRepository campaignRepository, ProductManager productManager) {
        this.campaignRepository = campaignRepository;
        this.productManager = productManager;
    }

    public void addCampaign(Campaign campaign) {
        boolean nameIsFree = campaignRepository.getAllNames().stream().noneMatch(
                t -> t.equals(campaign.getName()));
        boolean validIDs = productManager.getIDs().containsAll(campaign.getProductIDs());
        if (!nameIsFree || campaign.getBid() <= 0 || !validIDs) {
            LOGGER.info("Received invalid Campaign");
            LOGGER.info("Name check: " + nameIsFree);
            LOGGER.info("Bid: " + campaign.getBid());
            LOGGER.info("Valid IDs: " + validIDs);
            throw new IllegalArgumentException();
        }
        campaignRepository.save(campaign);
        campaignRepository.flush();
    }

    public Product getProductWithHighestBidding(String category) {
        List<String> categoryProducts = productManager.getProductsInCategory(category).stream().map(Product::getSerialNumber).toList();
        List<Campaign> categoryList = this.campaignRepository.findCampaignsByProductIDsIn(categoryProducts);
        Product product = null;
        if (categoryList != null) {
            product = iterateOverCampaigns(categoryList, category, false);
        }
        if (product == null) {
            product = iterateOverCampaigns(this.campaignRepository.findByOrderByBidAsc(), category, true);
        }
        return product;
    }

    public Product iterateOverCampaigns(List<Campaign> campaignList, String category,
                                                Boolean ignoreCategory) {
        String bidProductID = null;
        double highestBid = 0;
        for (Campaign campaign : campaignList) {
            if (campaign.getBid() > highestBid && (DAYS.between(campaign.getStartTime(), LocalDate.now())) <= 10) {
                for (String productID : campaign.getProductIDs()) {
                    if (this.productManager.getProduct(productID).getCategory().equals(category) || ignoreCategory) {
                        bidProductID = productID;
                        highestBid = campaign.getBid();
                        break;
                    }
                }
            }
        }
        return this.productManager.getProduct(bidProductID);
    }
}

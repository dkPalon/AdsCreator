package com.adscreator.http;

import com.adscreator.manager.adsManager;
import com.adscreator.models.Campaign;
import com.adscreator.models.CampaignHttp;
import com.adscreator.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class adsController {
    private final Logger LOGGER = LoggerFactory.getLogger(adsController.class);
    private final adsManager adsManager;

    @Autowired
    public adsController(adsManager adsManager) {
        this.adsManager = adsManager;
    }

    @PostMapping(path = "/createCampaign}", produces = "application/json")
    public Campaign createCampaign(@RequestBody CampaignHttp campaignHttp) {
        LOGGER.info("Got request for the following campaign creation" + campaignHttp);
        Campaign campaign = adsManager.createCampaign(campaignHttp);
        LOGGER.info("Sending back created campaign: " + campaign);
        return campaign;
    }

    @GetMapping(path = "/serveAd/category={category}", produces = "application/json")
    public Product serveAd (@PathVariable String category) {
        LOGGER.info("Got Request for ad serving for Category: " + category);
        Product product = adsManager.serveAd(category);
        LOGGER.info("Sending back product: " + product);
        return product;
    }

}

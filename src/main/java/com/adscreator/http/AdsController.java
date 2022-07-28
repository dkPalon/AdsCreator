package com.adscreator.http;

import com.adscreator.manager.AdsManager;
import com.adscreator.models.Campaign;
import com.adscreator.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1")
public class AdsController {
    private final Logger LOGGER = LoggerFactory.getLogger(AdsController.class);
    private final AdsManager adsManager;

    @Autowired
    public AdsController(AdsManager adsManager) {
        this.adsManager = adsManager;
    }

    @PostMapping(path = "/createCampaign", produces = "application/json")
    public Campaign createCampaign(@RequestBody Campaign campaignHttp, HttpServletResponse response) {
        LOGGER.info("Got request for the following campaign creation" + campaignHttp);
        try {
            Campaign campaign = adsManager.createCampaign(campaignHttp);
            LOGGER.info("Sending back created campaign: " + campaign);
            return campaign;
        } catch (IllegalArgumentException e) {
            response.setStatus(400);
            return null;
        }
    }

    @GetMapping(path = "/serveAd/category={category}", produces = "application/json")
    public Product serveAd (@PathVariable String category, HttpServletResponse response) {
        LOGGER.info("Got Request for ad serving for Category: " + category);
        Product product = adsManager.serveAd(category);
        LOGGER.info("Sending back product: " + product);
        if (product == null) {
            response.setStatus(400);
        }
        return product;
    }

}

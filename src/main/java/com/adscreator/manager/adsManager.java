package com.adscreator.manager;

import com.adscreator.models.Campaign;
import com.adscreator.models.CampaignHttp;
import com.adscreator.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class adsManager {
    private final Logger LOGGER = LoggerFactory.getLogger(adsManager.class);

    public Campaign createCampaign(CampaignHttp campaignHttp) {
        List<Product> list = new ArrayList<Product>();
        list.add(new Product("test",
                "test", 50.0, "A4G"));
        return new Campaign("test", list
                , LocalDateTime.now());
    }

    public Product serveAd(String category) {
        return new Product("test", "test", 50.0, "A4G");
    }
}

package com.adscreator.data;

import com.adscreator.models.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    List<Campaign> findCampaignsByProductIDsIn(List<String> productIDs);

    List<Campaign> findByOrderByBidAsc();

    @Query("select table.name from Campaign table")
    Set<String> getAllNames();
}

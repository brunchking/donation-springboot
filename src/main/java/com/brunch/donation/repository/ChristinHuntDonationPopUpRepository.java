package com.brunch.donation.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.brunch.donation.model.Streamer;
import com.brunch.donation.model.donationpopup.ChivesWangDonationPopUp;
import com.brunch.donation.model.donationpopup.ChristinHuntDonationPopUp;

public interface ChristinHuntDonationPopUpRepository extends MongoRepository<ChristinHuntDonationPopUp, String> {
	@Query("{merchant_trade_no: '?0'}")
	ChristinHuntDonationPopUp findChristinHuntDonationPopUpByMerchantOrderNo(String merchantOrderNo);
	
	@Query("{flag: ?0}")
	List<ChristinHuntDonationPopUp> findChristinHuntDonationPopUpByFlag(int flag);
}

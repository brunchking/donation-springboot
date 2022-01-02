package com.brunch.donation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.brunch.donation.config.Config;
import com.brunch.donation.model.Donation;
import com.brunch.donation.model.DonationForm;
import com.brunch.donation.model.Streamer;
import com.brunch.donation.repository.StreamerRepository;
import com.brunch.donation.util.EcpayUtils;


@RestController
public class DonateController {
	@Autowired
	StreamerRepository streamerRepo;

	@Autowired
	private Config config;

	@Autowired
	EcpayUtils ecpayUtils;
	
	
//	@GetMapping("/donate/{payment_method}")
	@PostMapping("/donate")
	public String donation(@RequestBody DonationForm donationForm) {
//	public String donation(@PathVariable String payment_method) {
//		getDonationFormDetail(donationForm);
//		DonationForm donationForm = null;
		// init ecpay
		EcpayUtils.initial();
		String htmlPage = "";
//		switch(payment_method) {
		switch(donationForm.getPayment_method()) {
			case "credit_card":
				htmlPage = EcpayUtils.genAioCheckOutOneTime(config, donationForm);
				break;
			case "cvs_barcode":
				htmlPage = EcpayUtils.genAioCheckOutCVS(config, donationForm);
				break;
			case "webATM":
				htmlPage = EcpayUtils.genAioCheckOutWebATM(config, donationForm);
				break;
			case "ATM":
				htmlPage = EcpayUtils.genAioCheckOutATM(config, donationForm);
				break;
//			case "ALL":
//				htmlPage = EcpayUtils.genAioCheckOutALL(config, donationForm);
			default:
				break;
		}
		return htmlPage;
		
		// QUERY ORDER
//		System.out.println("queryTradeInfo: " + EcpayUtils.postQueryTradeInfo());
//		return "/donate";
	}
	
	

	public void getDonationFormDetail(DonationForm donationForm) {
		System.out.println(donationForm.getName());
		System.out.println(donationForm.getPayment_method());
		System.out.println(donationForm.getAmount());
		System.out.println(donationForm.getMessage());
		System.out.println(donationForm.getTarget());

	}

	public void getDetail(Streamer streamer) {
		System.out.println(streamer.getId());
		System.out.println(streamer.getName());
		List<Donation> donation = streamer.getDonation();
		for (Donation dona : donation) {
			System.out.println(dona.get_id());
			System.out.println(dona.getName());
			System.out.println(dona.getPayment_method());
			System.out.println(dona.getAmount());
			System.out.println(dona.getMessage());
			System.out.println(dona.getCreate_time());
			System.out.println(dona.getModify_time());
		}
	}
}

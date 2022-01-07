package com.brunch.donation.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.brunch.donation.config.Config;
import com.brunch.donation.model.Donation;
import com.brunch.donation.model.Streamer;
import com.brunch.donation.repository.StreamerRepository;
import com.brunch.donation.util.EcpayUtils;

@RestController
public class ReceiveController {
	private static final Logger log = LoggerFactory.getLogger(ReceiveController.class);
	@Autowired
	private Config config;

	@Autowired
	EcpayUtils ecpayUtils;

	@Autowired
	StreamerRepository streamerRepo;

	@PostMapping("/receive")
	public String receive(@RequestParam Map<String, String> requstBody) {
		EcpayUtils.initial();
		// 9699C8A1CF16DF49930F522BF761640ED37A87862C43A6A6B1EE6CC96CF13FEE
		// ecpay規定交易成功須回傳"1|OK"
		boolean isSuccess = false;
		// 驗證檢查碼
		Hashtable<String, String> dict = new Hashtable<String, String>();
		dict.putAll(requstBody);

		if (EcpayUtils.cmprChkMacValue(config, dict)) {
			try {
				String target = requstBody.get("CustomField1").split(",")[0];
				String merchantTradeNo = requstBody.get("CustomField1").split(",")[1];
				Donation donation = new Donation();
				donation.set_id(new ObjectId());
				donation.setMerchant_trade_no(merchantTradeNo);
				donation.setPayment_method(requstBody.get("CustomField2"));
				donation.setAmount(Integer.valueOf(requstBody.get("CustomField3")));
				String donator = requstBody.get("CustomField4").split(",")[0];
				String message = requstBody.get("CustomField4").split(",")[1];
				donation.setName(donator);
				donation.setMessage(message);
				// MongoDB 需要轉換+8小時
				Calendar ca = Calendar.getInstance();
				ca.setTime(new Date());
				ca.add(Calendar.HOUR_OF_DAY, 8);
				donation.setCreate_time(ca.getTime());
				donation.setModify_time(ca.getTime());
				Streamer streamer = streamerRepo.findStreamerByName(target);
				List<Donation> donationList = streamer.getDonation();
				donationList.add(donation);
				streamerRepo.save(streamer);
				isSuccess = true;
			} catch (Exception e) {
				log.error("Insert donation fail, requestBody = ");
			}
		}
		log.info("isSuccess = [" + isSuccess + "]");
		return isSuccess ? "1|OK" : "0|ERROR|";
	}

	@GetMapping("/alert")
	public ModelAndView alert() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("alert.html");
		return modelAndView;
	}
	
//	@PostMapping("/getTest")
//	public void test() {
//		String name = "streamer01";
//		Donation donation = new Donation();
//		donation.set_id(new ObjectId());
//		donation.setPayment_method("credit_cared69");
//		donation.setAmount(7414);
//		donation.setMessage("hello ooooooo");
//		// MongoDB 需要轉換+8小時
//		Calendar ca = Calendar.getInstance();
//		ca.setTime(new Date());
//		ca.add(Calendar.HOUR_OF_DAY, 8);
//		donation.setCreate_time(ca.getTime());
//		donation.setModify_time(ca.getTime());
//		Streamer streamer = streamerRepo.findStreamerByName(name);
//		List<Donation> donationList = streamer.getDonation();
//		donationList.add(donation);
//		streamerRepo.save(streamer);
//	}
}
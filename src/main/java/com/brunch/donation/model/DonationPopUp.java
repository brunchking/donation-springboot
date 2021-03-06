package com.brunch.donation.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class DonationPopUp {
	private ObjectId _id;
	private String name;
	private int amount;
	private String message;

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

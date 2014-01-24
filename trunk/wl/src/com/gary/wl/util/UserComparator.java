package com.gary.wl.util;

import java.util.Comparator;

import com.gary.wl.entity.User;

public class UserComparator implements Comparator<User> {

	@Override
	public int compare(User arg0, User arg1) {
		// TODO Auto-generated method stub
		Boolean online1 = arg0.getLogin().getOnline();
		Boolean online2 = arg1.getLogin().getOnline();
		int a = online1 == null || online1 == false ? 0 : 1;
		int b = online2 == null || online2 == false ? 0 : 1;
		int result = b - a;
		return result == 0 ? 1 : result;
	}

}

package com.gary.wl.util;

import com.gary.mail.dto.SendConfig;
import com.gary.mail.impl.AbstractSendMail;

public class SendMailUtil extends AbstractSendMail {

	@Override
	public SendConfig getConfig() {
		// TODO Auto-generated method stub
		SendConfig sendConfig = new SendConfig("smtp.exmail.qq.com", "25", "gary@cn-face.com", "zxj19910611");
		sendConfig.setSSL(true);
		sendConfig.setSslSmtpPort("465");
		return sendConfig;
	}

	@Override
	protected boolean debug() {
		// TODO Auto-generated method stub
		return false;
	}

}

package com.gary.wl.util;

import java.util.Comparator;
import java.util.Set;

import com.gary.wl.entity.Chat;
import com.gary.wl.entity.Message;


public class ChatComparator implements Comparator<Chat> {

	@Override
	public int compare(Chat arg0, Chat arg1) {
		// TODO Auto-generated method stub
		Set<Message> messages1 = arg0.getMessages();
		Set<Message> messages2 = arg1.getMessages();
		long msg1 = 0;
		long msg2 = 0;
		if(messages1 == null || messages1.isEmpty()){
			msg1 = 0;
		}else{
			if(messages1.iterator().hasNext()){
				Message message = messages1.iterator().next();
				if(message == null){
					msg1 = 0;
				}else{
					msg1 = message.getDate().getTime();
				}
			}
		}
		if(messages2 == null || messages2.isEmpty()){
			msg2 = 0;
		}else{
			if(messages2.iterator().hasNext()){
				Message message = messages2.iterator().next();
				if(message == null){
					msg2 = 0;
				}else{
					msg2 = message.getDate().getTime();
				}
			}
		}
		int result = (int) (msg2 - msg1);
		return result == 0 ? 1 : result;
	}

}

package com.gary.wl.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gary.framework.controller.BaseController;
import com.gary.framework.result.ExecuteResult;
import com.gary.framework.result.Result;
import com.gary.wl.entity.Chat;
import com.gary.wl.entity.Message;
import com.gary.wl.entity.User;
import com.gary.wl.error.MyErrorCode;
import com.gary.wl.service.ChatService;
import com.gary.wl.service.MessageService;
import com.gary.wl.service.UserService;

@Controller
@RequestMapping("msg")
public class MessageController extends BaseController {
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ChatService chatService;
	
	@RequestMapping("read")
	@ResponseBody
	private Result read(@RequestParam String from, @RequestParam String to, @RequestParam Integer type, Integer state){
		Result result = new Result();
		Message msg = messageService.get(from, to, type, state != null ? state : Message.UNREAD);
		if(msg != null){
			msg.setState(Message.READ);
			messageService.update(msg);
			result.setSuccess(true);
		}else{
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
			result.setMsg("没有该消息");
		}
		return result;
	}
	
	@RequestMapping("friendFail")
	@ResponseBody
	private Result friendFail(@RequestParam String from, @RequestParam String to, @RequestParam String content, Integer state){
		Result result = new Result();
		Message msg = messageService.get(from, to, Message.FIREND_VALIDATION, Message.READ);
		if(msg != null){
			msg.setContent(content);
			msg.setState(Message.FIREND_VALIDATION_NO);
			messageService.update(msg);
			result.setSuccess(true);
		}else{
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
			result.setMsg("没有该消息");
		}
		return result;
	}
	
	@RequestMapping("friendOk")
	@ResponseBody
	private Result friendOk(@RequestParam String from, @RequestParam String to){
		Result result = new Result();
		Message msg = messageService.get(from, to, Message.FIREND_VALIDATION, Message.READ);
		if(msg != null){
			msg.setState(Message.FIREND_VALIDATION_YES);
			messageService.update(msg);
			result.setSuccess(true);
		}else{
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
			result.setMsg("没有该消息");
		}
		return result;
	}
	
	@RequestMapping("readMsg")
	@ResponseBody
	private Result readMsg(@RequestParam String id){
		Result result = new Result();
		Message msg = messageService.get(id);
		if(msg != null){
			msg.setState(Message.READ);
			messageService.update(msg);
			result.setSuccess(true);
		}else{
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
			result.setMsg("没有该消息");
		}
		return result;
	}
	
	@RequestMapping("unreads")
	@ResponseBody
	private Result unreads(@RequestParam String userid, @RequestParam String chatid, @RequestParam Integer type, Integer count){
		count = count == null ? 100 : count;
		Result result = new Result();
		User user = userService.get(userid);
		Chat chat = chatService.get(chatid);
		if(user != null && chat != null){
			List<Message> msgs = messageService.unReadList(user, chat, type, count);
			result.setSuccess(true);
			result.getData().put("msgs", msgs);
		}else{
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
		}
		return result;
	}
	
	@RequestMapping("allUnreads")
	@ResponseBody
	private Result allUnreads(@RequestParam String userid, Integer count, String notin){
		count = count == null ? 100 : count;
		Result result = new Result();
		User user = userService.get(userid);
		if(user != null){
			String[] ids = null;
			if(notin != null && notin.contains(",")){
				if(notin.endsWith(","))
					notin.substring(0, notin.length() - 1);
				ids = notin.split(",");
			}
			List<Message> msgs = messageService.unReadList(user, count, ids);
			result.setSuccess(true);
			result.getData().put("msgs", msgs);
		}else{
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
		}
		return result;
	}
	
	@RequestMapping("reads")
	@ResponseBody
	private Result reads(@RequestParam String ids){
		Result result = new Result();
		if(ids.endsWith(",")){
			ids.substring(0, ids.length() - 1);
		}
		String[] arr = ids.split(",");
		int res = messageService.updates(arr);
		result.setSuccess(res > 0);
		return result;
	}
	
	@RequestMapping("historyMsg")
	@ResponseBody
	private Result historyMsg(@RequestParam String chatId, String lastMsgId, @RequestParam String _userid){
		Result result = new Result();
		Chat chat = chatService.get(chatId);
		User user = userService.get(_userid);
		Message last = messageService.get(lastMsgId);
		if(last == null){
			last = new Message();
			last.setDate(new Date());
			last.setId("");
		}
		if(chat != null && user != null && last != null){
			List<Message> list = messageService.hisMsgList(user, chat, last, 10);
			boolean hasHisMsg = messageService.hasHisMsg(user, chat, list.get(list.size() - 1));
			result.setSuccess(true);
			result.getData().put("list", list);
			result.getData().put("has", hasHisMsg);
		}
		return result;
	}
}

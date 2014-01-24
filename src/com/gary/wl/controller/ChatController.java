package com.gary.wl.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gary.framework.controller.BaseController;
import com.gary.framework.result.ExecuteResult;
import com.gary.framework.result.Result;
import com.gary.wl.entity.Chat;
import com.gary.wl.entity.Group;
import com.gary.wl.entity.Message;
import com.gary.wl.entity.User;
import com.gary.wl.error.MyErrorCode;
import com.gary.wl.service.ChatService;
import com.gary.wl.service.GroupService;
import com.gary.wl.service.UserService;

@RequestMapping("chat")
@Controller
public class ChatController extends BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private ChatService chatService;
	@Autowired
	private GroupService groupService;
	
	@RequestMapping("talk")
	@ResponseBody
	private Result talk(@RequestParam String create, @RequestParam String to, String title){
		Result result = new Result();
		User user1 = userService.get(create);
		User user2 = userService.get(to);
		Chat chat = chatService.isHave(user1, user2);
		if(user1 == null || user2 == null){
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
		}else{
			if(isFriend(user1.getGroups(), user2) && isFriend(user2.getGroups(), user1)){
				if(chat == null){
					chat = new Chat();
					chat.setCreateDate(new Date());
					chat.setCreateUser(user1);
					chat.setType(Message.PRIVATE);
					chat.setTitle(StringUtils.isBlank(title) ? user2.getUserExt().getNickName() : title);
					chat.getUsers().add(user1);
					chat.getUsers().add(user2);
					chatService.save(chat);
				}
				result.getData().put("chat", chat);
				result.setSuccess(true);
			}else{
				result.setExecuteResult(new ExecuteResult(MyErrorCode.NO_ACCESS));
				result.setMsg("双方不是好友");
			}
		}
		return result;
	}
	
	@RequestMapping("groupTalk")
	@ResponseBody
	private Result groupTalk(@RequestParam String id, String userIds, String name, @RequestParam String _userid){
		Result result = new Result();
		Chat chat = chatService.get(id);
		User my = userService.get(_userid);
		List<String> newUsers = new ArrayList<String>();
		if(chat != null && my != null){
			if(!StringUtils.isBlank(userIds)){
				if(chat.getType().equals(Message.PRIVATE)){
					chat = new Chat();
					chat.setCreateUser(my);
					chat.setCreateDate(new Date());
					chat.setType(Message.GROUP);
					chatService.save(chat);
				}
				String[] sz = userIds.split(",");
				
				for (String string : sz) {
					User user = userService.get(string);
					if(user != null && !chat.getUsers().contains(user)){
						chat.getUsers().add(user);
						newUsers.add(string + "$#$" + user.getUserExt().getNickName());
					}
				}
				if(StringUtils.isBlank(name)){
					if(StringUtils.isBlank(chat.getTitle())){
						name = "";
						for (User u : chat.getUsers()) {
							name += u.getUserExt().getNickName() + ',';
						}
						name = name.substring(0, name.length() - 1);
						chat.setTitle(name);
					}
				}else{
					chat.setTitle(name);
				}
				chatService.update(chat);
			}
			chat.getCreate().setNoteName(chat.getCreateUser().getNoteName(_userid));
			result.setSuccess(true);
			result.getData().put("chat", chat);
			result.getData().put("newUsers", newUsers);
		}else{
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
			result.setMsg("会话不存在!");
		}
		return result;
	}
	
	@RequestMapping("outChat")
	@ResponseBody
	private Result outChat(@RequestParam String userId, @RequestParam String id){
		Result result = new Result();
		Chat chat = chatService.get(id);
		User user = userService.get(userId);
		if(chat != null && user != null){
			if(chat.getUsers().contains(user)){
				if(userId.equals(chat.getCreateUser().getId())){
					for (User u : chat.getUsers()) {
						u.getChats().remove(chat);
					}
					chat.setUsers(null);
					chat.setCreateUser(null);
					chatService.delete(chat);
				}else{
					user.getChats().remove(chat);
					chat.getUsers().remove(user);
					chatService.update(chat);
				}
			}
			result.setSuccess(true);
			result.getData().put("chat", chat);
		}else{
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
			result.setMsg("会话不存在!");
		}
		return result;
	}
	
	private boolean isFriend(Set<Group> groups, User user){
		for (Group group : groups) {
			if(groupService.haveUser(group, user)){
				return true;
			}
		}
		return false;
	}
	
	@RequestMapping("list")
	@ResponseBody
	private Result list(@RequestParam String userid){
		Result result = new Result();
		User user = userService.get(userid);
		if(user != null){
			List<Chat> list = chatService.list(user, 20);
			result.getData().put("list", list);
			result.setSuccess(true);
		}else{
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
		}
		return result;
	}
}

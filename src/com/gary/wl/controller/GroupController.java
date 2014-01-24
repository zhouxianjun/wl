package com.gary.wl.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gary.framework.controller.BaseController;
import com.gary.framework.result.ExecuteResult;
import com.gary.framework.result.Result;
import com.gary.wl.entity.Group;
import com.gary.wl.entity.User;
import com.gary.wl.error.MyErrorCode;
import com.gary.wl.service.GroupService;
import com.gary.wl.service.UserService;

@Controller
@RequestMapping("group")
public class GroupController extends BaseController {
	@Autowired
	private GroupService groupService;
	@Autowired
	private UserService userService;
	
	@RequestMapping("list")
	@ResponseBody
	private Result getGroupsByUser(@RequestParam String id){
		Result result = new Result();
		User user = userService.get(id);
		if(user != null){
			List<Group> list = groupService.list(user);
			result.getData().put("list", list);
			result.setSuccess(true);
		}else{
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
		}
		return result;
	}
	
	@RequestMapping("addGroup")
	@ResponseBody
	private Result addGroup(@RequestParam String id, @RequestParam String name){
		Result result = new Result();
		User user = userService.get(id);
		if(user != null && !isHaveGroupByUser(user, name)){
			Group group = new Group();
			group.setName(name);
			group.setState(Group.CUSTOM);
			group.setUser(user);
			groupService.save(group);
			result.getData().put("group", group);
			result.setSuccess(true);
		}else{
			result.setExecuteResult(new ExecuteResult(user == null ? MyErrorCode.NOT_FOUND : MyErrorCode.REPEATED_INSERT));
		}
		return result;
	}
	
	@RequestMapping("updateGroup")
	@ResponseBody
	private Result updateGroup(@RequestParam String id, @RequestParam String name){
		Result result = new Result();
		Group group = groupService.get(id);
		if(group != null){
			if(!group.getName().equals(name)){
				group.setName(name);
				groupService.save(group);
			}
			result.getData().put("group", group);
			result.setSuccess(true);
		}else{
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
		}
		return result;
	}
	
	@RequestMapping("delGroup")
	@ResponseBody
	private Result delGroup(@RequestParam String id){
		Result result = new Result();
		Group group = groupService.get(id);
		if(group != null && group.getState() != Group.NOT_UPDATE){
			group.setUser(null);
			group.setUsers(null);
			groupService.delete(group);
			result.setSuccess(true);
		}else{
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
		}
		return result;
	}
	
	private boolean isHaveGroupByUser(User user, String name){
		if(user != null){
			Set<Group> groups = user.getGroups();
			for (Group group : groups) {
				if(group.getName().equals(name)){
					return true;
				}
			}
		}
		return false;
	}
}

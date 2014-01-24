package com.gary.wl.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gary.core.util.AlgorithmUtils;
import com.gary.dao.result.Page;
import com.gary.framework.controller.BaseController;
import com.gary.framework.result.ExecuteResult;
import com.gary.framework.result.Result;
import com.gary.mail.dto.SendMail;
import com.gary.wl.dto.Config;
import com.gary.wl.dto.IPDtoExt;
import com.gary.wl.entity.Area;
import com.gary.wl.entity.Chat;
import com.gary.wl.entity.Group;
import com.gary.wl.entity.Login;
import com.gary.wl.entity.NoteName;
import com.gary.wl.entity.User;
import com.gary.wl.entity.UserExt;
import com.gary.wl.error.MyErrorCode;
import com.gary.wl.service.AreaService;
import com.gary.wl.service.ChatService;
import com.gary.wl.service.GroupService;
import com.gary.wl.service.NoteNameService;
import com.gary.wl.service.UserService;
import com.gary.wl.spring.bind.annotation.FormModel;
import com.gary.wl.util.SendMailUtil;
import com.gary.wl.util.Util;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private ChatService chatService;
	@Autowired
	private NoteNameService noteNameService;
	@Autowired
	private Config config;
	@Autowired
	private SendMailUtil sendMailUtil;
	
	@RequestMapping("login")
	@ResponseBody
	private Result login(@RequestParam String name, @RequestParam String password){
		Result result = new Result();
		User user = userService.getByName(name);
		if(user == null){
			user = userService.getByEmail(name);
		}
		if(user == null){
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
			result.setMsg("没有此用户!");
		}else{
			password = AlgorithmUtils.MD5(password + "{"+ user.getName() +"}");
			if(password.equals(user.getPassword())){
				result.setSuccess(true);
				result.getData().put("user", user);
			}else{
				result.setExecuteResult(new ExecuteResult(MyErrorCode.NO_ACCESS));
				result.setMsg("用户密码错误!");
			}
		}
		return result;
	}
	
	@RequestMapping("reg")
	@ResponseBody
	private Result reg(@RequestParam String name, @RequestParam String email, @RequestParam String password, @RequestParam String ip){
		Result result = new Result();
		User isName = userService.getByName(name);
		User isEmail = userService.getByEmail(email);
		if(isName == null && isEmail == null){
			User user = new User();
			user.setName(name);
			user.setPassword(AlgorithmUtils.MD5(password + "{"+ name +"}"));
			user.setEmail(email);
			
			UserExt userExt = new UserExt();
			userExt.setNickName(name);
			userExt.setUser(user);
			
			Login login = new Login();
			login.setLevel(0L);
			login.setTotalOnlineHour(0l);
			login.setUser(user);
			
			IPDtoExt ipDto = Util.getIPInfo(ip);
			if(ipDto != null){
				Area area = areaService.getByIp(ip);
				if(area == null){
					area = new Area();
					area.setCity(ipDto.getCity());
					area.setCountry(ipDto.getCountry());
					area.setIp(ip);
					area.setIsp(ipDto.getIsp());
					area.setProvince(ipDto.getProvince());
					area.setCitySN(ipDto.getCitySN());
					area.setProvinceSN(ipDto.getProvinceSN());
					area.setCountrySN(ipDto.getCountrySN());
					area.getUsers().add(user);
				}
				user.setArea(area);
			}
			
			Group group = new Group();
			group.setName("我的好友");
			group.setUser(user);
			group.setState(Group.NOT_UPDATE);
			user.setUserExt(userExt);
			user.setLogin(login);
			
			Set<Group> groups = new HashSet<>();
			groups.add(group);
			user.setGroups(groups);
			
			userService.save(user);
			result.setSuccess(true);
			result.getData().put("user", user);
			
			SendMail mail = new SendMail(new String[] { user.getEmail() }, "恭喜您,注册成功!", "恭喜您，成功注册了 微聊 帐号!您可以使用注册邮箱：<font color=red>"+user.getEmail()+"</font>,或注册帐号：<font color=red>"+user.getName()+"</font>;登录！此邮件为系统自动发送，请勿回复！");
			try {
				sendMailUtil.sendHtml(mail);
			} catch (EmailException e) {
				logger.error(e.getMessage(), e);
			}
		}else{
			result.setExecuteResult(new ExecuteResult(MyErrorCode.REPEATED_INSERT));
			result.setMsg(isName != null ? name + "该用户已存在!" : email + "该邮箱已被注册!");
			result.setSuccess(false);
		}
		return result;
	}
	
	@RequestMapping("info")
	@ResponseBody
	private Result getInfo(@RequestParam String id, String _userid){
		Result result = new Result();

		User user = userService.get(id);
		if(user == null){
			result.setSuccess(false);
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
		}else{
			result.setSuccess(true);
			result.getData().put("user", user);
		}
		
		return result;
	}
	
	@RequestMapping("update")
	@ResponseBody
	private Result update(@RequestParam String id, @FormModel("userExt") UserExt userExt, @FormModel("area") Area area) throws IOException{
		Result result = new Result();

		User user = userService.get(id);
		if(user == null){
			result.setSuccess(false);
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
		}else{
			if(userExt != null){
				Util.copyOrigNotNullPropertyToDestBean(user.getUserExt(), userExt);
			}
			if(area != null){
				Util.copyOrigNotNullPropertyToDestBean(user.getArea(), area);
			}
			userService.update(user);
			result.getData().put("user", user);
			result.setSuccess(true);
		}
		
		return result;
	}
	
	@RequestMapping("updateFace")
	@ResponseBody
	private Result updateFace(HttpServletRequest request, @RequestParam String id, @RequestParam MultipartFile face) throws IOException{
		Result result = new Result();

		User user = userService.get(id);
		if(user == null || face.isEmpty() && face.getSize() < 1){
			result.setSuccess(false);
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
		}else{
			String originalFilename = face.getOriginalFilename();
			String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
			byte[] data = face.getBytes();
			if(Util.isImg(suffix) && Util.imgSize(data, 100, 100)){
				String contentPath = request.getSession().getServletContext().getRealPath(File.separator + config.getUSER_FACE_SAVE_PATH())+File.separator;
				String name = id + "$" + System.currentTimeMillis() + suffix;
				File f = new File(contentPath);
				if(!f.exists()){
					f.mkdirs();
				}
				Util.getFile(data, contentPath, name);
				user.getUserExt().setFace(name);
			}else{
				return result;
			}
			userService.update(user);
			result.getData().put("user", user);
			result.setSuccess(true);
		}
		
		return result;
	}
	
	@RequestMapping(params = "type=2", value = "find")
	@ResponseBody
	private Result find(UserExt userExt, Area area, @RequestParam boolean online, Integer pageSize, Integer page){
		Result result = new Result();
		
		page = page == null ? 1 : page;
		pageSize = pageSize == null ? 20 : pageSize;
		User user = new User();
		if(area != null){
			if(area.getCity() != null && area.getCity().endsWith("市")){
				area.setCity(area.getCity().substring(0, area.getCity().length() - 1));
			}
		}
		user.setArea(area);
		user.setUserExt(userExt);
		Page<User> p = userService.find(user, pageSize, page, online);
		result.getData().put("page", p);
		result.setSuccess(true);
		return result;
	}
	
	@RequestMapping(params = "type=1", value = "find")
	@ResponseBody
	private Result findExact(String name, String nickName, @RequestParam boolean online, Integer pageSize, Integer page){
		Result result = new Result();
		
		page = page == null ? 1 : page;
		pageSize = pageSize == null ? 20 : pageSize;
		Page<User> p = userService.find(name, nickName, pageSize, page, online);
		
		result.getData().put("page", p);
		result.setSuccess(true);
		return result;
	}
	
	@RequestMapping("addFriend")
	@ResponseBody
	private Result addFriend(@RequestParam String userid, @RequestParam String groupid){
		Result result = new Result();
		
		Group group = groupService.get(groupid);
		User user = userService.get(userid);
		
		if(group != null && user != null){
			if(!groupService.haveUser(group, userid))
				groupService.addUser(group, user);
			result.setSuccess(true);
		}else{
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
		}
		return result;
	}
	
	@RequestMapping("delFriend")
	@ResponseBody
	private Result delFriend(@RequestParam String id, @RequestParam String userid, @RequestParam String groupid){
		Result result = new Result();
		
		Group group = groupService.get(groupid);
		User user = userService.get(userid);
		User me = userService.get(id);
		
		if(group != null && user != null && me != null){
			if(groupService.haveUser(group, userid))
				groupService.removeUser(group, user);
			Set<Group> groups = user.getGroups();
			for (Group group2 : groups) {
				if(groupService.haveUser(group2, id)){
					groupService.removeUser(group2, me);
					break;
				}
			}
			Chat chat = chatService.isHave(user, me);
			if(chat != null){
				user.getChats().remove(chat);
				me.getChats().remove(chat);
				chat.setUsers(null);
				chat.setCreateUser(null);
				chatService.delete(chat);
			}
			result.setSuccess(true);
		}else{
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
		}
		return result;
	}
	
	@RequestMapping("moveFriend")
	@ResponseBody
	private Result moveFriend(@RequestParam String userid, @RequestParam String groupid, @RequestParam String ogroupid){
		Result result = new Result();
		Group ogroup = groupService.get(ogroupid);
		Group group = groupService.get(groupid);
		User user = userService.get(userid);
		
		if(ogroup != null && user != null && group != null){
			if(groupService.haveUser(ogroup, userid))
				groupService.removeUser(ogroup, user);
			if(!groupService.haveUser(group, userid))
				groupService.addUser(group, user);
			result.setSuccess(true);
		}else{
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
		}
		return result;
	}
	
	@RequestMapping("noteName")
	@ResponseBody
	private Result noteName(@RequestParam String userid, @RequestParam String noteName, @RequestParam String _userid){
		Result result = new Result();
		User user = userService.get(userid);
		User my = userService.get(_userid);
		if(user != null && my != null){
			NoteName name = noteNameService.get(my, user);
			if(name == null){
				name = new NoteName();
				name.setBelongs(my);
				name.setCreateDate(new Date());
				name.setTarget(user);
			}
			if(!user.getNoteNames().contains(name)){
				name.setName(noteName);
				user.getNoteNames().add(name);
				userService.update(user);
			}else{
				name.setName(noteName);
				noteNameService.update(name);
			}
			result.getData().put("user", user);
			result.setSuccess(true);
		}else{
			result.setExecuteResult(new ExecuteResult(MyErrorCode.NOT_FOUND));
		}
		return result;
	}
}

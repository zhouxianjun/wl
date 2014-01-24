package com.gary.wl.controller;

import java.io.File;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gary.framework.controller.BaseController;
import com.gary.framework.result.Result;
import com.gary.wl.dto.Config;
import com.gary.wl.entity.Version;
import com.gary.wl.service.VersionService;

@Controller
@RequestMapping("version")
public class VersionController extends BaseController {
	
	@Autowired
	private Config config;
	
	@Autowired
	private VersionService versionService;
	
	@RequestMapping("now")
	@ResponseBody
	public Result getNow(HttpServletRequest request, @RequestParam String type){
		Result result = new Result();
		result.setSuccess(true);
		result.getData().put("now", versionService.now(type));
		return result;
	}
	
	@RequestMapping("version")
	@ResponseBody
	public Result getVersion(HttpServletRequest request, @RequestParam String type){
		Result result = new Result();
		result.setSuccess(true);
		result.getData().put("list", versionService.all(type));
		return result;
	}
	
	@RequestMapping("upload")
	@ResponseBody
	public Result upload(HttpServletRequest request, @RequestParam MultipartFile f, @RequestParam String t, @RequestParam String v, String m) throws Exception{
		Result result = new Result();
		result.setSuccess(true);
		Version version = versionService.findByUniqueCode(t, v);
		if(version == null){
			if(f == null || f.isEmpty() || f.getSize() < 1){
				result.setSuccess(false);
				throw new MissingServletRequestParameterException("f", "File");
			}
			version = new Version();
			if(!StringUtils.isEmpty(m))
				version.setMemo(m);
			version.setType(t);
			version.setVersion(v);
			String originalFilename = f.getOriginalFilename();
			File file = new File(getPath(request, config.getVERSION_SAVE_PATH() + File.separator + t), t + "_" + v.replace(".", "_") + originalFilename.substring(originalFilename.lastIndexOf(".")));
			try {
				if(!file.exists())
					file.mkdirs();
				f.transferTo(file);
			} catch (Exception e) {
				result.setSuccess(false);
				logger.error(e.getMessage(), e);
				return result;
			}
			version.setPath(file.getAbsolutePath());
			Serializable id = versionService.save(version);
			result.setSuccess(id != null);
		}else{
			
		}
		result.getData().put("version", version);
		return result;
	}
	
	private String getPath(HttpServletRequest request, String path){
		return request.getSession().getServletContext().getRealPath(path == null ? "" : path)+File.separator;
	}
}

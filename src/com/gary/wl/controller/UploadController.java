package com.gary.wl.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gary.framework.controller.BaseController;
import com.gary.framework.result.Result;
import com.gary.wl.dto.Config;
import com.gary.wl.entity.FileSHA;
import com.gary.wl.service.FileSHAService;
import com.gary.wl.util.Util;

@RequestMapping("upload")
@Controller
public class UploadController extends BaseController {
	
	@Autowired
	private Config config;
	
	@Autowired
	private FileSHAService fileSHAService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy$MM$dd$$HH$mm$ss@");
	
	@RequestMapping("msgImg")
	@ResponseBody
	private Result msgImg(HttpServletRequest request, @RequestParam MultipartFile[] files, @RequestParam String sha) throws IOException{
		Result result = new Result();
		List<String> list = new ArrayList<String>();
		for (MultipartFile multipartFile : files) {
			if(multipartFile != null && !multipartFile.isEmpty() && multipartFile.getSize() > 0){
				String originalFilename = multipartFile.getOriginalFilename();
				String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
				byte[] data = multipartFile.getBytes();
				if(Util.isImg(suffix)){
					String contentPath = request.getSession().getServletContext().getRealPath(config.getMSG_IMG_PATH())+File.separator;
					String name = sdf.format(new Date()) + UUID.randomUUID().toString().replaceAll("-", "") + suffix;
					File f = new File(contentPath);
					if(!f.exists()){
						f.mkdirs();
					}
					Util.getFile(data, contentPath, name);
					list.add(name);
					FileSHA fileSHA = new FileSHA();
					fileSHA.setPath(name);
					fileSHA.setSha(sha);
					fileSHA.setSize(multipartFile.getSize());
					fileSHA.setDate(new Date());
					fileSHAService.saveOrUpdate(fileSHA);
				}
			}
		}
		result.setSuccess(true);
		result.getData().put("files", list);
		return result;
	}
	
	@RequestMapping("msgFile")
	@ResponseBody
	private Result msgFile(HttpServletRequest request, @RequestParam MultipartFile[] files, @RequestParam String sha) throws IOException{
		Result result = new Result();
		List<String> list = new ArrayList<String>();
		for (MultipartFile multipartFile : files) {
			if(multipartFile != null && !multipartFile.isEmpty() && multipartFile.getSize() > 0){
				String originalFilename = multipartFile.getOriginalFilename();
				String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
				byte[] data = multipartFile.getBytes();
				String contentPath = request.getSession().getServletContext().getRealPath(config.getMSG_FILE_PATH())+File.separator;
				String name = sdf.format(new Date()) + UUID.randomUUID().toString().replaceAll("-", "") + suffix;
				File f = new File(contentPath);
				if(!f.exists()){
					f.mkdirs();
				}
				Util.getFile(data, contentPath, name);
				list.add(name);
				FileSHA fileSHA = new FileSHA();
				fileSHA.setPath(name);
				fileSHA.setSha(sha);
				fileSHA.setSize(multipartFile.getSize());
				fileSHA.setDate(new Date());
				fileSHAService.saveOrUpdate(fileSHA);
			}
		}
		result.setSuccess(true);
		result.getData().put("files", list);
		return result;
	}
	
	@RequestMapping("sha")
	@ResponseBody
	private Result getSHA(HttpServletRequest request, @RequestParam String sha, @RequestParam String path){
		Result result = new Result();
		FileSHA fileSHA = fileSHAService.get(sha);
		result.setSuccess(true);
		if(fileSHA != null){
			String contentPath = request.getSession().getServletContext().getRealPath(path)+File.separator;
			File file = new File(contentPath, fileSHA.getPath());
			if(file.exists()){
				result.getData().put("file", fileSHA);
			}
		}
		return result;
	}
}

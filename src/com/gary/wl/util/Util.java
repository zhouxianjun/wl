package com.gary.wl.util;

import java.awt.image.BufferedImage;
import java.beans.PropertyDescriptor;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.AntPathMatcher;

import com.gary.core.Coder;
import com.gary.core.HttpClient;
import com.gary.core.util.Utils;
import com.gary.wl.dto.IPDtoExt;

public class Util {
	public static IPDtoExt getIPInfo(String ip) {
		IPDtoExt dto = null;
		try {
			HttpClient http = Utils.getInstance(HttpClient.class);
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("ip", ip);
			/*String ipdesc = http.getMethod(
					"http://int.dpool.sina.com.cn/iplookup/iplookup.php", p).get(
					String.class);*/
			String ipdesc = http.getMethod(
					"http://ip.taobao.com/service/getIpInfo.php", p).get(
					String.class);
			ipdesc = Coder.decodeUnicode(ipdesc);
			JSONObject json = JSONObject.fromObject(ipdesc).getJSONObject("data");
			dto = new IPDtoExt();
			dto.setIp(ip);
			dto.setCountry(json.getString("country"));
			dto.setProvince(json.getString("region"));
			dto.setCity(json.getString("city"));
			dto.setIsp(json.getString("isp"));
			dto.setCitySN(json.getString("city_id"));
			dto.setCountrySN(json.getString("country_id"));
			dto.setProvinceSN(json.getString("region_id"));
		} catch (Exception e) {
		}
		
		return dto;
	}

	/**
	 * 获取访问者IP
	 * 
	 * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	 * 
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
	 * 如果还不存在则调用Request .getRemoteAddr()。
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return request.getRemoteAddr();
		}
	}

	public static String getLocalIp() {
		String myip = null;
		try {
			myip = InetAddress.getLocalHost().getHostAddress();

		} catch (UnknownHostException e) {
		}
		return myip;
	}
	
	public static void copyOrigNotNullPropertyToDestBean(Object result, Object orig){  
		// Validate existence of the specified beans  
		if (result == null && orig != null) {  
		    return;
		}  
	
		PropertyDescriptor[] pd = PropertyUtils.getPropertyDescriptors(orig);
		for (PropertyDescriptor propertyDescriptor : pd) {
			try {
				Method m = propertyDescriptor.getReadMethod();
				Transient t = m.getAnnotation(Transient.class);
				if(m.getModifiers() == 1 && !m.getReturnType().equals(Set.class) && !m.getReturnType().equals(List.class) && t == null){
					Object invoke = m.invoke(orig);
					if(invoke != null){
						String name = propertyDescriptor.getName();
						BeanUtils.copyProperty(result, name, invoke);
					}
				}
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean isImg(String suffix){
		if(".jpg".equalsIgnoreCase(suffix)){
			return true;
		}else if(".gif".equalsIgnoreCase(suffix)){
			return true;
		}else if(".png".equalsIgnoreCase(suffix)){
			return true;
		}else if(".jpeg".equalsIgnoreCase(suffix)){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean imgSize(byte[] in, int maxw, int maxh){
		BufferedImage src;
		try {
			src = ImageIO.read(new ByteArrayInputStream(in));// 读入文件
			int width = src.getWidth(); // 得到源图宽
			int height = src.getHeight(); // 得到源图长
			return width <= maxw && height <= maxh;
		} catch (IOException e) {
			return false;
		} 
	}
	
	public static void getFile(byte[] bfile, String filePath,String fileName) {  
        BufferedOutputStream bos = null;  
        FileOutputStream fos = null;  
        File file = null;  
        try {  
            File dir = new File(filePath);  
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在  
                dir.mkdirs();  
            }  
            file = new File(filePath, fileName);  
            fos = new FileOutputStream(file);  
            bos = new BufferedOutputStream(fos);  
            bos.write(bfile);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (bos != null) {  
                try {  
                    bos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
            if (fos != null) {  
                try {  
                    fos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
        }  
    }  
	
	public static void main(String[] args) {
		/*UserExt ext = new UserExt();
		ext.setBirthday("10-10");
		ext.setFace("face");
		
		UserExt ext2 = new UserExt();
		ext2.setBirthday("10-12");
		ext2.setFace("face");
		ext2.setMobile("15019418143");
		
		copyOrigNotNullPropertyToDestBean(ext2, ext);
		
		System.out.println(JSONObject.fromObject(ext2));
*/		
		
		AntPathMatcher antPathMatcher = new AntPathMatcher();
		System.out.println(antPathMatcher.match("/**/onlines.do*", "/wl/main/onlines.do"));
	}
}

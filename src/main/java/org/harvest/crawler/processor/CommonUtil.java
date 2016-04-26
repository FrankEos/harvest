package org.harvest.crawler.processor;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.harvest.crawler.util.StringUtil;

public class CommonUtil {

	private static final Logger logger = Logger.getLogger(CommonUtil.class);
	
	

	
	private Map<String, Integer> en_websites = null;
	private Map<String, Integer> cn_websites = null;
	private Map<String, String> areas = null;
	private Map<String, String> tags1 = null;
	private Map<String, String> tags2 = null;
	private Map<String, String> tags3 = null;
	private Map<String, String> tags4 = null;
	private Map<String, String> tags5 = null;
	private Map<String, String> tags6 = null;
	
	
	public static CommonUtil mInstance;

	
	/**
	 * 获取实例
	 * @return
	 */
	public static CommonUtil getInstance() {
		if (mInstance == null) {
			mInstance = new CommonUtil();
		}
		return mInstance;
	}
	
	private CommonUtil(){

		init();
	}
	
	private void init(){

		cn_websites = new HashMap<String, Integer>();
		en_websites = new HashMap<String, Integer>();
//		List<Map<String, Object>> webSiteList = mContentDbUtil.selectWebSite();
//		for(Map<String, Object> map:webSiteList){
//			if(!StringUtil.isEmpty(map.get("hms_pgm_website_name"))){
//				cn_websites.put(map.get("hms_pgm_website_name").toString(), (Integer)map.get("hms_pgm_website_id"));
//			}
//			if(!StringUtil.isEmpty(map.get("hms_pgm_website_code"))){
//				String websiteCode = map.get("hms_pgm_website_code").toString().toLowerCase();
//				en_websites.put(websiteCode, (Integer)map.get("hms_pgm_website_id"));
//			}
//		}

//		areas = new HashMap<String, String>();
//		List<Map<String, Object>> areasList = mContentDbUtil.selectAreas();
//		for(Map<String, Object> map:areasList){
//			areas.put(map.get("hms_pgm_name").toString(),map.get("hms_pgm_id").toString());
//		}


		tags1 = new HashMap<String, String>();
		initTags(tags1,1);
		tags2 = new HashMap<String, String>();
		initTags(tags2,2);
		tags3 = new HashMap<String, String>();
		initTags(tags3,3);
		tags4 = new HashMap<String, String>();
		initTags(tags4,4);
		tags5 = new HashMap<String, String>();
		initTags(tags5,5);
		tags6 = new HashMap<String, String>();
		initTags(tags6,6);
		
	}
	
	
	private void initTags(Map<String, String> tag,Integer i){
//		List<Map<String, Object>> tagsList = mContentDbUtil.selectTags(i);
//		for(Map<String, Object> map:tagsList){
//			tag.put(map.get("hms_pgm_type_name").toString(),map.get("hms_pgm_type_code").toString());
//		}
	}
	
	

	/**
	 * 获取网站ID
	 * @return
	 */
	public String getWebsiteIds(String websiteNames) {
		StringBuilder result = new StringBuilder();
		String[] tmpWebsiteNames = websiteNames.split(",");
		for (String tmpWebsiteName : tmpWebsiteNames) {
			if(tmpWebsiteName.equals("腾讯视频")){
				tmpWebsiteName = "腾讯";
			}
			Integer tmpWebsiteId = -1;
			if (cn_websites.containsKey(tmpWebsiteName)) {
				tmpWebsiteId = cn_websites.get(tmpWebsiteName);
			} else if (en_websites.containsKey(tmpWebsiteName.toLowerCase())) {
				tmpWebsiteId = en_websites.get(tmpWebsiteName);
			}
			if (result.length() > 0) {
				result.append(",");
			}
			result.append(tmpWebsiteId);
		}
		return result.toString();
	}

	/**
	 * 获取地区
	 * @return
	 */
	public String getArea(String area) {
		String result = "-1";
		
		
		if(area.equals("中国大陆")||area.equals("中国内地")||area.equals("中国")||area.equals("内地")){
			area = "大陆";
		}
		if(area.equals("香港")||area.equals("台湾")){
			area = "港台";
		}
		if (areas.containsKey(area)) {
			return areas.get(area);
		}
		return result;
	}


	/**
	 * 获取评分
	 * @return
	 */
	public String getHot(String hot) {
		String result = "0";
		try {
			if(!StringUtil.isEmpty(hot)){
				Double tmp = Double.parseDouble(hot) * 10;
				return String.valueOf(tmp);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.error("Double.parseDouble error! hot : "+hot);
		}
		return result;
	}
	

	/**
	 * 获取分类
	 * @return
	 */
	public String getPgmTag(List<String> pgmTag,Integer channel_id) {
		Map<String, String> tags = getTags(channel_id);
		StringBuffer result = new StringBuffer();
		for (String tag : pgmTag) {
			if (!tags.containsKey(tag)){
				continue;
			}
			if (result.length() > 0) {
				result.append(" ");
			}
			result.append(tags.get(tag));
		}
		if(result.toString().equals("")){
			return "other";
		}else{
			return result.toString();
		}
	}

	/**
	 * 获取分类
	 * @return
	 */
	public String getPgmTag(String pgmTags,Integer channel_id) {
		Map<String, String> tags = getTags(channel_id);
		StringBuffer result = new StringBuffer();
		String[] pgmTag = pgmTags.split(" ");
		for (String tag : pgmTag) {
			if (!tags.containsKey(tag)){
				continue;
			}
			if (result.length() > 0) {
				result.append(" ");
			}
			result.append(tags.get(tag));
		}
		if(result.toString().equals("")){
			return "other";
		}else{
			return result.toString();
		}
	}
	
	
	private Map<String, String> getTags(Integer channel_id){
		
		switch (channel_id) {
			case 1:
				return tags1;
			case 2:
				return tags2;
			case 3:
				return tags3;
			case 4:
				return tags4;
			case 5:
				return tags5;
			case 6:
				return tags6;
	
			default:
				break;
		}
		return null;
	}
	

	/**
	 * 获取质量
	 * @return
	 */
	public Integer getQuality(Integer quality) {
		Integer result = 202;
		switch (quality) {
		case 1:
			return 202;
		case 2:
			return 203;
		case 3:
			return 204;
		case 4:
			return 205;
		}
		return result;
	}
	

	/**
	 * 获取Json内容
	 * @return
	 */
	public String getJsonContentFromUrl(String nextUrl) {
		InputStream is = null;
		ByteArrayOutputStream os = null;
		HttpURLConnection openConnection = null;
		try {
//			openConnection = (HttpURLConnection) new URL(nextUrl).openConnection();
//			openConnection.setConnectTimeout(60 * 1000);
//			openConnection.setReadTimeout(60 * 1000);
//			is = openConnection.getInputStream();
//			os = new ByteArrayOutputStream();
//			byte[] b = new byte[1024 * 1024 * 3];
//			int i = -1;
//			while ((i = is.read(b)) != -1) {
//				os.write(b, 0, i);
//			}
//			return os.toString("UTF-8");
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.error("Connection error! url : "+nextUrl);
			return "";
		} finally {
			if (is != null){
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (os != null){
				try {
					os.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (os != null){
				try {
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (openConnection != null) {
				openConnection.disconnect();
			}
		}
	}
	

	/**
	 * 获取Json内容
	 * @return
	 */
	public String getJsonContentFromUrl(String url, String param) {
        PrintWriter out = null;
		InputStream is = null;
		ByteArrayOutputStream os = null;
		HttpURLConnection openConnection = null;
		try {
			openConnection = (HttpURLConnection) new URL(url).openConnection();
			openConnection.setConnectTimeout(60 * 1000);
			openConnection.setReadTimeout(60 * 1000);
			openConnection.setRequestProperty("accept", "*/*");// 设置通用的请求属性
			openConnection.setRequestProperty("connection", "Keep-Alive");
			openConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

	        // 发送POST请求必须设置如下两行
			openConnection.setDoOutput(true);
			openConnection.setDoInput(true);
	        out = new PrintWriter(openConnection.getOutputStream());// 获取URLConnection对象对应的输出流
	        // 发送请求参数
	        out.print(param);
	        out.flush();// flush输出流的缓冲
			is = openConnection.getInputStream();
			os = new ByteArrayOutputStream();
			byte[] b = new byte[1024 * 1024 * 3];
			int i = -1;
			while ((i = is.read(b)) != -1) {
				os.write(b, 0, i);
			}
			return os.toString("UTF-8");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.error("Connection error! url : "+url);
			return "";
		} finally {
			if (is != null){
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (os != null){
				try {
					os.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (os != null){
				try {
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (openConnection != null) {
				openConnection.disconnect();
			}
		}
	}

	/**
	 * 获取List
	 * @return
	 */
	public String list2String(List<String> list) {
		return list2String(list, " ");
	}

	/**
	 * 获取字符串
	 * @return
	 */
	public String list2String(List<String> list, String separator) {
		if (list == null || list.size() == 0){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String s : list) {
			if (sb.length() > 0){
				sb.append(separator);
			}
			sb.append(s);
		}
		return sb.toString();
	}
}

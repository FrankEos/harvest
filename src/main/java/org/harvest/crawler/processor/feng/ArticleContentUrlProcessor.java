package org.harvest.crawler.processor.feng;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.harvest.crawler.dao.HarvestDBManager;
import org.harvest.crawler.framework.BaseProcessor;
import org.harvest.crawler.util.StringUtil;
import org.harvest.crawler.util.Util;
import org.harvest.crawler.util.XmlUtil;
import org.harvest.web.bean.UrlContent;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.exception.HttpException;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;
import org.xml.sax.InputSource;

public class ArticleContentUrlProcessor extends BaseProcessor {

	private static final String WORK_DIR = "~/harvest/feng/";

	private static final String TOP_DIR = "/home/spider/spiderdownload/aguo";

	private static final String HOST_URL = "http://down.aguo.com/d2";

	HarvestDBManager mDBManager = HarvestDBManager.getInstance();

	HashMap<String, String> mArticleMap = new HashMap<String, String>();

	HashMap<String, String> mDistinctMap = new HashMap<String, String>();

	private String mText = "";

	static HashMap<String, Integer> categoryMap = new HashMap<String, Integer>();

	static {

		categoryMap.put("角色扮演", 36);
		categoryMap.put("动作游戏", 37);
		categoryMap.put("格斗游戏", 38);
		categoryMap.put("体育竞技", 39);
		categoryMap.put("桌面棋牌", 40);
		categoryMap.put("策略战棋", 41);
		categoryMap.put("飞行游戏", 42);
		categoryMap.put("即时战略", 43);
		categoryMap.put("恋爱养成", 44);
		categoryMap.put("赛车竞速", 45);
		categoryMap.put("其他游戏", 46);
		categoryMap.put("冒险游戏", 47);
		categoryMap.put("第三人称射击", 48);
		categoryMap.put("第一人称射击", 49);
		categoryMap.put("休闲益智", 50);
		categoryMap.put("模拟经营", 51);
		categoryMap.put("动作射击", 52);
	}

	@Override
	protected void extractContentUrls(UrlContent curi) {
		super.extractContentUrls(curi);
		Document dom = XmlUtil.parseXml(this.getRulefile(), true);
		String strXml = XmlUtil.dom2str(dom);// 将dom转换成String

		ScraperConfiguration config = new ScraperConfiguration(new InputSource(new StringReader(strXml)));
		Scraper scraper = new Scraper(config, WORK_DIR);

		try {
			String url = curi.getUrl();
			scraper.addVariableToContext("pageUrl", new String(url));
			scraper.addVariableToContext("xmlcontent", new String(""));
			scraper.getHttpClientManager().getHttpClient().getParams().setConnectionManagerTimeout(16 * 1000);
			scraper.getHttpClientManager().getHttpClient().getHttpConnectionManager().getParams()
					.setSoTimeout(16 * 1000);
			scraper.getHttpClientManager().getHttpClient().getHttpConnectionManager().getParams()
					.setConnectionTimeout(16 * 1000);
			scraper.setDebug(true);
			scraper.execute();

			Variable xmlcontent = (Variable) scraper.getContext().getVar("xmlcontent");
			Variable textVar = (Variable) scraper.getContext().getVar("text");
			String content = xmlcontent.toString();
			mText = textVar.toString();
			// 将提取到的xml内容做MD5运算

			if (!StringUtil.isEmpty(content) || !StringUtil.isEmpty(mText)) {
				ValidateContentAccord(content);
				if (!this.isContentAccord) {
					// 解析content xml，并将数据写入数据库
					if (!content2database(curi, content)) {
						curi.setOper_flag(3);// 非正常下载
					}
				}
			} else {
				curi.setOper_flag(3);// 非正常下载
			}

		} catch (HttpException e) {
			logger.error("URL地址请求超时：" + e.getMessage());
			curi.setOper_flag(3);// 非正常下载
		} catch (IllegalArgumentException e) {
			logger.error("无效的URL地址：" + e.getMessage());
			curi.setOper_flag(3);// 非正常下载
		} catch (Exception e) {
			logger.error("在解析网页  " + curi.getUrl() + " 时发生错误 ：" + e.getMessage());
			curi.setOper_flag(3);// 非正常下载
		} finally {
			// 释放内存资源
			scraper.dispose();
		}
	}

	@Override
	protected boolean content2database(UrlContent curi, String content) {
		Document dom = XmlUtil.str2dom(content);
		if (dom != null) {
			Element root = dom.getRootElement();

			mArticleMap.clear();
			String title = null;
			String smalltext = null;
			String images = null;
			String game = null;
			String titlepic = null;
			String newstext = null;

			{
				smalltext = mText.trim();
				if (StringUtil.isEmpty(smalltext)) {
					return false;
				}

				smalltext = smalltext.replace(
						"<div class=\"articalDetail\" id=\"article_mobile_content\" style=\"display:none;\">", "");
				smalltext = smalltext.replace("</div>", "");
				smalltext = smalltext.trim();
			}

			for (Iterator iter = root.elementIterator(); iter.hasNext();) {
				Element element = (Element) iter.next();
				if ("title".equalsIgnoreCase(element.getName())) {
					title = element.getTextTrim();

					if (StringUtil.isEmpty(title)) {
						return false;
					}

				} else if ("images".equalsIgnoreCase(element.getName())) {
					images = element.getTextTrim();
				}

			}

			int start = title.lastIndexOf('《');
			int end = title.lastIndexOf('》');
			if (start >= 0 && end >= 0) {
				game = title.substring(start + 1, end);
			} else {
				game = title;
			}
			if(StringUtil.isEmpty(titlepic) && StringUtil.isEmpty(images)) {
				return false;
			}
			titlepic = StringUtil.isEmpty(images) ? "" : images.split(" ")[0];

			// newstext有日期+md5(text)
			newstext = getNewstext(smalltext);

			logger.debug(String.format("title:%s, titlepic:%s, smalltext:%s, images:%s game:%s newstext:%s\n", title,
					titlepic, smalltext, images, game, newstext));

			mArticleMap.put("classid", "46");
			mArticleMap.put("title", title);
			mArticleMap.put("titlepic", titlepic);
			mArticleMap.put("smalltext", smalltext);
			mArticleMap.put("newstext", newstext);
			mArticleMap.put("game", game);
			mArticleMap.put("smalltext_md5", Util.getMD5(smalltext));

			if (mDBManager.isArticleExist(mArticleMap)) {
				return false;
			}

			// 第一步：插入数据库
			// 第二步：通过newstext获取刚刚插入的id
			// 第三步：下载文章中的图片
			// 第四步：将下载成功的本地图片链接替换原来文章中的图片链接
			mDBManager.insertArticle(mArticleMap);
			int id = mDBManager.getArticleId(mArticleMap);

			mArticleMap.put("id", String.valueOf(id));
			// now process pictures task
			if (doDownloadImages(mArticleMap, images)) {
				logger.info("Download sucess: " + title);
				return true;
			}
			mArticleMap.put("id", String.valueOf(id));

			if (mDBManager.isArticleIdExist(mArticleMap)) {
				mDBManager.deleteArticleById(mArticleMap);
			}
		}

		return false;
	}

	private String getNewstext(String text) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MMdd");
		String today = format.format(date);
		String md5 = Util.getMD5(text);
		return String.format("%s/%s", today, md5);
	}

	private boolean writeText(String text, String newstext) {
		FileOutputStream fop = null;
		File file;
		StringBuilder sb = new StringBuilder();
		sb.append("<? exit();?>");
		sb.append("<p>");
		sb.append(text);
		sb.append("</p>");
		text = sb.toString();
		text = text.replace("\"", "\\\"");

		try {
			String path = TOP_DIR + File.separatorChar + "txt" + File.separatorChar + newstext + ".php";
			logger.debug("text save path:" + path);
			file = new File(path);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}

			fop = new FileOutputStream(file);

			// get the content in bytes
			byte[] contentInBytes = text.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	private boolean doDownloadImages(HashMap<String, String> map, String rawUrl) {
		if (!mDBManager.isArticleIdExist(map) || StringUtil.isEmpty(rawUrl)) {
			return false;
		}

		String id = map.get("id");
		String classid = map.get("classid");
		String smalltext = map.get("smalltext");
		String newstext = map.get("newstext");

		String name = classid + "_" + id;
		String urls[] = rawUrl.split(" ");
		int index = 0;

		for (String url : urls) {
			mDistinctMap.clear();
			String suffix = url.contains(".jpg") ? "jpg" : "png";
			String fileName = String.format("%s_%d.%s", name, index, suffix);
			String absolutePath = compileFilePath(fileName, "pic");

			String finalPath = url;
			if (download(url, absolutePath)) {
				finalPath = absolutePath.replace(TOP_DIR, HOST_URL);
				File file = new File(absolutePath);
				String fileMd5 = Util.getFileMD5String(file);
				mDistinctMap.put("file_md5", fileMd5);
				if (mDBManager.isImgExist(mDistinctMap)) {
					file.deleteOnExit();
					finalPath = mDBManager.getImgFilePath(mDistinctMap).replace(TOP_DIR, HOST_URL);
				} else {
					mDistinctMap.put("file_abs_path", absolutePath);
					mDistinctMap.put("insert_time", String.valueOf(System.currentTimeMillis() / 1000));
					mDistinctMap.put("file_path", finalPath.replace(HOST_URL, ""));
					mDBManager.insertImgMd5(mDistinctMap);
				}
			} else {
				if (download(url, absolutePath)) {
					finalPath = absolutePath.replace(TOP_DIR, HOST_URL);
					File file = new File(absolutePath);
					String fileMd5 = Util.getFileMD5String(file);
					mDistinctMap.put("file_md5", fileMd5);
					if (mDBManager.isImgExist(mDistinctMap)) {
						file.deleteOnExit();
						finalPath = mDBManager.getImgFilePath(mDistinctMap).replace(TOP_DIR, HOST_URL);
					} else {
						mDistinctMap.put("file_abs_path", absolutePath);
						mDistinctMap.put("insert_time", String.valueOf(System.currentTimeMillis() / 1000));
						mDistinctMap.put("file_path", finalPath.replace(HOST_URL, ""));
						mDBManager.insertImgMd5(mDistinctMap);
					}
				} else {
					mDBManager.deleteArticleById(map);
					return false;
				}
			}

			if (index == 0) {
				map.put("titlepic", finalPath);
				mDBManager.updateArticleTitlePic(map);
			}
			// 用下载成功的图片地址去更新smalltext中的外部图片链接
			smalltext = smalltext.replace(url, finalPath);

			index++;
		}

		map.put("smalltext", smalltext);
		mDBManager.updateArticleContent(map);

		return writeText(smalltext, newstext) ? true : false;
	}

	private String compileFilePath(String fileName, String type) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String today[] = format.format(date).split("-");
		return TOP_DIR + File.separatorChar + "article" + File.separatorChar + type + File.separatorChar + today[0]
				+ File.separatorChar + today[1] + File.separatorChar + today[2] + File.separatorChar + fileName;
	}

	private boolean download(String url, String path) {
		logger.debug("path:" + path + ", url:" + url);
		if (StringUtil.isEmpty(url) || StringUtil.isEmpty(path)) {
			return false;
		}

		HttpClient client = null;
		try {

			client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = client.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				byte[] result = EntityUtils.toByteArray(response.getEntity());
				BufferedOutputStream bw = null;
				File f = new File(path);
				if (!f.getParentFile().exists())
					f.getParentFile().mkdirs();
				bw = new BufferedOutputStream(new FileOutputStream(path));
				bw.write(result);
				if (bw != null)
					bw.close();
			} else {
				if (client != null) {
					client.getConnectionManager().shutdown();
				}
				return false;
			}

		} catch (Exception e) {
			new File(path).deleteOnExit();
			logger.error(e.getMessage());
			logger.error("!!!!!!!!!!!!!!!!!! download file failed:" + url);
			return false;
		} finally {
			if (client != null) {
				client.getConnectionManager().shutdown();
			}
		}

		return true;
	}
}

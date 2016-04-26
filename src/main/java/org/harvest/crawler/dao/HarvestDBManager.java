
package org.harvest.crawler.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.harvest.crawler.util.StringUtil;

public class HarvestDBManager {

	public static final Logger logger = Logger.getLogger(HarvestDBManager.class);

	public static final int PLATFORM_ANDROID = 1;

	public static final int PLATFORM_IPHONE = 2;

	public static final int PLATFORM_IPAD = 3;

	private static HarvestDBManager mInstance;

	private SqlSessionFactory sqlSessionFactory;

	private static final String NS = "org.harvest.crawler.dao.HarvestDBManager.";

	private HarvestDBManager() {
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader("harvest_mybatis_config.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			reader = null;
		}
	}

	public static HarvestDBManager getInstance() {
		if (mInstance == null) {
			mInstance = new HarvestDBManager();
		}
		return mInstance;
	}

	/**
	 * 向aguo database表中插入一条anroid游戏或应用 第一步：将详细信息插入aguo_ecms_android表
	 * 第二步：通过map中的title_version_md5获取第一步插入记录的id
	 * 第三步：通过获取到的id，调用updateExtra更新aguo_ecms_android表filename/titleurl字段的值
	 * 第四步：将上面的id/classid插入到aguo_ecms_android_index表
	 * 第五步：将上面的id/classid插入到aguo_ecms_android_data_1表
	 * 
	 * 说明：第四步，第五步不能省略，不然admin.aguo.com在后台点击修改时无法找到数据
	 * 
	 * @param map
	 * @return
	 */
	public int insertApp(HashMap<String, String> map) {
		map.put("newstime", String.valueOf(System.currentTimeMillis() / 1000));
		map.put("truetime", String.valueOf(System.currentTimeMillis() / 1000 + 600));
		map.put("language", "中文");
		insert("insertApp", map);
		int id = getInsertId(map);
		map.put("id", String.valueOf(id));
		map.put("filename", String.valueOf(id));

		if ("29".equals(map.get("classid"))) {
			map.put("titleurl", String.format("/Andriod/soft/%d.html", id));
		} else if ("21".equals(map.get("classid"))) {
			map.put("titleurl", String.format("/Andriod/game/%d.html", id));
		}

		updateExtra(map);

		{
			insert("insertAppToIndex", map);
			insert("insertAppToData1", map);
		}

		return id;
	}

	/**
	 * 更新aguo_ecms_andriod表中的filename, titleurl字段
	 * 
	 * @param map
	 */
	public void updateExtra(HashMap<String, String> map) {
		update("updateExtra", map);
	}

	/**
	 * 更新aguo_ecms_andriod表中的titlepic字段，下载完外站的icon图片后，用本地url更新该字段
	 * 
	 * @param map
	 */
	public void updateTitlePic(HashMap<String, String> map) {
		update("updateTitlePic", map);
	}

	/**
	 * 更新aguo_ecms_andriod表中的downpath字段，下载完外站的apk文件后，用本地url更新该字段
	 * 
	 * @param map
	 */
	public void updateDownPath(HashMap<String, String> map) {
		update("updateDownPath", map);
	}

	/**
	 * 更新aguo_ecms_andriod表中的morepic字段，下载完外站的应用截图后，用本地url更新该字段
	 * 
	 * @param map
	 */
	public void updateMorePic(HashMap<String, String> map) {
		update("updateMorePic", map);
	}

	/**
	 * 通过md5(title + ver)去数据库取md5，如果取到，说明改应用已经存在于数据库中
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean isAppExist(Map<String, String> map) {
		List list = selectList("selectMd5", map);
		return list.size() > 0 ? true : false;
	}

	/**
	 * 通过title_version_md5获取插入记录的对应id
	 * 
	 * @param map
	 * @return
	 */
	public int getInsertId(Map<String, String> map) {
		return selectId("selectIdbyMd5", map);
	}

	/**
	 * 通过id获取对应的classid
	 * 
	 * @param map
	 * @return
	 */
	public int getClassId(Map<String, String> map) {
		return selectId("selectClassId", map);
	}

	/**
	 * 通过id判断该id是否存在于aguo_ecms_android表中
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean isIdExist(Map<String, String> map) {
		List list = selectList("selectId", map);
		return list.size() > 0 ? true : false;
	}

	/**
	 * 根据Id同时删除aguo_ecms_android/aguo_ecms_android_data_1/
	 * aguo_ecms_android_index里面的记录
	 * 
	 * @param param
	 * @return
	 */
	public Integer deleteById(HashMap<String, String> param) {
		delete("deleteData1ById", param);
		delete("deleteIndexById", param);
		return delete("deleteById", param);
	}

	/**
	 * 将图片MD5插入spider_img_md5表里面
	 * 
	 * @param param
	 */
	public void insertImgMd5(HashMap<String, String> param) {
		insert("insertImgMd5", param);
	}

	/**
	 * 将Apk MD5插入spider_apk_md5表里面
	 * 
	 * @param param
	 */
	public void insertApkMd5(HashMap<String, String> param) {
		insert("insertApkMd5", param);
	}

	/**
	 * 根据图片的md5判断图片是否已经下载过
	 * 
	 * @param param
	 * @return
	 */
	public boolean isImgExist(HashMap<String, String> param) {
		return !StringUtil.isEmpty(selectOne("selectImgMd5", param));
	}

	/**
	 * 根据apk的md5判断图片是否已经下载过
	 * 
	 * @param param
	 * @return
	 */
	public boolean isApkExist(HashMap<String, String> param) {
		return !StringUtil.isEmpty(selectOne("selectApkMd5", param));
	}

	/**
	 * 根据图片的md5从spider_img_md5表中取出下载的绝对路径
	 * 
	 * @param param
	 * @return
	 */
	public String getImgFilePath(HashMap<String, String> param) {
		return selectOne("selectImgFilePath", param);
	}

	/**
	 * 根据apk的md5从spider_apk_md5表中取出下载的绝对路径
	 * 
	 * @param param
	 * @return
	 */
	public String getApkFilePath(HashMap<String, String> param) {
		return selectOne("selectApkFilePath", param);
	}

	public String getImgUrl(HashMap<String, String> param) {
		return selectOne("selectImgUrl", param);
	}

	public String getApkUrl(HashMap<String, String> param) {
		return selectOne("selectApkUrl", param);
	}

	// Article tables interface Begin
	public void insertArticle(HashMap<String, String> map) {
		map.put("newstime", String.valueOf(System.currentTimeMillis() / 1000));
		map.put("truetime", String.valueOf(System.currentTimeMillis() / 1000 + 600));
		insert("insertArticle", map);
		int id = getArticleId(map);
		map.put("id", String.valueOf(id));
		map.put("filename", String.valueOf(id));

		int classid = 46;
		String titleurl = String.format("/gonglue/qtyx/%d.html", id);
		try {
			classid = Integer.parseInt(map.get("classid"));
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
		}
		switch (classid) {
		case 36:
			titleurl = String.format("/gonglue/jsby/%d.html", id);
			break;
		case 37:
			titleurl = String.format("/gonglue/dzyx/%d.html", id);
			break;
		case 38:
			titleurl = String.format("/gonglue/gdyx/%d.html", id);
			break;
		case 39:
			titleurl = String.format("/gonglue/tyjj/%d.html", id);
			break;
		case 40:
			titleurl = String.format("/gonglue/zmqp/%d.html", id);
			break;
		case 41:
			titleurl = String.format("/gonglue/clzq/%d.html", id);
			break;
		case 42:
			titleurl = String.format("/gonglue/fxyx/%d.html", id);
			break;
		case 43:
			titleurl = String.format("/gonglue/jszl/%d.html", id);
			break;
		case 44:
			titleurl = String.format("/gonglue/layc/%d.html", id);
			break;
		case 45:
			titleurl = String.format("/gonglue/scjs/%d.html", id);
			break;
		case 46:
			titleurl = String.format("/gonglue/qtyx/%d.html", id);
			break;
		case 47:
			titleurl = String.format("/gonglue/mxyx/%d.html", id);
			break;
		case 48:
			titleurl = String.format("/gonglue/dsrc/%d.html", id);
			break;
		case 49:
			titleurl = String.format("/gonglue/dyrc/%d.html", id);
			break;
		case 50:
			titleurl = String.format("/gonglue/xxyz/%d.html", id);
			break;
		case 51:
			titleurl = String.format("/gonglue/mnjy/%d.html", id);
			break;
		case 52:
			titleurl = String.format("/gonglue/dzsj/%d.html", id);
			break;

		default:
			break;
		}

		map.put("titleurl", titleurl);

		updateArticleExtra(map);

		{
			insert("insertArticleToIndex", map);
			insert("insertArticleToData1", map);
		}
	}

	public int getArticleId(HashMap<String, String> map) {
		return selectId("selectArticleId", map);
	}

	public void updateArticleExtra(HashMap<String, String> map) {
		update("updateArticleExtra", map);
	}

	public void updateArticleTitlePic(HashMap<String, String> map) {
		update("updateArticleTitlePic", map);
	}

	public void updateArticleContent(HashMap<String, String> map) {
		update("updateArticleContent", map);
	}

	public Integer deleteArticleById(HashMap<String, String> param) {
		return delete("deleteArticleById", param);
	}

	@SuppressWarnings("rawtypes")
	public boolean isArticleExist(Map<String, String> map) {
		List list = selectList("selectArticle", map);
		return list.size() > 0 ? true : false;
	}

	@SuppressWarnings("rawtypes")
	public boolean isArticleIdExist(Map<String, String> map) {
		List list = selectList("selectArticleById", map);
		return list.size() > 0 ? true : false;
	}
	// Article tables interface End

	// Edu tables interface Begin
	public void insertEdu(HashMap<String, String> map) {
		map.put("newstime", String.valueOf(System.currentTimeMillis() / 1000));
		map.put("truetime", String.valueOf(System.currentTimeMillis() / 1600));
		int classid = 56;
		String type = "安卓教程";

		try {
			classid = Integer.parseInt(map.get("classid"));
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
		}

		switch (classid) {
		case 56:
			type = "安卓教程";
			break;
		case 57:
			type = "iPhone教程";
			break;
		case 58:
			type = "iPad教程";
			break;
		case 59:
			type = "软件教程";
			break;
		default:
			break;
		}
		map.put("type", type);

		insert("insertEdu", map);

		int id = getEduId(map);
		map.put("id", String.valueOf(id));
		map.put("filename", String.valueOf(id));

		String titleurl = String.format("/edu/android/%d.html", id);
		switch (classid) {
		case 56:
			titleurl = String.format("/edu/android/%d.html", id);
			break;
		case 57:
			titleurl = String.format("/edu/iphone/%d.html", id);
			break;
		case 58:
			titleurl = String.format("/edu/ipad/%d.html", id);
			break;
		case 59:
			titleurl = String.format("/edu/soft/%d.html", id);
			break;
		default:
			break;
		}

		map.put("titleurl", titleurl);

		updateEduExtra(map);

		{
			insert("insertEduToIndex", map);
			insert("insertEduToData1", map);
		}
	}

	public int getEduId(HashMap<String, String> map) {
		return selectId("selectEduId", map);
	}

	public void updateEduExtra(HashMap<String, String> map) {
		update("updateEduExtra", map);
	}

	public void updateEduTitlePic(HashMap<String, String> map) {
		update("updateEduTitlePic", map);
	}

	public void updateEduContent(HashMap<String, String> map) {
		update("updateEduContent", map);
		update("updateEduData1Newstext", map);
	}

	public Integer deleteEduById(HashMap<String, String> param) {
		return delete("deleteEduById", param);
	}

	@SuppressWarnings("rawtypes")
	public boolean isEduExist(Map<String, String> map) {
		List list = selectList("selectEdu", map);
		return list.size() > 0 ? true : false;
	}

	@SuppressWarnings("rawtypes")
	public boolean isEduIdExist(Map<String, String> map) {
		List list = selectList("selectEduById", map);
		return list.size() > 0 ? true : false;
	}
	// Edu tables interface End

	// android/iphone/ipad tables interface Begin
	public int insertApp(HashMap<String, String> map, int platform) {
		map.put("newstime", String.valueOf(System.currentTimeMillis() / 1000));
		map.put("truetime", String.valueOf(System.currentTimeMillis() / 1000 + 600));
		map.put("language", "中文");
		int id = -1;
		switch (platform) {
		case PLATFORM_ANDROID: {
			insert("insertApp", map);
			id = getInsertId(map, platform);
			map.put("id", String.valueOf(id));
			map.put("filename", String.valueOf(id));

			if ("29".equals(map.get("classid"))) {
				map.put("titleurl", String.format("/Andriod/soft/%d.html", id));
			} else if ("21".equals(map.get("classid"))) {
				map.put("titleurl", String.format("/Andriod/game/%d.html", id));
			}

			updateExtra(map, platform);
			{
				insert("insertAppToIndex", map);
				insert("insertAppToData1", map);
			}
			break;
		}
		case PLATFORM_IPHONE: {
			insert("insertAppiPhone", map);
			id = getInsertId(map, platform);
			map.put("id", String.valueOf(id));
			map.put("filename", String.valueOf(id));

			if ("30".equals(map.get("classid"))) {
				map.put("titleurl", String.format("/iPhone/soft/%d.html", id));
			} else if ("22".equals(map.get("classid"))) {
				map.put("titleurl", String.format("/iPhone/game/%d.html", id));
			}

			updateExtra(map, platform);
			{
				insert("insertAppToIndexiPhone", map);
				insert("insertAppToData1iPhone", map);
			}
			break;
		}
		case PLATFORM_IPAD: {
			insert("insertAppiPad", map);
			id = getInsertId(map, platform);
			map.put("id", String.valueOf(id));
			map.put("filename", String.valueOf(id));

			if ("31".equals(map.get("classid"))) {
				map.put("titleurl", String.format("/iPad/soft/%d.html", id));
			} else if ("23".equals(map.get("classid"))) {
				map.put("titleurl", String.format("/iPad/game/%d.html", id));
			}

			updateExtra(map, platform);
			{
				insert("insertAppToIndexiPad", map);
				insert("insertAppToData1iPad", map);
			}
			break;
		}
		default:
			return id;
		}

		return id;
	}

	/**
	 * 更新aguo_ecms_android/iphone/ipad表中的filename, titleurl字段
	 * 
	 * @param map
	 */
	public void updateExtra(HashMap<String, String> map, int platform) {
		String sqlKey = "updateExtra";
		switch (platform) {
		case PLATFORM_ANDROID: {
			sqlKey = "updateExtra";
			break;
		}
		case PLATFORM_IPHONE: {
			sqlKey = "updateExtraiPhone";
			break;
		}
		case PLATFORM_IPAD: {
			sqlKey = "updateExtraiPad";
			break;
		}
		default:
			return;
		}
		update(sqlKey, map);
	}

	/**
	 * 更新aguo_ecms_android/iphone/ipad表中的titlepic字段，下载完外站的icon图片后，用本地url更新该字段
	 * 
	 * @param map
	 */
	public void updateTitlePic(HashMap<String, String> map, int platform) {
		String sqlKey = "updateTitlePic";
		switch (platform) {
		case PLATFORM_ANDROID: {
			sqlKey = "updateTitlePic";
			break;
		}
		case PLATFORM_IPHONE: {
			sqlKey = "updateTitlePiciPhone";
			break;
		}
		case PLATFORM_IPAD: {
			sqlKey = "updateTitlePiciPad";
			break;
		}
		default:
			return;
		}
		update(sqlKey, map);
	}

	/**
	 * 更新aguo_ecms_android/iphone/ipad表中的downpath字段，下载完外站的apk文件后，用本地url更新该字段
	 * 
	 * @param map
	 */
	public void updateDownPath(HashMap<String, String> map, int platform) {
		String sqlKey = "updateDownPath";
		switch (platform) {
		case PLATFORM_ANDROID: {
			sqlKey = "updateDownPath";
			break;
		}
		case PLATFORM_IPHONE: {
			sqlKey = "updateDownPathiPhone";
			break;
		}
		case PLATFORM_IPAD: {
			sqlKey = "updateDownPathiPad";
			break;
		}
		default:
			return;
		}
		update(sqlKey, map);
	}

	/**
	 * 更新aguo_ecms_android/iphone/ipad表中的downpath字段，下载完外站的apk文件后，用本地url更新该字段
	 * 
	 * @param map
	 */
	public void updateIpaDown(HashMap<String, String> map, int platform) {
		String sqlKey = "updateIpaDowniPhone";
		switch (platform) {
		case PLATFORM_IPHONE: {
			sqlKey = "updateIpaDowniPhone";
			break;
		}
		case PLATFORM_IPAD: {
			sqlKey = "updateIpaDowniPad";
			break;
		}
		default:
			return;
		}
		update(sqlKey, map);
	}

	/**
	 * 更新aguo_ecms_android/iphone/ipad表中的morepic字段，下载完外站的应用截图后，用本地url更新该字段
	 * 
	 * @param map
	 */
	public void updateMorePic(HashMap<String, String> map, int platform) {
		String sqlKey = "updateMorePic";
		switch (platform) {
		case PLATFORM_ANDROID: {
			sqlKey = "updateMorePic";
			break;
		}
		case PLATFORM_IPHONE: {
			sqlKey = "updateMorePiciPhone";
			break;
		}
		case PLATFORM_IPAD: {
			sqlKey = "updateMorePiciPad";
			break;
		}
		default:
			return;
		}

		update(sqlKey, map);
	}

	/**
	 * 通过md5(title + ver)去数据库取md5，如果取到，说明改应用已经存在于数据库中
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean isAppExist(Map<String, String> map, int platform) {
		String sqlKey = "selectMd5";
		switch (platform) {
		case PLATFORM_ANDROID: {
			sqlKey = "selectMd5";
			break;
		}
		case PLATFORM_IPHONE: {
			sqlKey = "selectMd5iPhone";
			break;
		}
		case PLATFORM_IPAD: {
			sqlKey = "selectMd5iPad";
			break;
		}
		default:
			return false;
		}
		List list = selectList(sqlKey, map);
		return list.size() > 0 ? true : false;
	}

	/**
	 * 通过title_version_md5获取插入记录的对应id
	 * 
	 * @param map
	 * @return
	 */
	public int getInsertId(Map<String, String> map, int platform) {
		String sqlKey = "selectIdbyMd5";
		switch (platform) {
		case PLATFORM_ANDROID: {
			sqlKey = "selectIdbyMd5";
			break;
		}
		case PLATFORM_IPHONE: {
			sqlKey = "selectIdbyMd5iPhone";
			break;
		}
		case PLATFORM_IPAD: {
			sqlKey = "selectIdbyMd5iPad";
			break;
		}
		default:
			return -1;
		}

		return selectId(sqlKey, map);
	}

	/**
	 * 通过id获取对应的classid
	 * 
	 * @param map
	 * @return
	 */
	public int getClassId(Map<String, String> map, int platform) {
		String sqlKey = "selectClassId";
		switch (platform) {
		case PLATFORM_ANDROID: {
			sqlKey = "selectClassId";
			break;
		}
		case PLATFORM_IPHONE: {
			sqlKey = "selectClassIdiPhone";
			break;
		}
		case PLATFORM_IPAD: {
			sqlKey = "selectClassIdiPad";
			break;
		}
		default:
			return -1;
		}

		return selectId(sqlKey, map);
	}

	/**
	 * 通过id判断该id是否存在于aguo_ecms_android/iphone/ipad表中
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean isIdExist(Map<String, String> map, int platform) {
		String sqlKey = "selectIdbyMd5";
		switch (platform) {
		case PLATFORM_ANDROID: {
			sqlKey = "selectIdbyMd5";
			break;
		}
		case PLATFORM_IPHONE: {
			sqlKey = "selectIdbyMd5iPhone";
			break;
		}
		case PLATFORM_IPAD: {
			sqlKey = "selectIdbyMd5iPad";
			break;
		}
		default:
			return false;
		}
		List list = selectList(sqlKey, map);
		return list.size() > 0 ? true : false;
	}

	/**
	 * 根据Id同时删除aguo_ecms_android/iphone/ipad/aguo_ecms_android/iphone/
	 * ipad_data_1/ aguo_ecms_android/iphone/ipad_index里面的记录
	 * 
	 * @return
	 */
	public Integer deleteById(HashMap<String, String> param, int platform) {
		String sqlKey = "deleteById";
		switch (platform) {
		case PLATFORM_ANDROID: {
			sqlKey = "deleteById";
			delete("deleteData1ById", param);
			delete("deleteIndexById", param);
			break;
		}
		case PLATFORM_IPHONE: {
			sqlKey = "deleteByIdiPhone";
			delete("deleteData1ByIdiPhone", param);
			delete("deleteIndexByIdiPhone", param);
			break;
		}
		case PLATFORM_IPAD: {
			sqlKey = "deleteByIdiPad";
			delete("deleteData1ByIdiPad", param);
			delete("deleteIndexByIdiPad", param);
			break;
		}
		default:
			return 0;
		}

		return delete(sqlKey, param);
	}

	public void insertAppMd5(HashMap<String, String> param, int platform) {
		String sqlKey = "insertApkMd5";
		switch (platform) {
		case PLATFORM_ANDROID: {
			sqlKey = "insertApkMd5";
			break;
		}
		case PLATFORM_IPHONE: {
			sqlKey = "insertIpaMd5";
			break;
		}
		case PLATFORM_IPAD: {
			sqlKey = "insertIpaMd5";
			break;
		}
		default:
			return;
		}
		insert(sqlKey, param);
	}

	public String getAppFilePath(HashMap<String, String> param, int platform) {
		String sqlKey = "selectApkFilePath";
		switch (platform) {
		case PLATFORM_ANDROID: {
			sqlKey = "selectApkFilePath";
			break;
		}
		case PLATFORM_IPHONE: {
			sqlKey = "selectIpaFilePath";
			break;
		}
		case PLATFORM_IPAD: {
			sqlKey = "selectIpaFilePath";
			break;
		}
		default:
			return "";
		}
		return selectOne(sqlKey, param);
	}

	public String getAppUrl(HashMap<String, String> param, int platform) {
		String sqlKey = "selectApkUrl";
		switch (platform) {
		case PLATFORM_ANDROID: {
			sqlKey = "selectApkUrl";
			break;
		}
		case PLATFORM_IPHONE: {
			sqlKey = "selectIpaUrl";
			break;
		}
		case PLATFORM_IPAD: {
			sqlKey = "selectIpaUrl";
			break;
		}
		default:
			return "";
		}
		return selectOne(sqlKey, param);
	}
	// android/iphone/ipad tables interface End

	// Factory Methods Begin
	private Integer update(String sqlKey, Object param) {
		if (sqlSessionFactory == null) {
			return -1;
		}
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession(true);
			return session.update(NS + sqlKey, param);
		} catch (Exception e) {
			logger.error("update error ,sqlKey:" + sqlKey + ",param:" + param);
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
			session = null;
		}
		return 0;
	}

	public Integer delete(String sqlKey, Object param) {
		if (sqlSessionFactory == null) {
			return -1;
		}
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession(true);
			return session.delete(NS + sqlKey, param);
		} catch (Exception e) {
			logger.error("delete error ,sqlKey:" + sqlKey + ",param:" + param);
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
			session = null;
		}
		return 0;
	}

	private Integer insert(String sqlKey, Object param) {
		if (sqlSessionFactory == null) {
			return -1;
		}
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession(true);
			return session.insert(NS + sqlKey, param);
		} catch (Exception e) {
			logger.error("insert error ,sqlKey:" + sqlKey + ",param:" + param);
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
			session = null;
		}
		return 0;
	}

	@SuppressWarnings("rawtypes")
	private List selectList(String sqlKey, Object param) {
		if (sqlSessionFactory == null) {
			return null;
		}
		List<Map<String, Object>> list = null;
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			return session.selectList(NS + sqlKey, param);
		} catch (Exception e) {
			logger.error("selectList error ,sqlKey: " + sqlKey + ",param:" + param);
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
			session = null;
		}
		return list;
	}

	private Integer selectId(String sqlKey) {
		if (sqlSessionFactory == null) {
			return null;
		}
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			return session.selectOne(NS + sqlKey);
		} catch (Exception e) {
			logger.error("selectList error ,sqlKey: " + sqlKey);
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
			session = null;
		}
		return null;
	}

	private Integer selectId(String sqlKey, Object param) {
		if (sqlSessionFactory == null) {
			return null;
		}
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			return session.selectOne(NS + sqlKey, param);
		} catch (Exception e) {
			logger.error("selectList error ,sqlKey: " + sqlKey);
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
			session = null;
		}
		return null;
	}

	private String selectOne(String sqlKey, Object param) {
		if (sqlSessionFactory == null) {
			return null;
		}
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			return session.selectOne(NS + sqlKey, param);
		} catch (Exception e) {
			logger.error("selectList error ,sqlKey: " + sqlKey);
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
			session = null;
		}
		return null;
	}
	// Factory Methods Begin
}

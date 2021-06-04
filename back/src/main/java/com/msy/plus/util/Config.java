package com.msy.plus.util;

import com.google.common.collect.Maps;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import javax.servlet.ServletContext;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;

public class Config {
    /**
     * 当前对象实例
     */
    private static Config CONFIG = new Config();

    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = Maps.newHashMap();

    /**
     * 属性文件加载对象
     */
    private static PropertiesLoader loader;

    /**
     * 上传文件基础虚拟路径
     */
    public static final String USERFILES_BASE_URL = "/userfiles/";

    private static boolean inited = false;

    private static String path = "classpath*:*.properties";

    public static void init() {
        if (!inited) {
            map = Maps.newHashMap();
            loader = new PropertiesLoader(path);
            inited = true;
        }
    }

    /**
     * 获取当前对象实例
     */
    public static Config getInstance() {
        return CONFIG;
    }

    /**
     * 获取配置
     */
    public static String get(String key) {
        init();
        Validate.notNull(map);
        String value = map.get(key);
        if (value == null) {
            Validate.notNull(loader);
            value = loader.getProperty(key);
            map.put(key, value != null ? value : StringUtils.EMPTY);
        }
        return value;
    }

    public static String get(String key, Object...args) {
        return MessageFormat.format(get(key), args);
    }

    /**
     * 获取管理端根路径
     */
    public static String getAdminPath() {
        return get("adminPath");
    }

    /**
     * 获取前端根路径
     */
    public static String getFrontPath() {
        return get("frontPath");
    }

    /**
     * 获取URL后缀
     */
    public static String getUrlSuffix() {
        return get("urlSuffix");
    }

    /**
     * 是否是演示模式，演示模式下不能修改用户、角色、密码、菜单、授权
     */
    public static Boolean isDemoMode() {
        String dm = get("demoMode");
        return "true".equals(dm) || "1".equals(dm);
    }

    /**
     * 在修改系统用户和角色时是否同步到Activiti
     */
    public static Boolean isSynActivitiIndetity() {
        String dm = get("activiti.isSynActivitiIndetity");
        return "true".equals(dm) || "1".equals(dm);
    }

    /**
     * 页面获取常量
     */
    public static Object getConst(String field) {
        try {
            return Config.class.getField(field).get(null);
        } catch (Exception e) {
            // 异常代表无配置，这里什么也不做
        }
        return null;
    }

    /**
     * 获取上传文件的根目录
     *
     * @return
     */
    public static String getUserfilesBaseDir() {
        String dir = get("userfiles.basedir");
        if (StringUtils.isBlank(dir)) {
            try {
                // dir = ServletContextFactory.getServletContext().getRealPath("/");
                dir = getServletContext().getRealPath("/");
            } catch (Exception e) {
                return "";
            }
        }
        dir = FilenameUtils.normalize(dir, true);
        if (!dir.endsWith("/")) {
            dir += "/";
        }
        // System.out.println("userfiles.basedir: " + dir);
        return dir;
    }

    /**
     * 获取工程路径
     *
     * @return
     */
    public static String getProjectPath() {
        // 如果配置了工程路径，则直接返回，否则自动获取。
        String projectPath = System.getProperty("");
        return projectPath;
    }

    /**
     * 是否生产环境
     */
    public static boolean isProdMode() {
        return "prod".equals(get("spring.profiles.env"));
    }

    /**
     * 是否测试环境
     */
    public static boolean isTestMode() {
        return "test".equals(get("spring.profiles.env"));
    }

    /**
     * 是否开发环境
     */
    public static boolean isDevMode() {
        return "dev".equals(get("spring.profiles.env"));
    }

    private ServletContext servletContext;

    public static String contextPath() {
        return getServletContext().getContextPath();
    }

    public static ServletContext getServletContext() {
        return CONFIG.servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public static Properties getPrperties() {
        init();
        return Config.loader.getProperties();
    }

}

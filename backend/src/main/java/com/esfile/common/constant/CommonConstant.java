package com.esfile.common.constant;

/**
 * 通用常量定义
 * 
 * @author esfile
 * @since 1.0.0
 */
public class CommonConstant {

    /**
     * 成功状态码
     */
    public static final int SUCCESS_CODE = 200;

    /**
     * 失败状态码
     */
    public static final int FAIL_CODE = 500;

    /**
     * 未授权状态码
     */
    public static final int UNAUTHORIZED_CODE = 401;

    /**
     * 禁止访问状态码
     */
    public static final int FORBIDDEN_CODE = 403;

    /**
     * 成功消息
     */
    public static final String SUCCESS_MESSAGE = "操作成功";

    /**
     * 失败消息
     */
    public static final String FAIL_MESSAGE = "操作失败";

    /**
     * 系统错误消息
     */
    public static final String SYSTEM_ERROR_MESSAGE = "系统错误，请联系管理员";

    /**
     * 参数错误消息
     */
    public static final String PARAM_ERROR_MESSAGE = "参数错误";

    /**
     * 数据不存在消息
     */
    public static final String DATA_NOT_FOUND_MESSAGE = "数据不存在";

    /**
     * 默认分页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 最大分页大小
     */
    public static final int MAX_PAGE_SIZE = 100;

    /**
     * 默认排序字段
     */
    public static final String DEFAULT_SORT_FIELD = "createTime";

    /**
     * 默认排序方向
     */
    public static final String DEFAULT_SORT_DIRECTION = "DESC";

    /**
     * 升序
     */
    public static final String ASC = "ASC";

    /**
     * 降序
     */
    public static final String DESC = "DESC";

    /**
     * 是
     */
    public static final String YES = "1";

    /**
     * 否
     */
    public static final String NO = "0";

    /**
     * 启用状态
     */
    public static final String ENABLE = "1";

    /**
     * 禁用状态
     */
    public static final String DISABLE = "0";

    /**
     * 删除标记
     */
    public static final String DELETED = "1";

    /**
     * 未删除标记
     */
    public static final String NOT_DELETED = "0";

    /**
     * 根节点ID
     */
    public static final Long ROOT_ID = 0L;

    /**
     * 顶级菜单ID
     */
    public static final Long TOP_MENU_ID = 0L;

    /**
     * 超级管理员角色ID
     */
    public static final Long SUPER_ADMIN_ROLE_ID = 1L;

    /**
     * 超级管理员用户ID
     */
    public static final Long SUPER_ADMIN_USER_ID = 1L;

    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * 密码加密盐值长度
     */
    public static final int SALT_LENGTH = 20;

    /**
     * Token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 请求头中的Token键名
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * 用户信息键名
     */
    public static final String USER_INFO_KEY = "userInfo";

    /**
     * 权限信息键名
     */
    public static final String PERMISSION_KEY = "permissions";

    /**
     * 角色信息键名
     */
    public static final String ROLE_KEY = "roles";

    /**
     * 菜单信息键名
     */
    public static final String MENU_KEY = "menus";

    /**
     * 验证码键名
     */
    public static final String CAPTCHA_KEY = "captcha";

    /**
     * 登录失败次数键名
     */
    public static final String LOGIN_FAIL_COUNT_KEY = "loginFailCount";

    /**
     * 最大登录失败次数
     */
    public static final int MAX_LOGIN_FAIL_COUNT = 5;

    /**
     * 登录失败锁定时间（分钟）
     */
    public static final int LOGIN_FAIL_LOCK_TIME = 30;

    /**
     * 文件上传临时目录
     */
    public static final String UPLOAD_TEMP_DIR = "/tmp/upload";

    /**
     * 文件上传最大大小（字节）
     */
    public static final long UPLOAD_MAX_SIZE = -1L;

    /**
     * 文件分片大小（字节）
     */
    public static final int UPLOAD_CHUNK_SIZE = 1024 * 1024;

    /**
     * 支持的文件类型
     */
    public static final String[] SUPPORTED_FILE_TYPES = {
        "jpg", "jpeg", "png", "gif", "bmp", "webp",
        "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",
        "txt", "md", "html", "css", "js", "json", "xml",
        "mp4", "avi", "mov", "wmv", "flv", "mkv",
        "mp3", "wav", "flac", "aac", "ogg"
    };

    /**
     * 图片文件类型
     */
    public static final String[] IMAGE_FILE_TYPES = {
        "jpg", "jpeg", "png", "gif", "bmp", "webp"
    };

    /**
     * 文档文件类型
     */
    public static final String[] DOCUMENT_FILE_TYPES = {
        "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",
        "txt", "md", "html", "css", "js", "json", "xml"
    };

    /**
     * 视频文件类型
     */
    public static final String[] VIDEO_FILE_TYPES = {
        "mp4", "avi", "mov", "wmv", "flv", "mkv"
    };

    /**
     * 音频文件类型
     */
    public static final String[] AUDIO_FILE_TYPES = {
        "mp3", "wav", "flac", "aac", "ogg"
    };

    /**
     * 私有构造函数，防止实例化
     */
    private CommonConstant() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}

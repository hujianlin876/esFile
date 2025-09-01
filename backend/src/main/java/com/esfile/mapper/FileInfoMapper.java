package com.esfile.mapper;

import com.esfile.entity.mybatis.FileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 文件信息Mapper接口
 * 
 * @author esfile
 * @since 2024-01-01
 */
@Mapper
public interface FileInfoMapper {
    
    /**
     * 插入文件信息
     * 
     * @param fileInfo 文件信息
     * @return 影响行数
     */
    int insert(FileInfo fileInfo);
    
    /**
     * 根据ID更新文件信息
     * 
     * @param fileInfo 文件信息
     * @return 影响行数
     */
    int updateById(FileInfo fileInfo);
    
    /**
     * 根据ID删除文件信息
     * 
     * @param id 文件ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据ID查询文件信息
     * 
     * @param id 文件ID
     * @return 文件信息
     */
    FileInfo selectById(@Param("id") Long id);
    
    /**
     * 根据文件名查询文件信息
     * 
     * @param fileName 文件名
     * @return 文件信息列表
     */
    List<FileInfo> selectByFileName(@Param("fileName") String fileName);
    
    /**
     * 根据文件类型查询文件信息
     * 
     * @param fileType 文件类型
     * @return 文件信息列表
     */
    List<FileInfo> selectByFileType(@Param("fileType") String fileType);
    
    /**
     * 根据上传用户ID查询文件信息
     * 
     * @param uploadUserId 上传用户ID
     * @return 文件信息列表
     */
    List<FileInfo> selectByUploadUserId(@Param("uploadUserId") Long uploadUserId);
    
    /**
     * 根据MD5值查询文件信息
     * 
     * @param fileMd5 文件MD5值
     * @return 文件信息
     */
    FileInfo selectByFileMd5(@Param("fileMd5") String fileMd5);
    
    /**
     * 查询所有文件信息
     * 
     * @return 文件信息列表
     */
    List<FileInfo> selectAll();
    
    /**
     * 分页查询文件信息
     * 
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 文件信息列表
     */
    List<FileInfo> selectPage(@Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 查询文件信息总数
     * 
     * @return 总数
     */
    long selectCount();
    
    /**
     * 根据条件查询文件信息
     * 
     * @param fileInfo 查询条件
     * @return 文件信息列表
     */
    List<FileInfo> selectByCondition(FileInfo fileInfo);
    
    /**
     * 更新下载次数
     * 
     * @param id 文件ID
     * @return 影响行数
     */
    int updateDownloadCount(@Param("id") Long id);
    
    /**
     * 更新预览次数
     * 
     * @param id 文件ID
     * @return 影响行数
     */
    int updatePreviewCount(@Param("id") Long id);
    
    /**
     * 根据关键词搜索文件
     * 
     * @param keyword 关键词
     * @return 文件信息列表
     */
    List<FileInfo> searchByKeyword(@Param("keyword") String keyword);
    
    /**
     * 根据标签查询文件
     * 
     * @param tag 标签
     * @return 文件信息列表
     */
    List<FileInfo> selectByTag(@Param("tag") String tag);
    
    /**
     * 根据文件夹ID查询文件
     * 
     * @param folderId 文件夹ID
     * @return 文件信息列表
     */
    List<FileInfo> selectByFolderId(@Param("folderId") Long folderId);
    
    /**
     * 根据状态查询文件
     * 
     * @param status 文件状态
     * @return 文件信息列表
     */
    List<FileInfo> selectByStatus(@Param("status") Integer status);
    
    /**
     * 根据创建时间范围查询文件
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 文件信息列表
     */
    List<FileInfo> selectByCreateTimeRange(@Param("startTime") String startTime, @Param("endTime") String endTime);
    
    /**
     * 根据文件大小范围查询文件
     * 
     * @param minSize 最小大小
     * @param maxSize 最大大小
     * @return 文件信息列表
     */
    List<FileInfo> selectBySizeRange(@Param("minSize") Long minSize, @Param("maxSize") Long maxSize);
    
    /**
     * 批量删除文件
     * 
     * @param ids 文件ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("ids") List<Long> ids);
    
    /**
     * 批量更新文件状态
     * 
     * @param ids 文件ID列表
     * @param status 状态
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);
    
    /**
     * 获取文件类型统计
     * 
     * @return 文件类型统计列表
     */
    List<Map<String, Object>> selectFileTypeStats();
    
    /**
     * 获取存储使用统计
     * 
     * @return 存储使用统计
     */
    Map<String, Object> selectStorageUsageStats();
    
    /**
     * 查找重复文件
     * 
     * @return 重复文件列表
     */
    List<Map<String, Object>> selectDuplicateFiles();
    
    /**
     * 根据权限查询文件
     * 
     * @param userId 用户ID
     * @param permission 权限
     * @return 文件信息列表
     */
    List<FileInfo> selectByPermission(@Param("userId") Long userId, @Param("permission") String permission);
    
    /**
     * 根据父级文件夹ID查询文件
     * 
     * @param parentId 父级文件夹ID
     * @return 文件信息列表
     */
    List<FileInfo> selectByParentId(@Param("parentId") Long parentId);
    
    /**
     * 根据父级文件夹ID查询文件夹
     * 
     * @param parentId 父级文件夹ID
     * @return 文件夹列表
     */
    List<FileInfo> selectFoldersByParentId(@Param("parentId") Long parentId);
    
    /**
     * 查询总存储大小
     * 
     * @return 总存储大小
     */
    Long selectTotalSize();
    
    /**
     * 查询今日上传数量
     * 
     * @return 今日上传数量
     */
    Long selectTodayUploads();
    
    /**
     * 查询文件夹数量
     * 
     * @return 文件夹数量
     */
    Long selectFolderCount();
    
    /**
     * 查询月度上传统计
     * 
     * @return 月度上传统计
     */
    List<Map<String, Object>> selectMonthlyUploadStats();
}

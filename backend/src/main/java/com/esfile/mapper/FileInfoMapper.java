package com.esfile.mapper;

import com.esfile.entity.mybatis.FileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
}

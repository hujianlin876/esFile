package com.esfile.service.file;

import com.esfile.common.vo.PageResult;
import com.esfile.entity.mybatis.FileInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @org.springframework.beans.factory.annotation.Autowired
    private com.esfile.mapper.FileInfoMapper fileInfoMapper;

    @Override
    public FileInfo uploadFile(MultipartFile file, Long userId, String description, String tags, Integer isPublic) {
        // 这里只做元数据入库，实际存储请接入 MinIO
        FileInfo info = new FileInfo();
        info.setFileName(file.getOriginalFilename());
        info.setOriginalFileName(file.getOriginalFilename());
        info.setFileSize(file.getSize());
        info.setFileType(file.getContentType());
        info.setUploadUserId(userId);
        info.setDescription(description);
        info.setTags(tags);
        info.setIsPublic(isPublic);
        info.setStatus(1);
        // TODO: MinIO存储，设置bucketName/objectName/fileUrl
        fileInfoMapper.insert(info);
        return info;
    }

    @Override
    public List<FileInfo> batchUploadFiles(List<MultipartFile> files, Long userId, String description, String tags, Integer isPublic) {
        List<FileInfo> result = new ArrayList<>();
        for (MultipartFile file : files) {
            result.add(uploadFile(file, userId, description, tags, isPublic));
        }
        return result;
    }

    @Override
    public boolean deleteFile(Long fileId, Long userId) {
        // 逻辑删除
        FileInfo info = fileInfoMapper.selectById(fileId);
        if (info == null || info.getStatus() == 0) return false;
        info.setStatus(0);
        return fileInfoMapper.updateById(info) > 0;
    }

    @Override
    public FileInfo getFileById(Long fileId) {
        return fileInfoMapper.selectById(fileId);
    }

    @Override
    public PageResult<FileInfo> getFilePage(int page, int size, String fileName, String fileType, Long uploadUserId) {
        int offset = (page - 1) * size;
        List<FileInfo> data = fileInfoMapper.selectPage(offset, size);
        long total = fileInfoMapper.selectCount();
        PageResult<FileInfo> result = new PageResult<>();
        result.setPageNum(page);
        result.setPageSize(size);
        result.setTotal(total);
        result.setData(data);
        result.setTotalPages((int) Math.ceil((double) total / size));
        return result;
    }

    @Override
    public List<FileInfo> getFilesByCondition(FileInfo fileInfo) {
        return fileInfoMapper.selectByCondition(fileInfo);
    }

    @Override
    public boolean updateFileInfo(FileInfo fileInfo) {
        return fileInfoMapper.updateById(fileInfo) > 0;
    }

    @Override
    public void updateDownloadCount(Long fileId) {
        fileInfoMapper.updateDownloadCount(fileId);
    }

    @Override
    public void updatePreviewCount(Long fileId) {
        fileInfoMapper.updatePreviewCount(fileId);
    }

    @Override
    public boolean isFileExists(String fileMd5) {
        return fileInfoMapper.selectByFileMd5(fileMd5) != null;
    }

    @Override
    public String getFilePreviewUrl(Long fileId) {
        FileInfo info = fileInfoMapper.selectById(fileId);
        return info != null ? info.getFileUrl() : null;
    }

    @Override
    public String getFileDownloadUrl(Long fileId) {
        FileInfo info = fileInfoMapper.selectById(fileId);
        return info != null ? info.getFileUrl() : null;
    }
}

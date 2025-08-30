package com.esfile.service.file.impl;
import com.esfile.service.file.FilePreviewService;
import org.springframework.stereotype.Service;
/**
 * 文件预览服务实现类
 */
@Service
public class FilePreviewServiceImpl implements FilePreviewService {
    @Override
    public String getPreviewUrl(Long fileId) {
        // TODO: 预览URL实现
        return "preview-url";
    }
}

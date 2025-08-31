package com.esfile.controller.file;

import com.esfile.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * 文件预览控制器
 */
@RestController
@RequestMapping("/api/file/preview")
public class FilePreviewController {
    @Autowired
    private com.esfile.service.file.FileService fileService;

    /**
     * 文件预览
     */
    @GetMapping("/{id}")
    public String preview(@PathVariable Long id) {
        // 使用正确的方法调用
        return "文件预览功能待实现";
    }
}

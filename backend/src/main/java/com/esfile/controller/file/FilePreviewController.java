package com.esfile.controller.file;
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
        return fileService.getFilePreviewUrl(id);
    }
}

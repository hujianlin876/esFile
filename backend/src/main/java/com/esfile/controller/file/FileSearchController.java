package com.esfile.controller.file;
import com.esfile.entity.dto.SearchDto;
import org.springframework.web.bind.annotation.*;
/**
 * 文件搜索控制器
 */
@RestController
@RequestMapping("/api/file/search")
public class FileSearchController {
    @Autowired
    private com.esfile.service.file.FileService fileService;

    /**
     * 文件搜索
     */
    @PostMapping
    public Object search(@RequestBody SearchDto searchDto) {
        // 假设 SearchDto 可转为 FileInfo 条件
        com.esfile.entity.mybatis.FileInfo condition = new com.esfile.entity.mybatis.FileInfo();
        condition.setFileName(searchDto.getFileName());
        condition.setFileType(searchDto.getFileType());
        condition.setUploadUserId(searchDto.getUploadUserId());
        return fileService.getFilesByCondition(condition);
    }
}

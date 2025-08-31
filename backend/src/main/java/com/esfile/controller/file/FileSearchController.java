package com.esfile.controller.file;

import com.esfile.entity.dto.SearchDto;
import com.esfile.entity.mybatis.FileInfo;
import com.esfile.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
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
        // 简化实现，直接返回提示信息
        return "文件搜索功能待实现";
    }
}

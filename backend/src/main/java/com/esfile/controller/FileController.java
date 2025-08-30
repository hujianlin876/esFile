package com.esfile.controller;

import com.esfile.common.vo.PageResult;
import com.esfile.entity.mybatis.FileInfo;
import com.esfile.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private FileService fileService;

        /**
         * 分页查询文件
         */
        @GetMapping
        public PageResult<FileInfo> listFiles(@RequestParam int page,
                                             @RequestParam int size,
                                             @RequestParam(required = false) String fileName,
                                             @RequestParam(required = false) String fileType,
                                             @RequestParam(required = false) Long uploadUserId) {
            return fileService.getFilePage(page, size, fileName, fileType, uploadUserId);
        }

        /**
         * 上传文件
         */
        @PostMapping("/upload")
        public FileInfo uploadFile(@RequestParam("file") MultipartFile file,
                                   @RequestParam Long userId,
                                   @RequestParam(required = false) String description,
                                   @RequestParam(required = false) String tags,
                                   @RequestParam(required = false) Integer isPublic) {
            return fileService.uploadFile(file, userId, description, tags, isPublic);
        }

        /**
         * 删除文件
         */
        @DeleteMapping("/{id}")
        public boolean deleteFile(@PathVariable Long id, @RequestParam Long userId) {
            return fileService.deleteFile(id, userId);
        }
    
        /**
         * 获取文件详情
         */
        @GetMapping("/{id}")
        public FileInfo getFileDetail(@PathVariable Long id) {
            return fileService.getFileById(id);
        }
}

package com.esfile.service.file.impl;
import com.esfile.entity.dto.SearchDto;
import com.esfile.service.file.FileSearchService;
import org.springframework.stereotype.Service;
/**
 * 文件搜索服务实现类
 */
@Service
public class FileSearchServiceImpl implements FileSearchService {
    @Override
    public String search(SearchDto searchDto) {
        // TODO: 搜索实现
        return "search-result";
    }
}

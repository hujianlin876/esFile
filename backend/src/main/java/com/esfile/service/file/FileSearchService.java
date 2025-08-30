package com.esfile.service.file;
import com.esfile.entity.dto.SearchDto;
/**
 * 文件搜索服务接口
 */
public interface FileSearchService {
    String search(SearchDto searchDto);
}

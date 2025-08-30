package com.esfile.service.system.impl;
import com.esfile.service.system.PermissionService;
import org.springframework.stereotype.Service;
/**
 * 权限服务实现类
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @org.springframework.beans.factory.annotation.Autowired
    private com.esfile.mapper.PermissionMapper permissionMapper;

    @Override
    public boolean createPermission(com.esfile.entity.mybatis.Permission permission) {
        return permissionMapper.insert(permission) > 0;
    }

    @Override
    public boolean updatePermission(com.esfile.entity.mybatis.Permission permission) {
        return permissionMapper.updateById(permission) > 0;
    }

    @Override
    public boolean deletePermission(Long id) {
        return permissionMapper.deleteById(id) > 0;
    }

    @Override
    public com.esfile.entity.mybatis.Permission getPermissionById(Long id) {
        return permissionMapper.selectById(id);
    }

    @Override
    public com.esfile.entity.mybatis.Permission getPermissionByCode(String permissionCode) {
        return permissionMapper.selectByPermissionCode(permissionCode);
    }

    @Override
    public java.util.List<com.esfile.entity.mybatis.Permission> getAllPermissions() {
        return permissionMapper.selectAll();
    }

    @Override
    public com.esfile.common.vo.PageResult<com.esfile.entity.mybatis.Permission> getPermissionPage(int page, int size) {
        int offset = (page - 1) * size;
        java.util.List<com.esfile.entity.mybatis.Permission> data = permissionMapper.selectPage(offset, size);
        long total = permissionMapper.selectCount();
        com.esfile.common.vo.PageResult<com.esfile.entity.mybatis.Permission> result = new com.esfile.common.vo.PageResult<>();
        result.setPageNum(page);
        result.setPageSize(size);
        result.setTotal(total);
        result.setData(data);
        result.setTotalPages((int) Math.ceil((double) total / size));
        return result;
    }

    @Override
    public java.util.List<com.esfile.entity.mybatis.Permission> getPermissionsByParentId(Long parentId) {
        // 如有 parentId 字段可实现，否则返回全部
        return permissionMapper.selectAll();
    }
}

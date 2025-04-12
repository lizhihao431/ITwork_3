package com.library.management;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 用户权限管理模块
 * 功能：管理用户角色、权限分配与验证
 */
public class UserPermission {
    // 预定义系统角色
    public enum SystemRole {
        ADMIN, LIBRARIAN, READER, GUEST
    }

    // 预定义操作权限
    public enum OperationPermission {
        BOOK_MANAGE, USER_MANAGE, BORROW_MANAGE, DATA_BACKUP
    }

    private final Map<String, SystemRole> userRoles = new HashMap<>();
    private final Map<SystemRole, Set<OperationPermission>> rolePermissions = new HashMap<>();

    public UserPermission() {
        initializeDefaultPermissions();
    }

    // 初始化默认权限
    private void initializeDefaultPermissions() {
        // 管理员权限
        Set<OperationPermission> adminPermissions = new HashSet<>();
        adminPermissions.add(OperationPermission.BOOK_MANAGE);
        adminPermissions.add(OperationPermission.USER_MANAGE);
        adminPermissions.add(OperationPermission.BORROW_MANAGE);
        adminPermissions.add(OperationPermission.DATA_BACKUP);
        rolePermissions.put(SystemRole.ADMIN, adminPermissions);

        // 图书馆员权限
        Set<OperationPermission> librarianPermissions = new HashSet<>();
        librarianPermissions.add(OperationPermission.BORROW_MANAGE);
        rolePermissions.put(SystemRole.LIBRARIAN, librarianPermissions);

        // 读者权限（基础权限）
        Set<OperationPermission> readerPermissions = new HashSet<>();
        rolePermissions.put(SystemRole.READER, readerPermissions);
    }

    // 添加用户角色
    public void assignRole(String username, SystemRole role) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        userRoles.put(username, role);
    }

    // 验证用户权限
    public boolean checkPermission(String username, OperationPermission permission) {
        SystemRole role = userRoles.get(username);
        if (role == null) return false;
        
        Set<OperationPermission> permissions = rolePermissions.get(role);
        return permissions != null && permissions.contains(permission);
    }

    // 自定义角色权限
    public void customizeRolePermission(SystemRole role, OperationPermission permission, boolean enable) {
        Set<OperationPermission> permissions = rolePermissions.computeIfAbsent(role, k -> new HashSet<>());
        if (enable) {
            permissions.add(permission);
        } else {
            permissions.remove(permission);
        }
    }

    // 获取用户角色
    public SystemRole getUserRole(String username) {
        return userRoles.get(username);
    }

    // 用户权限异常类
    public static class PermissionException extends RuntimeException {
        public PermissionException(String message) {
            super(message);
        }
    }

    // 示例使用
    public static void main(String[] args) {
        UserPermission permissionManager = new UserPermission();
        
        // 分配角色
        permissionManager.assignRole("admin1", SystemRole.ADMIN);
        permissionManager.assignRole("librarian1", SystemRole.LIBRARIAN);

        // 权限验证测试
        System.out.println("管理员是否有备份权限：" + 
            permissionManager.checkPermission("admin1", OperationPermission.DATA_BACKUP));
        
        System.out.println("馆员是否有用户管理权限：" + 
            permissionManager.checkPermission("librarian1", OperationPermission.USER_MANAGE));
    }
}

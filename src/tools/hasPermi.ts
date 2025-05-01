import useUserStore from "@/store/modules/user";

/**
 * 检测是否包含权限
 * @param permission
 * @returns
 */
export function hasPer(permission: string) {
    const all_permission = "*:*:*";
    let userPermissions = useUserStore().permissions;
    // console.log(userPermissions)
    return userPermissions.includes(permission) || userPermissions.includes(all_permission);
}

export function hasRoleId(roleId?: Number) {
    let userRoles = useUserStore().roles;
    const super_admin = "admin";
    if (userRoles.includes(super_admin)) {
        return true;
    }
    if (!roleId) {
        return false;
    }
    return useUserStore().roleIds.includes(roleId);
}

/**
 * 检测是否包含角色
 * @param rolename
 * @returns
 */
export function hasRole(rolename: string) {
    let userRoles = useUserStore().roles;
    const super_admin = "admin";
    // console.log(userRoles)
    return userRoles.includes(super_admin) || userRoles.includes(rolename);
}
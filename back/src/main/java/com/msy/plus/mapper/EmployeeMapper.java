package com.msy.plus.mapper;

import com.msy.plus.core.mapper.MyMapper;
import com.msy.plus.entity.Employee;
import com.msy.plus.entity.EmployeeDetail;
import com.msy.plus.entity.EmployeeWithRoleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeMapper extends MyMapper<Employee> {
    EmployeeDetail getDetailById(@Param("id") Long id);

    /**
     * 分页查询员工
     * @return
     */
    List<EmployeeWithRoleDO> listEmployeeWithRole(@Param("keyword") String keyword,@Param("dept")  Integer dept);

    /**
     * 保存员工角色信息
     * @param id
     * @param roles
     * @return
     */
    void saveRoles(@Param("id") Long id,@Param("roles")  List<Long> roles);

    /**
     * 删除员工权限
     * @param id
     * @return
     */
    int deleteEmployeeWithRole(Long id);
    int deleteEmployeeWithRoleItem(@Param("id") Long id,@Param("roleId")Long roleId);

    /**
     * 获取所有中间表的id
     * @param id
     * @return List<RolePermissionDO>
     */
    List<Long> getAllEmployeeRoleTableRow(@Param("id") Long id);

}
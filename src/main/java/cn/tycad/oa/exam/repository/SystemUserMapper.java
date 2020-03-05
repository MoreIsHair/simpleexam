package cn.tycad.oa.exam.repository;

import cn.tycad.oa.exam.model.bo.SystemUserBo;
import cn.tycad.oa.exam.model.entity.SystemUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * @Author: YY
 * @Date: 2019/3/4 16:53
 * @Description:
 */
@Mapper
public interface SystemUserMapper {

     /**
      * 添加一个用户
      * @param user
      */
     void add(SystemUser user);

     /**
      * 获取所有用户
      * @return 用户列表
      */
     List<SystemUserBo> list(@Param(value = "username") String userName,@Param("name") String name);

     /**
      * 查询单个用户
      * @return User对象
      */
     SystemUser getOne(String id);

     /**
      * 根据角色id获取所有用户
      * @param roleId 角色id
      * @return 用户信息列表
      */
     List<SystemUser> getByRoleId(String roleId);

     /**
      * 查询用户所有角色的权限
      * @param userId
      * @return
      */
     List<String> findAllAuthorityNameByUserId(@Param("userId") String userId);

     /**
      * 通过唯一用户名查找用户
      * @param userName
      * @return
      */
     SystemUserBo findByUserName(@Param("userName") String userName);

     /**
      * 通过用户名以及角色类型查找用户
      * @param userName
      * @param roleType
      * @return
      */
     SystemUser findByUserNameAndRoleType(@Param("userName") String userName,@Param("roleType") int roleType);

     /**
      * 批量导入用户
      * @param users 用户信息
      */
     void bulkInsert(@Param("users") List<SystemUserBo> users);

     /**
      * 根据角色类型与用户ID进行查找
      * @param userId
      * @param roleType
      */
     int findIsRoleByUserIdAndRoleType(@Param("userId") String userId,@Param("roleType") int roleType);

    void updateUserPassword(String newPass, String userId);


     /**
      *  批量修改密码
      * @param users
      */
     void batchUpdatePassword(@Param("users") Map users);
}

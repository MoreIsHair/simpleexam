package cn.tycad.oa.exam.service;

import cn.tycad.oa.exam.common.util.JwtUtils;
import cn.tycad.oa.exam.common.util.UserUtils;
import cn.tycad.oa.exam.excel.ExcelUserInfo;
import cn.tycad.oa.exam.common.*;
import cn.tycad.oa.exam.excel.ExcelUtil;
import cn.tycad.oa.exam.exception.BusinessException;
import cn.tycad.oa.exam.common.enums.ExceptionInfoEnum;
import cn.tycad.oa.exam.interceptor.AuthorityInterceptor;
import cn.tycad.oa.exam.model.bo.SystemUserBo;
import cn.tycad.oa.exam.model.bo.UserRoleBo;
import cn.tycad.oa.exam.model.entity.SystemDepartment;
import cn.tycad.oa.exam.model.entity.SystemGroup;
import cn.tycad.oa.exam.model.entity.SystemRole;
import cn.tycad.oa.exam.model.entity.SystemUser;
import cn.tycad.oa.exam.model.param.UserNameAndPasswordParam;
import cn.tycad.oa.exam.repository.SystemDepartmentMapper;
import cn.tycad.oa.exam.repository.SystemGroupMapper;
import cn.tycad.oa.exam.repository.SystemRoleMapper;
import cn.tycad.oa.exam.repository.SystemUserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * @author YY
 * @date 2019/3/5 9:35
 * @description
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private SystemUserMapper userMapper;

    @Autowired
    private AuthorityInterceptor authorityInterceptor;

    @Autowired
    private SystemDepartmentMapper departmentMapper;

    @Autowired
    private SystemGroupMapper groupMapper;

    @Autowired
    private SystemRoleMapper roleMapper;

    /**
     * 生成token密文
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * 过期的时间长度
     */
    @Value("${jwt.expire}")
    private Float expire;

    @Transactional(rollbackFor = Exception.class)
    public void save(SystemUser user){
        userMapper.add(user);
    }

    /**
     * 分页查询所有用户
     * @return 用户信息列表
     */
    public PageInfo<SystemUserBo> list(int pageNum, int pageSize, String username,String name) {
        if (pageNum == 0 || pageSize == 0) {
            pageNum = 1;
            pageSize = 15;
        }
        PageHelper.startPage(pageNum, pageSize);
        List<SystemUserBo> users = userMapper.list(username,name);
        List<String> userIds = users.stream().map(m -> m.getUserId())
                .collect(toList());

        List<UserRoleBo> roles = roleMapper.getByUserIds(userIds);

        users.stream().map(u -> {
            Optional<UserRoleBo> item = roles.stream()
                    .filter(r -> r.getUserId().toLowerCase().equals(u.getUserId().toLowerCase()))
                    .findFirst();
            if (item.isPresent()) {
                u.setRoles(item.get().getRoles());
            }
            return u;
        }).collect(toList());

        PageInfo<SystemUserBo> userPageInfo = new PageInfo<>(users);
        return userPageInfo;
    }

    /**
     * 查询单个用户
     * @param id 用户id
     * @return User对象
     */
    public SystemUser getOne(String id) {
        return userMapper.getOne(id);
    }

    /**
     * 获取用户所有角色的权限
     * @param userId
     * @return
     */
    public List<String> getPermission(String userId) {
        if (StringUtils.isBlank(userId)){
            log.debug("用户名为空");
            throw new BusinessException(ExceptionInfoEnum.USERNAME_OR_PASSWORD_NULL_ERROR);
        }
        return userMapper.findAllAuthorityNameByUserId(userId);
    }

    public Map login(UserNameAndPasswordParam userNameAndPasswordParam) {
        HashMap<Object, Object> map = new HashMap<>(16);
        if (!StringUtils.isNotBlank(userNameAndPasswordParam.getUserName())||
                !StringUtils.isNotBlank(userNameAndPasswordParam.getPassword())){
            log.debug("用户名或者密码为空");
            throw new BusinessException(ExceptionInfoEnum.USERNAME_OR_PASSWORD_NULL_ERROR);
        }
        TokenHelp tokenHelp1 = authorityInterceptor.getSystemUserMap().get(userNameAndPasswordParam.getUserName());
        SystemUserBo systemUser = checkUserPassword(userNameAndPasswordParam.getUserName(), userNameAndPasswordParam.getPassword());
        if (tokenHelp1 != null){
            log.debug("用户已登录");
            map.put("token",tokenHelp1.getToken());
            map.put("userId",tokenHelp1.getUserId());
            map.put("roles", systemUser.getRoles());
            tokenHelp1.setLastRequestTime(new Date());
            return map;
        }

        SystemUser systemUser1 = new SystemUser(systemUser);
        String token = JwtUtils.createJWT(systemUser1,60 * 1000 * expire,secret);
        TokenHelp tokenHelp = new TokenHelp(systemUser.getUserId(),systemUser.getUsername(), new Date(),token);
        authorityInterceptor.getSystemUserMap().put(systemUser.getUsername(),tokenHelp);
        map.put("token",token);
        map.put("userId",systemUser.getUserId());
        map.put("roles", systemUser.getRoles());
        return map;
    }


    /**
     * 检查用户密码是否正确
     * @param userName
     * @param password
     * @return
     */
    public SystemUserBo checkUserPassword(String userName,String password){
        SystemUserBo byUserName = userMapper.findByUserName(userName);
        if (byUserName == null){
            log.debug("用户不存在");
            throw new BusinessException(ExceptionInfoEnum.USERNAME_NO_EXIST);
        }
        String checkPassword = UserUtils.md5DigestAsHex(password, userName);
        if (!checkPassword.equals(byUserName.getPassword())){
            log.debug("用户名或者密码不正确");
            throw new BusinessException(ExceptionInfoEnum.USERNAME_OR_PASSWORD_NO_VALID);
        }
        return byUserName;
    }

    /**
     * 注册用户
     * @param userNameAndPasswordParam
     */
    @Transactional(rollbackFor = Exception.class)
    public void registerUser(UserNameAndPasswordParam userNameAndPasswordParam) {
        if (!StringUtils.isNotBlank(userNameAndPasswordParam.getUserName())||
                !StringUtils.isNotBlank(userNameAndPasswordParam.getPassword())){
            log.debug("用户名或者密码为空");
            throw new BusinessException(ExceptionInfoEnum.USERNAME_OR_PASSWORD_NULL_ERROR);
        }
        SystemUser byUserName = userMapper.findByUserName(userNameAndPasswordParam.getUserName());
        // 判断用户名是否存在
        if (byUserName!=null){
            log.debug("用户名已被使用");
            throw new BusinessException(ExceptionInfoEnum.USERNAME_ALREADY_EXIST);
        }
        // 插入
        SystemUser user = new SystemUser();
        user.setUserId(UUID.randomUUID().toString());
        user.setUsername(userNameAndPasswordParam.getUserName());
        user.setPassword(UserUtils.md5DigestAsHex(userNameAndPasswordParam.getPassword()
                ,userNameAndPasswordParam.getUserName()));
        user.setGender("未知");
        user.setCreateTime(new Date());
        user.setLastEditTime(new Date());
        userMapper.add(user);
    }

    public void logout(HttpServletRequest request){
        String token = UserUtils.getToken(request);
        SystemUser systemUser = JwtUtils.parseJWT(token, SystemUser.class, secret);
        authorityInterceptor.getSystemUserMap().remove(systemUser.getUsername());
        return;
    }

    /**
     * 根据group id获取其下所有用户
     * @param groupId 分组id
     * @return
     */
    public List<SystemUser> getUsersByGroupId(String groupId) {
        if (groupId.isEmpty()) {throw new BusinessException(ExceptionInfoEnum.ID_ILLEGAL_ERROR);}
        return groupMapper.getUsersByGroupId(groupId);
    }

    /**
     * 批量写入用户
     * @param file
     * @throws FileNotFoundException
     */
    @Transactional(rollbackFor = Exception.class)
    public void bulkInsertUser(File file) throws IOException {
        List<Object> users = ExcelUtil.readExcelWithModel(new BufferedInputStream(new FileInputStream(file)),
                1, ExcelUserInfo.class, true);

        //过滤数据，并转换成SystemUser模型
        List<SystemUserBo> transformedData = users.stream()
                .map(u -> {
                    ExcelUserInfo tmp = (ExcelUserInfo)u;
                    SystemUserBo user = new SystemUserBo();
                    user.setUserId(UUID.randomUUID().toString());
                    user.setUsername(tmp.getUserName());
                    user.setName(tmp.getName());
                    user.setPassword(tmp.getPassword());
                    user.setGender(tmp.getGender());
                    user.setDescription(tmp.getDescription());
                    user.setBirthday(tmp.getBirthday());
                    user.setGroupName(tmp.getGroup());
                    user.setDeptId(tmp.getDept());
                    user.setCreateTime(new Date());
                    user.setLastEditTime(new Date());
                    user.setCompanyName(tmp.getCompanyName());
                    user.setType("内部员工".equals(tmp.getTypeString())?0:1);

                    //用户角色
                    List<SystemRole> roles = new ArrayList<>();
                    SystemRole role = new SystemRole();
                    role.setRoleName(tmp.getRole());
                    roles.add(role);
                    user.setRoles(roles);

                    return user;
                })
                .filter(u -> u.getUsername() != null)
                .filter(u -> u.getPassword() != null)
                .filter(u -> u.getName() != null)
                .map(u -> {u.setPassword(UserUtils.md5DigestAsHex(u.getPassword(), u.getUsername())); return u;})
                .collect(toList());

        if (transformedData.size() > 0) {
            List<SystemUserBo> finalData = buildUserList(transformedData);
            userMapper.bulkInsert(finalData);
        } else {
            throw new BusinessException(ExceptionInfoEnum.NONE_VALID_DATA);
        }
    }

    /**
     * 构造用户数据
     * @param users
     * @return
     */
    private List<SystemUserBo> buildUserList(List<SystemUserBo> users) {
        //获取部门
        List<SystemDepartment> departments = departmentMapper.getList();
        //获取分组
        List<SystemGroup> groups = groupMapper.getList();
        //获取角色
        List<SystemRole> roles = roleMapper.list();

        return users.stream()
                .map(item -> {
                    Optional<SystemDepartment> dep = departments.stream()
                            .filter(d -> d.getDeptName().equals(item.getDeptId()))
                            .findFirst();
                    if (dep.isPresent()) {
                        item.setDeptId(dep.get().getDeptId());
                    } else {
                        item.setDeptId("");
                    }
                    return item;
                })
                .map(item -> {
                    Optional<SystemGroup> group = groups.stream()
                            .filter(d -> d.getGroupName().equals(item.getGroupName()))
                            .findFirst();
                    if (group.isPresent()) {
                        item.setGroupId(group.get().getGroupId());
                    } else {
                        item.setGroupId("");
                    }
                    return item;
                })
                .map(item -> {
                    List<SystemRole> user_roles = roles.stream()
                            .filter(d -> d.getRoleName().equals(item.getRoles().get(0).getRoleName())).collect(toList());

                    if (user_roles.size() > 0) {
                        item.setRoles(user_roles);
                    } else {
                        item.setRoles(null);
                    }

                    return item;
                })
                .collect(toList());
    }
    @Transactional(rollbackFor = Exception.class)
    public SystemUser updatePassword(HttpServletRequest request, String newPass) {
        String token = UserUtils.getToken(request);
        SystemUser systemUser = JwtUtils.parseJWT(token, SystemUser.class, secret);
        if (StringUtils.contains(newPass," ")){
            throw new BusinessException(ExceptionInfoEnum.PASSWORD_BLANK_ERROR);
        }
        userMapper.updateUserPassword(UserUtils.md5DigestAsHex(newPass
                ,systemUser.getUsername()), systemUser.getUserId());
        systemUser.setPassword(null);
        return systemUser;
    }

    /**
     * 临时接口用来初始化用户密码
     * @param username
     */
    @Transactional(rollbackFor = Exception.class)
    public void  initPassword(String username){
        if (StringUtils.isNotBlank(username)){
            SystemUserBo byUserName = userMapper.findByUserName(username);
            if (byUserName != null) {
                userMapper.updateUserPassword(UserUtils.md5DigestAsHex(username
                        ,byUserName.getUsername()), byUserName.getUserId());
            }
        }else {
            throw new BusinessException("用户名为空或者不存在");
        }
    }

}

package com.aibaixun.gail.service.impl;

import com.aibaixun.basic.exception.BaseException;
import com.aibaixun.basic.result.BaseResultCode;
import com.aibaixun.gail.entity.*;
import com.aibaixun.gail.mapper.UserMapper;
import com.aibaixun.gail.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjhuang
 * @since 2022-01-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService, IAuthUserService {
    @Value("${gail.token-expired}")
    private Long tokenExpired;

    @Value("${gail.reflash-token-expired}")
    private Long reflashTokenExpired;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserGroupService userGroupService;

    @Autowired
    private IAuthLogService authLogService;

    @Override
    public AuthUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadUserByAuth(UserPrincipal.Type.USERNAME,username);
    }

    @Override
    public AuthUser loadUserByMobile(String mobile) throws UsernameNotFoundException {
        return loadUserByAuth(UserPrincipal.Type.MOBILE,mobile);
    }

    @Override
    public AuthUser loadUserByEmail(String email) throws UsernameNotFoundException {
        return loadUserByAuth(UserPrincipal.Type.EMAIL,email);
    }

    @Override
    public AuthUser loadUserByUserId(String userId) throws UsernameNotFoundException {
        return loadUserByAuth(UserPrincipal.Type.REFLASH,userId);
    }

    private AuthUser loadUserByAuth(UserPrincipal.Type type, String value) throws UsernameNotFoundException {
        //根据用户名查找用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        if (type==UserPrincipal.Type.USERNAME){
            queryWrapper.eq(User::getUsername,value);
        } else if (type==UserPrincipal.Type.MOBILE){
            queryWrapper.eq(User::getMobile,value);
        } else if (type==UserPrincipal.Type.EMAIL){
            queryWrapper.eq(User::getEmail,value);
        }else if (type==UserPrincipal.Type.REFLASH){
            queryWrapper.eq(User::getId,value);
        } else{
            throw new UsernameNotFoundException(value);
        }
        User user = getOne(queryWrapper);
        if (user==null){
            throw new UsernameNotFoundException("账号或密码错误");
        }
        //权限
        List<Role> roles = new ArrayList<>();
        if (user.getGroupId()!=null){
            roles = roleService.listRoleByUserGroup(user.getGroupId());
        }
        AuthUser authUser = new AuthUser(user, roles);
        //token有效时间
        authUser.setTokenExpired(System.currentTimeMillis()+(tokenExpired*1000));
        authUser.setReflashTokenExpired(System.currentTimeMillis()+(reflashTokenExpired*1000));
        return authUser;
    }

    @Override
    public IPage<User> page(Integer page, Integer pageSize, String groupId, String name, String tenantId) {
        return baseMapper.page(name,groupId,tenantId, Page.of(page,pageSize));
    }

    @Override
    public List<User> list(String name, String tenantId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        if (!StringUtils.isEmpty(name)){
            queryWrapper.likeRight(User::getRealname, name);
        }
        if (!StringUtils.isEmpty(tenantId)){
            queryWrapper.likeRight(User::getTenantId,tenantId);
        }
        queryWrapper.orderByDesc(User::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    public Boolean checkName(String id, String name, String currentTenantId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        if (!StringUtils.isEmpty(id)){
            queryWrapper.ne(User::getId, id);
        }
        queryWrapper.eq(User::getRealname,name);

        queryWrapper.eq(User::getTenantId, currentTenantId);

        queryWrapper.orderByDesc(User::getCreateTime);
        User user = getOne(queryWrapper);
        if (user==null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean checkSave(User user) throws BaseException {
        checkParam(user);
        save(user);
        return true;
    }

    @Override
    public Boolean checkUpdate(User user) throws BaseException {
        checkParam(user);
        saveOrUpdate(user);
        return true;
    }

    @Override
    public User info(String id) {
        User user = getById(id);
        if (user!=null&&user.getGroupId()!=null){
            UserGroup group = userGroupService.getById(user.getGroupId());
            if (group!=null){
                user.setGroupName(group.getName());
            }
        }
        return user;
    }

    @Override
    public Boolean del(List<String> userIds) {
        if (CollectionUtils.isEmpty(userIds)){
            return true;
        }

        //删除登录认知相关信息
        authLogService.delByUserIds(userIds);
        removeByIds(userIds);
        return true;
    }

    private void checkParam(User user) throws BaseException {
        if (StringUtils.isEmpty(user.getRealname())){
            throw new BaseException("用户名称为空", BaseResultCode.BAD_PARAMS);
        }
        if (StringUtils.isEmpty(user.getMobile())){
            throw new BaseException("用户电话为空", BaseResultCode.BAD_PARAMS);
        }
        if (StringUtils.isEmpty(user.getGroupId())){
            throw new BaseException("用户分组为空", BaseResultCode.BAD_PARAMS);
        }
        //重名校验
        if (!checkName(user.getId(),user.getRealname(),user.getTenantId())){
            throw new BaseException("用户名称已经存在", BaseResultCode.BAD_PARAMS);
        }
    }
}

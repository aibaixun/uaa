package com.aibaixun.gail.service.impl;

import com.aibaixun.gail.entity.AuthUser;
import com.aibaixun.gail.entity.Role;
import com.aibaixun.gail.entity.User;
import com.aibaixun.gail.entity.UserPrincipal;
import com.aibaixun.gail.mapper.UserMapper;
import com.aibaixun.gail.service.IAuthUserService;
import com.aibaixun.gail.service.IRoleService;
import com.aibaixun.gail.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
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
}

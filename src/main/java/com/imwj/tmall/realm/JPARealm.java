package com.imwj.tmall.realm;

import com.imwj.tmall.pojo.User;
import com.imwj.tmall.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author langao_q
 * @create 2020-01-10 14:21
 */
public class JPARealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 授权：已经认证过 进行授权操作
     * 这里没有权限操作 所以无需授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo s = new SimpleAuthorizationInfo();
        return s;
    }

    /**
     * 认证：对用户进行认证校验操作
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户名
        String username = token.getPrincipal().toString();

        //获取数据库中的密码
        User user = userService.getByName(username);
        String passwordInDB = user.getPassword();
        String salt = user.getSalt();

        //认证信息里存放账号密码，getName是当前Realm的继承方法，通常返回当前类名：databaseRealm
        //使用Shiro提供的 HashedCredentialsMatcher 帮我们做密码校验：
        //这样通过shiro.ini里配置的 HashedCredentialsMatcher 进行自动校验
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, passwordInDB, ByteSource.Util.bytes(salt), getName());
        return info;
    }
}

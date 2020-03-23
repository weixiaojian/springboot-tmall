# Lombok
* 1.使用maven引入依赖
```
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.16.20</version>
    <scope>provided</scope>
</dependency>
```

* 2.实体类中使用注解
```
@Data：注解在类上，会为类的所有属性自动生成setter/getter、equals、canEqual、hashCode、toString方法，
如为final属性，则不会为该属性生成setter方法
```

# axios表单数据提交&文件上传
数据和图片一起提交
* 1.前端准备
1.input标签准备，与getFile事件绑定
2.校验name和image不为空
3.axios 需要用如代码所示的 formData的形式来做，否则没法上传
4.调用 axios 的post方法进行实际的上传工作。
```
$(function () {
    //构建vue
    var vue = new Vue({
        el: '#workingArea',
        data: {
            uri: 'categories',
            beans: [],
            bean: {id: 0, name: ""},
            file: null
        },
        mounted: function () {//mounted标识此vue对象加载成功
            this.list(0);
        },
        methods: {
            getFile: function (event) {
                this.file = event.target.files[0];
            },
            add: function () {
                if(!checkEmpty(this.bean.name, '分类名称')){
                    return ;
                }
                if(!checkEmpty(this.file, '分类图片')){
                    return ;
                }
                var url = this.uri;
                //axios方式上传文件：formData
                var formData = new FormData();
                formData.append("image", this.file);
                formData.append("name", this.bean.name);
                //开始上传
                axios.post(url, formData).then(function (reponse) {
                    vue.list(0);//重置首页数据
                    vue.file = null;
                    vue.bean = {id: 0, name: ""};
                    $("#categoryPic").val();
                });
            }
        }
    })
})


<table class="addTable">
    <tr>
        <td>分类名称</td>
        <td><input  @keyup.enter="add" v-model.trim="bean.name" type="text" class="form-control"></td>
    </tr>
    <tr>
        <td>分类图片</td>
        <td>
            <input id="categoryPic" accept="image/*" type="file" name="image" @change="getFile($event)" />
        </td>
    </tr>
    <tr class="submitTR">
        <td colspan="2" align="center">
            <a href="#nowhere"  @click="add" class="btn btn-success">提交</a>
        </td>
    </tr>
</table>
```

* 2.后台处理
```
@RequestMapping(value = "/categories")
public Object add(Category category, MultipartFile image, HttpServletRequest request) throws IOException {
    Category bnea = categoryService.add(category);
    saveOrUpdateImageFile(bnea, image, request);//保存文件操作
    return category;
}
```

# 自定义登录拦截器
1、创建我们自己的拦截器类并实现 HandlerInterceptor 接口.  
2、创建一个Java类继承WebMvcConfigurerAdapter，并重写 addInterceptors 方法.  
2、实例化我们自定义的拦截器，然后将对像手动添加到拦截器链中（在addInterceptors方法中添加）.  

* 1.实现HandlerInterceptor
```
/**
 * @author langao_q
 * @create 2019-12-18 15:20
 * 登录拦截器：校验是否登录
 * 1.preHandle：在业务处理器处理请求之前被调用（常用）
 * 2.postHandle：在业务处理器处理请求执行完成后，生成视图之前执行（少）
 * 3.afterCompletion：在DispatcherServlet完全处理完请求后被调用，可用于清理资源等（少）
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HttpSession session = httpServletRequest.getSession();
        String contextPath = httpServletRequest.getServletContext().getContextPath();
        //需要登录的页面url
        String[] requireAuthPages = new String[]{
                "buy",
                "alipay",
                "payed",
                "cart",
                "bought",
                "confirmPay",
                "orderConfirmed",

                "forebuyone",
                "forebuy",
                "foreaddCart",
                "forecart",
                "forechangeOrderItem",
                "foredeleteOrderItem",
                "forecreateOrder",
                "forepayed",
                "forebought",
                "foreconfirmPay",
                "foreorderConfirmed",
                "foredeleteOrder",
                "forereview",
                "foredoreview"
        };

        //当前请求的url
        String uri = httpServletRequest.getRequestURI();

        //去掉指定字符串
        uri = StringUtils.remove(uri, contextPath + "/");
        String page = uri;

        //判断是否是属于需要登录的url
        if(beginWith(page, requireAuthPages)){
            //校验用户是否登录
            User user = (User) session.getAttribute("user");
            if(user == null){
                httpServletResponse.sendRedirect("login");
                return false;
            }
        }

        return true;
    }

    /**
     * 比较page是否存在于requiredAuthPages中
     * startsWith() 方法用于检测字符串是否以指定的前缀开始。
     * @param page
     * @param requiredAuthPages
     * @return
     */
    private boolean beginWith(String page, String[] requiredAuthPages){
        boolean result = false;
        for(String requiredAuthPage : requiredAuthPages){
            if(StringUtils.startsWith(page, requiredAuthPage)){
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
```

* 2.配置mvc
```
/**
 * @author langao_q
 * @create 2019-12-18 15:40
 * web mvc配置类
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    /**
     * 注册登录拦截器（实例化）
     * @return
     */
    @Bean
    public LoginInterceptor getLoginIntercepter(){
        return new LoginInterceptor();
    }

    /**
     * 指定拦截器的拦截url
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLoginIntercepter())
                .addPathPatterns("/**");
    }
}
```

# 事务
* 1.声明式事务
```
@Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
```
| 属性                  | 类型                                      |  描述   |
| :------:              | :-----:                                   | :----:  |
| value                 | String                                    |   	可选的限定描述符，指定使用的事务管理器    |
| propagation           |   enum: Propagation                       |   定义事务的生命周期，有REQUIRED、SUPPORTS、MANDATORY、REQUIRES_NEW、NOT_SUPPORTED、NEVER、NESTED，详细含义可查阅枚举类   |
| isolation             |    enum: Isolation                        |   可选的事务隔离级别设置，决定了事务的完整性  |
| readOnly              |   boolean                                 |   读写或只读事务，默认读写   |
| timeout               |    int (in seconds granularity)           |   事务超时时间设置  |
| rollbackFor           |   	Class对象数组，必须继承自Throwable   |   导致事务回滚的异常类数组   |
| rollbackForClassName  |    类名数组，必须继承自Throwable           |   导致事务回滚的异常类名字数组  |
| noRollbackFor         |   Class对象数组，必须继承自Throwable       |   不会导致事务回滚的异常类数组   |
| noRollbackForClassName|    类名数组，必须继承自Throwable           |   不会导致事务回滚的异常类名字数组  |

# springboot中使用shiro
* 1.认证和授权的JPARealm
```
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
```

* 2.shiro配置类
```
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @author langao_q
 * @create 2020-01-10 14:27
 * shiro配置类
 */
@Configuration
public class ShiroConfiguration {
    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean  = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        securityManager.setRealm(getJPARealm());
        return securityManager;
    }

    @Bean
    public JPARealm getJPARealm(){
        JPARealm myShiroRealm = new JPARealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }

    /**
     *  开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
```

* 3.注册
```
    /**
     * 注册操作
     * @param user
     * @return
     */
    @PostMapping(value = "/foreregister")
    public Object register(@RequestBody User user){
        //对用户名进行转义：防止恶意用户名
        user.setName(HtmlUtils.htmlEscape(user.getName()));
        //校验用户名是否存在：存在就提示前台
        boolean exis = userService.isExis(user.getName());
        if(exis){
            return Result.fail("用户名已经存在！");
        }
        //生成一个盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        //加密 加盐。md5加密 次数为两次
        String encodedPassword = new SimpleHash("md5",user.getPassword(),salt,2).toString();
        user.setPassword(encodedPassword);
        user.setSalt(salt);

        userService.add(user);

        return Result.success();
    }
```

* 4.登录
```
    /**
     * 用户登录
     * @param userParam
     * @param session
     * @return
     */
    @PostMapping(value = "/forelogin")
    public Object forelogin(@RequestBody User userParam, HttpSession session){
        //对用户名进行转义：防止恶意用户名
        userParam .setName(HtmlUtils.htmlEscape(userParam .getName()));
        //对用户名和密码进行校验
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userParam.getName(), userParam.getPassword());
        try {
            subject.login(token);
            User user = userService.getByName(userParam.getName());
            session.setAttribute("user", user);
            return Result.success();
        } catch (AuthenticationException e) {
            String message ="账号密码错误";
            return Result.fail(message);
        }
    }
```

* 5.退出登录
```
    /**
     * 退出登录
     * @return
     */
    @GetMapping("/forelogout")
    public String logout(HttpSession session ) {
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated())
            subject.logout();
        return "redirect:home";
    }
```

* 6.拦截器判断是否登录
```
/**
 * @author langao_q
 * @create 2019-12-18 15:20
 * 登录拦截器：校验是否登录
 * preHandle：在业务处理器处理请求之前被调用（常用）
 * postHandle：在业务处理器处理请求执行完成后，生成视图之前执行（少）
 * afterCompletion：在DispatcherServlet完全处理完请求后被调用，可用于清理资源等（少）
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HttpSession session = httpServletRequest.getSession();
        String contextPath = httpServletRequest.getServletContext().getContextPath();
        //需要登录的页面url
        String[] requireAuthPages = new String[]{
                "buy",
                "alipay",
                "payed",
                "cart",
                "bought",
                "confirmPay",
                "orderConfirmed",

                "forebuyone",
                "forebuy",
                "foreaddCart",
                "forecart",
                "forechangeOrderItem",
                "foredeleteOrderItem",
                "forecreateOrder",
                "forepayed",
                "forebought",
                "foreconfirmPay",
                "foreorderConfirmed",
                "foredeleteOrder",
                "forereview",
                "foredoreview"
        };

        //当前请求的url
        String uri = httpServletRequest.getRequestURI();

        //去掉指定字符串
        uri = StringUtils.remove(uri, contextPath + "/");
        String page = uri;

        //判断是否是属于需要登录的url
        if(beginWith(page, requireAuthPages)){
            //校验用户是否登录- shiro判断
            Subject subject = SecurityUtils.getSubject();
            if(!subject.isAuthenticated()) {
                httpServletResponse.sendRedirect("login");
                return false;
            }
        }

        return true;
    }

    /**
     * 比较page是否存在于requiredAuthPages中
     * startsWith() 方法用于检测字符串是否以指定的前缀开始。
     * @param page
     * @param requiredAuthPages
     * @return
     */
    private boolean beginWith(String page, String[] requiredAuthPages){
        boolean result = false;
        for(String requiredAuthPage : requiredAuthPages){
            if(StringUtils.startsWith(page, requiredAuthPage)){
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
```

* 7.判断是否登录
```
    /**
     * 校验用户是否登录
     * @param session
     * @return
     */
    @GetMapping(value = "/forecheckLogin")
    public Object checkLogin(HttpSession session){
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated())
            return Result.success();
        else
            return Result.fail("未登录");
    }
```

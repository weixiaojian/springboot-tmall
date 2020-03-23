package com.imwj.tmall.controller.web;

import com.imwj.tmall.pojo.*;
import com.imwj.tmall.service.*;
import com.imwj.tmall.util.Result;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author langao_q
 * @create 2019-12-03 16:18
 * 前台统一返回数据controller类
 */
@RestController
public class ForeRESTController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private PropertyValueService propertyValueService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderService orderService;

    /**
     * 首页商品集合数据
     * @return
     */
    @GetMapping("/forehome")
    public Object home(){
        List<Category> list = categoryService.list();
        productService.fill(list);
        productService.fillByRow(list);
        categoryService.removeCategoryFromProduct(list);
        return list;
    }

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

    /**
     * 商品详情数据
     * @param pid
     * @return
     */
    @GetMapping("/foreproduct/{pid}")
    public Object foreproduct(@PathVariable Integer pid){
        Product product = productService.get(pid);

        //赋值缩略图集合和详情图集合
        List<ProductImage> productSingleImages = productImageService.listSingleProductImages(product);
        List<ProductImage> productDetailImages = productImageService.listDetailProductImages(product);
        product.setProductSingleImages(productSingleImages);
        product.setProductDetailImages(productDetailImages);

        //商品属性值集合
        List<PropertyValue> pvs = propertyValueService.list(product);
        //商品评论集合
        List<Review> reviews = reviewService.list(product);

        //赋值销售总数和评论总数、赋值第一张图片
        productService.setSaleAndReviewNumber(product);
        productImageService.setFirstProdutImage(product);

        //返回数据：商品集合、属性集合、评论集合
        Map<String, Object> map = new HashMap<>();
        map.put("product", product);
        map.put("pvs", pvs);
        map.put("reviews", reviews);
        return Result.success(map);
    }

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

    /**
     * 分类商品集合
     * @param cid
     * @param sort
     * @return
     */
    @GetMapping(value = "/forecategory/{cid}")
    public Object forecategory(@PathVariable Integer cid, String sort){
        Category category = categoryService.get(cid);
        productService.fill(category);
        productService.setSaleAndReviewNumber(category.getProducts());
        categoryService.removeCategoryFromProduct(category);
        if(sort != null){
            switch (sort){
                //使用lambda表达式简化代码
                case "all":
                    Collections.sort(category.getProducts()
                            ,(p1,p2)->(p2.getReviewCount()*p2.getSaleCount() - p1.getReviewCount()*p1.getSaleCount()));
                    break;
                case "review":
                    Collections.sort(category.getProducts(),(p1,p2)->(p2.getReviewCount() - p1.getReviewCount()));
                    break;
                case "date":
                    Collections.sort(category.getProducts(),(p1,p2)->(p2.getCreateDate().compareTo(p1.getCreateDate())));
                    break;
                case "saleCount":
                    Collections.sort(category.getProducts(),(p1,p2)->(p2.getSaleCount() - p1.getSaleCount()));
                    break;
                case "price":
                    Collections.sort(category.getProducts(),(p1,p2)-> (int) (p2.getPromotePrice() - p2.getPromotePrice()));
                    break;
            }
        }
        return category;
    }


    /**
     * 搜索操作：未使用elasticsearch
     * @param keyword
     * @return
     */
    @PostMapping(value = "/foresearch")
    public Object search(String keyword){
        keyword = keyword==null?"":keyword;
        List<Product> products = productService.searh(keyword, 0, 20);
        productImageService.setFirstProdutImages(products);
        productService.setSaleAndReviewNumber(products);

        return products;
    }

    /**
     * 立即购买
     * @param pid
     * @param num
     * @param session
     * @return
     */
    @GetMapping("forebuyone")
    public Object buyone(int pid, int num, HttpSession session) {
        return buyoneAndAddCart(pid,num,session);
    }

    /**
     * 立即购买和加入购物车
     * 1. 获取参数pid  2. 获取参数num  3. 根据pid获取产品对象p  4. 从session中获取用户对象user
     *
     * 接下来就是新增订单项OrderItem， 新增订单项要考虑两个情况
     * a. 如果已经存在这个产品对应的OrderItem，并且还没有生成订单，即还在购物车中。 那么就应该在对应的OrderItem基础上，调整数量
     * a.1 基于用户对象user，查询没有生成订单的订单项集合  a.2 遍历这个集合  a.3 如果产品是一样的话，就进行数量追加  a.4 获取这个订单项的 id
     *
     * b. 如果不存在对应的OrderItem,那么就新增一个订单项OrderItem
     * b.1 生成新的订单项  b.2 设置数量，用户和产品  b.3 插入到数据库  b.4 获取这个订单项的 id  5.返回当前订单项id
     * 6. 在页面上，拿到这个订单项id，就跳转到 location.href="buy?oiid="+oiid;
     * @param pid
     * @param num
     * @param session
     * @return
     */
    public Object buyoneAndAddCart(int pid, int num, HttpSession session){
        Product product = productService.get(pid);
        User user = (User) session.getAttribute("user");

        int oiid = 0;
        boolean found = false;
        List<OrderItem> ois = orderItemService.listByUser(user);
        for(OrderItem oi : ois){
            if(pid == oi.getProduct().getId()){
                oi.setNumber(oi.getNumber() + num);
                orderItemService.update(oi);
                found = true;
                oiid = oi.getId();
                break;
            }
        }

        if(!found){
            OrderItem oi = new OrderItem();
            oi.setProduct(product);
            oi.setUser(user);
            oi.setNumber(num);
            orderItemService.save(oi);
            oiid = oi.getId();
        }

        return oiid;
    }

    /**
     * 结算页面
     * @param oiid
     * @param session
     * @return
     */
    @GetMapping(value = "/forebuy")
    public Object forebuy(String[] oiid, HttpSession session){
        List<OrderItem> list = new ArrayList<>();
        float total = 0;
        for(String oidstr : oiid){
            int i = Integer.parseInt(oidstr);
            OrderItem oi = orderItemService.get(i);
            total += oi.getProduct().getPromotePrice() * oi.getNumber();
            list.add(oi);
        }
        productImageService.setFirstProdutImagesOnOrderItems(list);

        session.setAttribute("ois", list);

        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("orderItems", list);
        return Result.success(map);
    }

    /**
     * 加入购物车
     * @param pid
     * @param num
     * @param session
     * @return
     */
    @GetMapping("/foreaddCart")
    public Object addCart(Integer pid, Integer num, HttpSession session){
        Object o = buyoneAndAddCart(pid, num, session);
        return Result.success();
    }

    /**
     * 购物车
     * @param session
     * @return
     */
    @GetMapping("/forecart")
    public Object forecart(HttpSession session){
        User user = (User) session.getAttribute("user");
        List<OrderItem> orderItems = orderItemService.listByUser(user);
        productImageService.setFirstProdutImagesOnOrderItems(orderItems);
        return orderItems;
    }

    /**
     * 前台同步购物车中商品数量
     * @param pid
     * @param num
     * @param session
     * @return
     */
    @GetMapping("/forechangeOrderItem")
    public Object changeOrderItem(Integer pid, Integer num, HttpSession session){
        User user =(User)  session.getAttribute("user");
        if(null==user)
            return Result.fail("未登录");

        List<OrderItem> ois = orderItemService.listByUser(user);
        for(OrderItem oi : ois){
            if(pid.equals(oi.getProduct().getId())){
                oi.setNumber(num);
                orderItemService.update(oi);
                break;
            }
        }
        return Result.success();
    }

    /**
     * 删除购物车中的商品
     * @param oiid
     * @param session
     * @return
     */
    @GetMapping("/foredeleteOrderItem")
    public Object deleteOrderItem(Integer oiid, HttpSession session){
        User user =(User)  session.getAttribute("user");
        if(null==user)
            return Result.fail("未登录");

        orderItemService.delete(oiid);
        return Result.success();
    }

    /**
     * 提交订单操作：生成order数据，更新orderitem数据
     * @param order
     * @param session
     * @return
     */
    @PostMapping("/forecreateOrder")
    public Object createOrder(@RequestBody Order order, HttpSession session){
        User user = (User) session.getAttribute("user");
        if(null==user)
            return Result.fail("未登录");
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);
        order.setOrderCode(orderCode);
        order.setCreateDate(new Date());
        order.setUser(user);
        order.setStatus(OrderService.waitPay);

        List<OrderItem> ois= (List<OrderItem>)  session.getAttribute("ois");
        float total = orderService.add(order, ois);

        Map<String,Object> map = new HashMap<>();
        map.put("oid", order.getId());
        map.put("total", total);

        return Result.success(map);
    }

    /**
     * 支付成功：更新订单状态、支付时间
     * @param oid
     * @return
     */
    @GetMapping("/forepayed")
    public Object payed(Integer oid){
        Order order = orderService.get(oid);
        order.setStatus(OrderService.waitDelivery);
        order.setPayDate(new Date());
        orderService.update(order);
        return order;
    }

    /**
     * 我的订单页显示数据
     * @param session
     * @return
     */
    @GetMapping("/forebought")
    public Object bought(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(null==user)
            return Result.fail("未登录");

        List<Order> orders = orderService.listByUserWithoutDelete(user);

        orderService.removeOrderFromOrderItem(orders);
        return orders;
    }

    /**
     * 确认收货
     * @param oid
     * @return
     */
    @GetMapping("/foreconfirmPay")
    public Object confirmPay(Integer oid){
        //根据id查询订单数据
        Order order = orderService.get(oid);
        //填充订单项数据
        orderItemService.fill(order);
        //计算总金额
        orderService.cacl(order);
        //移除订单项中的订单 防止json返回时重复递归
        orderService.removeOrderFromOrderItem(order);

        return order;
    }

    /**
     * 确认支付
     * @param oid
     * @return
     */
    @GetMapping("/foreorderConfirmed")
    public Object orderConfirmed(Integer oid){
        //3.1 获取参数oid
        //3.2 根据参数oid获取Order对象o
        Order order = orderService.get(oid);
        //3.3 修改对象o的状态为等待评价，修改其确认支付时间
        order.setStatus(OrderService.waitReview);
        order.setConfirmDate(new Date());
        //3.4 更新到数据库
        orderService.update(order);
        //3.5 返回成功
        return Result.success();
    }

    /**
     * 删除订单
     * @param oid
     * @return
     */
    @PutMapping("/foredeleteOrder")
    public Object deleteOrder(Integer oid){
        //根据oid查询订单数据
        Order order = orderService.get(oid);
        //更新订单状态为删除状态
        order.setStatus(OrderService.delete);
        //更新到数据库
        orderService.update(order);
        //返回成功
        return Result.success();
    }

    /**
     * 评价页面显示订单数据
     * @param oid
     * @return
     */
    @GetMapping("/forereview")
    public Object review(Integer oid){
        //3.1 获取参数oid
        //3.2 根据oid获取订单对象o
        Order order = orderService.get(oid);
        //3.3 为订单对象填充订单项
        orderItemService.fill(order);
        orderService.removeOrderFromOrderItem(order);
        //3.4 获取第一个订单项对应的产品,因为在评价页面需要显示一个产品图片，那么就使用这第一个产品的图片了。（这里没有对订单里的每种产品都评价，因为复杂度就比较高了，初学者学起来太吃力，有可能就放弃学习了，所以考虑到学习的平滑性，就仅仅提供对第一个产品的评价）
        Product p = order.getOrderItems().get(0).getProduct();
        //3.5 获取这个产品的评价集合
        List<Review> reviews = reviewService.list(p);
        //3.6 为产品设置评价数量和销量
        productService.setSaleAndReviewNumber(p);
        //3.7 把产品，订单和评价集合放在map上
        Map<String, Object> map = new HashMap<>();
        map.put("p", p);
        map.put("o", order);
        map.put("reviews", reviews);
        //3.8 通过 Result 返回这个map
        return Result.success(map);
    }

    /**
     * 提交评价
     * @param session
     * @param oid
     * @param pid
     * @param content
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
    @PostMapping("/foredoreview")
    public Object doreview(HttpSession session,int oid,int pid,String content){
        //1.1 获取参数oid
        //1.2 根据oid获取订单对象o
        Order order = orderService.get(oid);
        //1.3 修改订单对象状态
        order.setStatus(OrderService.finish);
        //1.4 更新订单对象到数据库
        orderService.update(order);
        //1.5 获取参数pid
        //1.6 根据pid获取产品对象
        Product product = productService.get(pid);
        //1.7 获取参数content (评价信息)
        //1.8 对评价信息进行转义，道理同注册ForeRESTController.register()
        content = HtmlUtils.htmlEscape(content);
        //1.9 从session中获取当前用户
        User user = (User) session.getAttribute("user");
        if(user == null)
            return Result.fail("用户未登录");
        //1.10 创建评价对象review
        Review review = new Review();
        //1.11 为评价对象review设置 评价信息，产品，时间，用户
        review.setContent(content);
        review.setProduct(product);
        review.setUser(user);
        review.setCreateDate(new Date());
        //1.12 增加到数据库
        reviewService.add(review);
        //1.13.返回成功
        return Result.success();
    }
}

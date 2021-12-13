package com.liangzhicheng;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.liangzhicheng.config.mvc.annotation.TestAnnotation;
import com.liangzhicheng.modules.controller.TestController;
import com.liangzhicheng.modules.entity.User;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.*;

public class RunTest {

    /**
     * Convert使用：类型转换工具类
     */
    @Test
    public void covert(){
        //转换为字符串
        int num = 1;
        String numStr = Convert.toStr(num);
        System.out.println("--- 1");
        System.out.println(numStr);

        //转换为指定类型数组
        String[] arrayStr = {"1", "2", "3", "4"};
        Integer[] arrayInt = Convert.toIntArray(arrayStr);
        System.out.println("--- 2");
        for(Integer n : arrayInt){
            System.out.println(n);
        }

        //转换为列表
        List<String> list = Convert.toList(String.class, arrayStr);
        System.out.println("--- 3");
        System.out.println(list);

        //转换为日期对象
        String dateStr = "2021-12-10";
        Date date = Convert.toDate(dateStr);
        System.out.println("--- 4");
        System.out.println(date);
    }

    /**
     * DateUtil使用：日期时间工具类
     */
    @Test
    public void dateUtil(){
        //当前时间
        Date date = DateUtil.date();

        //Calendar转Date
        date = DateUtil.date(Calendar.getInstance());
        System.out.println("--- 1");
        System.out.println(date);

        //时间戳转Date
        date = DateUtil.date(System.currentTimeMillis());
        System.out.println("--- 2");
        System.out.println(date);

        //自动识别格式转换
        String dateStr = "2021-12-11 09:00:00";
        date = DateUtil.parse(dateStr);
        System.out.println("--- 3");
        System.out.println(date);

        //自定义格式化转换
        date = DateUtil.parse(dateStr, "yyyy-MM-dd");
        System.out.println("--- 4");
        System.out.println(date);

        //格式化输出日期
        String format = DateUtil.format(date, "yyyy-MM-dd");
        System.out.println("--- 5");
        System.out.println(format);

        //获得年的部分
        int year = DateUtil.year(date);
        System.out.println("--- 6");
        System.out.println(year);

        //获得月份，从0开始计数，需要加1操作
        int month = DateUtil.month(date) + 1;
        System.out.println("--- 7");
        System.out.println(month);

        //获取某天的开始、结束时间
        Date beginOfDay = DateUtil.beginOfDay(date);
        Date endOfDay = DateUtil.endOfDay(date);
        System.out.println("--- 8");
        System.out.println(beginOfDay);
        System.out.println(endOfDay);

        //计算偏移后的日期时间
        Date newDate = DateUtil.offset(date, DateField.DAY_OF_MONTH, 2);
        //计算日期时间之间的偏移量
        long betweenDay = DateUtil.between(date, newDate, DateUnit.DAY);
        System.out.println("--- 9");
        System.out.println(newDate);
        System.out.println(betweenDay);
    }

    /**
     * StrUtil使用：字符串工具类
     */
    @Test
    public void strUtil(){
        //判断是否为空字符串
        String str = " ";
        boolean empty = StrUtil.isEmpty(str);
        boolean blank = StrUtil.isBlank(str);
        System.out.println("--- 1");
        System.out.println(empty);
        System.out.println(blank);

        boolean notEmpty = StrUtil.isNotEmpty(str);
        boolean notBlank = StrUtil.isNotBlank(str);
        System.out.println("--- 2");
        System.out.println(notEmpty);
        System.out.println(notBlank);

        //去除字符串的前后缀
        String removePrefix = StrUtil.removePrefix("a.jpg", "a.");
        System.out.println("--- 3");
        System.out.println(removePrefix);

        String removeSuffix = StrUtil.removeSuffix("a.jpg", ".jpg");
        System.out.println("--- 4");
        System.out.println(removeSuffix);

        //格式化字符串
        String template = "这只是个占位符:{}";
        String format = StrUtil.format(template, "我是占位符");
        System.out.println("--- 5");
        System.out.println(format);
    }

    /**
     * ReflectUtil使用：java反射工具类
     */
    @Test
    public void reflectUtil(){
        //获取某个类的所有方法
        Method[] methods = ReflectUtil.getMethods(User.class);
        System.out.println("--- 1");
        System.out.println(methods);

        //获取某个类的指定方法
        Method method = ReflectUtil.getMethod(User.class, "getId");
        System.out.println("--- 2");
        System.out.println(method);

        //使用反射来创建对象
        User user = ReflectUtil.newInstance(User.class);
        //反射执行对象的方法
        ReflectUtil.invoke(user, "setId", 1L);
    }

    /**
     * NumberUtil使用：数字处理工具类
     */
    @Test
    public void numberUtil(){
        //加减乘除操作
        double v1 = 1.234;
        double v2 = 1.234;
        double result;
        result = NumberUtil.add(v1, v2);
        System.out.println("--- 1");
        System.out.println(result);

        result = NumberUtil.sub(v1, v2);
        System.out.println("--- 2");
        System.out.println(result);

        result = NumberUtil.mul(v1, v2);
        System.out.println("--- 3");
        System.out.println(result);

        result = NumberUtil.div(v1, v2);
        System.out.println("--- 4");
        System.out.println(result);

        //保留两位小数
        BigDecimal round = NumberUtil.round(v1, 2);
        System.out.println("--- 5");
        System.out.println(round);

        //判断是否为对应类型
        String v3 = "1.234";
        boolean isNumber = NumberUtil.isNumber(v3);
        boolean isInteger = NumberUtil.isInteger(v3);
        boolean isDouble = NumberUtil.isDouble(v3);
        System.out.println("--- 6");
        System.out.println(isNumber);
        System.out.println(isInteger);
        System.out.println(isDouble);
    }

    /**
     * BeanUtil使用：JavaBean工具类
     */
    @Test
    public void beanUtil(){
        //用户对象
        User user = new User(1L, "诸神");

        //Bean转换为Map
        Map<String, Object> beanToMap = BeanUtil.beanToMap(user);
        System.out.println("--- 1");
        System.out.println(beanToMap);

        //Map转换为Bean
        User mapToBean = BeanUtil.toBean(beanToMap, User.class);
        System.out.println("--- 2");
        System.out.println(mapToBean);
        System.out.println(mapToBean.getName());

        //Bean属性拷贝
        User copyUser = new User();
        BeanUtil.copyProperties(user, copyUser);
        System.out.println("--- 3");
        System.out.println(copyUser);
        System.out.println(copyUser.getName());
    }

    /**
     * CollUtil使用：集合工具类
     */
    @Test
    public void collUtil(){
        //数组转换为列表
        String[] array = new String[]{"a", "b", "c", "d", "e"};
        List<String> list = CollUtil.newArrayList(array);
        System.out.println("--- 1");
        System.out.println(list);

        //join：数组转字符串时添加连接符号
        String joinStr = CollUtil.join(list, ",");
        System.out.println("--- 2");
        System.out.println(joinStr);

        //将以连接符号分隔的字符串再转换为列表
        List<String> splitList = StrUtil.split(joinStr, ',');
        System.out.println("--- 3");
        System.out.println(splitList);

        //判断列表是否为空
        boolean empty = CollUtil.isEmpty(splitList);
        System.out.println("--- 4");
        System.out.println(empty);
        boolean notEmpty = CollUtil.isNotEmpty(splitList);
        System.out.println("--- 5");
        System.out.println(notEmpty);

        //创建新的List、Set
        ArrayList<Object> newArrayList = CollUtil.newArrayList();
        HashSet<Object> newHashSet = CollUtil.newHashSet();
    }

    /**
     * MapUtil使用：Map工具类
     */
    @Test
    public void mapUtil(){
        //创建新的Map
        HashMap<Object, Object> map = MapUtil.newHashMap();

        //获取Map指定key的值，并转换为指定类型
        map.put("user", new User(1L, "诸神"));
        User user = MapUtil.get(map, "user", User.class);
        System.out.println("--- 1");
        System.out.println(user.getName());

        //获取Map指定key的值，并转换为字符串
        map.put("key1", user.getName());
        String key1 = MapUtil.getStr(map, "key1");
        System.out.println("--- 2");
        System.out.println(key1);

        //获取Map指定key的值，并转换为Integer
        map.put("key2", 1);
        Integer key2 = MapUtil.getInt(map, "key2");
        System.out.println("--- 3");
        System.out.println(key2);

        //获取Map指定key的值，并转换为Double
        map.put("key3", 1.66);
        Double key3 = MapUtil.getDouble(map, "key3");
        System.out.println("--- 4");
        System.out.println(key3);

        //获取Map指定key的值，并转换为Boolean
        map.put("key4", true);
        Boolean key4 = MapUtil.getBool(map, "key4");
        System.out.println("--- 5");
        System.out.println(key4);

        //获取Map指定key的值，并转换为Date
        map.put("key5", new Date());
        Date key5 = MapUtil.getDate(map, "key5");
        System.out.println("--- 6");
        System.out.println(key5);

        //将多个键值对加入到Map中
        map = MapUtil.of(new String[][]{
                {"key1", "value1"},
                {"key2", "value2"},
                {"key3", "value3"}
        });
        System.out.println("--- 7");
        System.out.println(map);

        //判断Map是否为空
        boolean empty = MapUtil.isEmpty(map);
        System.out.println("--- 8");
        System.out.println(empty);

        boolean notEmpty = MapUtil.isNotEmpty(map);
        System.out.println("--- 9");
        System.out.println(notEmpty);
    }

    /**
     * JSONUtil使用：JSON解析工具类
     */
    @Test
    public void jsonUtil(){
        //用户对象
        User user = new User(1L, "李宁");

        //对象转换为JSON字符串
        String jsonStr = JSONUtil.toJsonStr(user);
        System.out.println("--- 1");
        System.out.println(jsonStr);

        //JSON字符串转换为对象
        User userBean = JSONUtil.toBean(jsonStr, User.class);
        System.out.println("--- 2");
        System.out.println(userBean);

        //JSON字符串转换为JSONObject对象
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        System.out.println("--- 3");
        System.out.println(jsonObject);

        //列表转换为JSON字符串
        List<User> userList = new ArrayList<User>();
        userList.add(user);
        String jsonListStr = JSONUtil.toJsonStr(userList);
        System.out.println("--- 4");
        System.out.println(jsonListStr);

        //JSON字符串转化为列表
        userList = JSONUtil.toList(JSONUtil.parseArray(jsonListStr), User.class);
        System.out.println("--- 5");
        for(Iterator<User> users = userList.iterator(); users.hasNext();){
            User entity = users.next();
            System.out.println(entity.getName());
        }

        //JSONArray操作
        JSONArray jsonArray = JSONUtil.parseArray(jsonListStr);
        System.out.println("--- 6");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            Long id = object.getLong("id");
            String name = object.getStr("name");
            System.out.println("user：id=" + id + ", name=" + name);
        }

        //创建新的JSONArray、JSONObject
        JSONArray array = JSONUtil.createArray();
        JSONObject object = JSONUtil.createObj();

        //判断是否为JSON字符串，首尾都为大括号或中括号判定为JSON字符串
        boolean json1 = JSONUtil.isJson(jsonStr);
        System.out.println("--- 7");
        System.out.println(json1);
        boolean json2 = JSONUtil.isJson(jsonListStr);
        System.out.println("--- 8");
        System.out.println(json2);

        //判断是否为JSONObject字符串，首尾都为大括号判定为JSONObject字符串
        boolean json3 = JSONUtil.isJson(jsonStr);
        System.out.println("--- 9");
        System.out.println(json3);
        boolean json4 = JSONUtil.isJson(jsonListStr);
        System.out.println("--- 10");
        System.out.println(json4);

        //判断是否为JSONArray字符串，首尾都为中括号判定为JSONArray字符串
        boolean array1 = JSONUtil.isJsonArray(jsonStr);
        System.out.println("--- 11");
        System.out.println(array1);
        boolean array2 = JSONUtil.isJsonArray(jsonListStr);
        System.out.println("--- 12");
        System.out.println(array2);
    }

    /**
     * SecureUtil使用：加密解密工具类
     */
    @Test
    public void secureUtil() throws UnsupportedEncodingException {
        //MD5加密
        String str = "123456";
        String md5 = SecureUtil.md5(str);
        System.out.println("--- 1");
        System.out.println(md5.toUpperCase());

        //sha1加密
        String sha1 = SecureUtil.sha1(str);
        System.out.println("--- 2");
        System.out.println(sha1.toUpperCase());
    }

    /**
     * CaptchaUtil使用：图形验证码
     */
    public void captchaUtil(HttpServletRequest request, HttpServletResponse response){
        //生成验证码图片
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        try {
            request.getSession().setAttribute("CAPTCHA_KEY", lineCaptcha.getCode());
            response.setContentType("image/png"); //告诉浏览器输出内容为图片
            response.setHeader("Pragma", "No-cache"); //禁止浏览器缓存
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            lineCaptcha.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Validator使用：字段验证器
     */
    @Test
    public void validator() {
        //判断是否为生日日期
        boolean result = Validator.isBirthday("2020-07-05");
        System.out.println("--- 1");
        System.out.println(result);

        //判断是否为身份证号码（18位中国）
        result = Validator.isCitizenId("123456");
        System.out.println("--- 2");
        System.out.println(result);

        //判断是否为邮箱地址
        result = Validator.isEmail("yichengc3@163.com");
        System.out.println("--- 3");
        System.out.println(result);

        //判断是否为IPV4地址
        result = Validator.isIpv4("192.168.3.35");
        System.out.println("--- 4");
        System.out.println(result);

        //判断是否为手机号码
        result = Validator.isMobile("15888888888");
        System.out.println("--- 5");
        System.out.println(result);

        //判断是否为URL
        result = Validator.isUrl("http://www.baidu.com");
        System.out.println("--- 6");
        System.out.println(result);

        //判断是否为汉字
        result = Validator.isChinese("易成");
        System.out.println("--- 7");
        System.out.println(result);
    }

    /**
     * DigestUtil使用：摘要算法工具类
     */
    @Test
    public void DigestUtil(){
        String password = "123456";
        //计算MD5摘要值，并转为16进制字符串
        String result = DigestUtil.md5Hex(password);
        System.out.println("--- 1");
        System.out.println(result);

        //计算SHA-256摘要值，并转为16进制字符串
        result = DigestUtil.sha256Hex(password);
        System.out.println("--- 2");
        System.out.println(result);

        //生成Bcrypt加密后的密文，并校验
        result = DigestUtil.bcrypt(password);
        boolean check = DigestUtil.bcryptCheck(password, result);
        System.out.println("--- 3");
        System.out.println(check);
    }

    /**
     * HttpUtil使用：Http请求工具类
     */
    @Test
    public void httpUtil() throws IOException {
        //将URL参数解析为Map（也可以解析Post中的键值对参数）
        String urlParams = "http://localhost:8081/test/get?param1=1&param2=2";
        Map<String, String> decodeParamMap = HttpUtil.decodeParamMap(urlParams, Charset.forName("UTF-8"));
        System.out.println("--- 1");
        System.out.println(decodeParamMap);

        //对URL参数做编码，只编码键和值，提供的值可以是url附带参数，但是不能只是url
        String encodeParams = HttpUtil.encodeParams(urlParams, Charset.forName("UTF-8"));
        System.out.println("--- 2");
        System.out.println(encodeParams);

        //从Http连接的头信息中获得字符集，从ContentType中获取
        String charset = HttpUtil.getCharset("application/json;charset=UTF-8");
        System.out.println("--- 3");
        System.out.println(charset);

        //将Map形式的Form表单数据转换为Url参数形式，会自动url编码键和值
        HashMap<String, Object> paramsMap = MapUtil.newHashMap();
        paramsMap.put("key1", 1);
        paramsMap.put("key2", 2);
        String params = HttpUtil.toParams(paramsMap);
        System.out.println("--- 4");
        System.out.println(params);

        //从请求参数的body中判断请求的Content-Type类型
//        String requestBody = "";
//        String contentTypeByRequestBody = HttpUtil.getContentTypeByRequestBody(requestBody);
//        System.out.println("--- 5");
//        System.out.println(contentTypeByRequestBody);

        //从流中读取内容，首先尝试使用charset编码读取内容（如果为空默认UTF-8），如果isGetCharsetFromContent为true，则通过正则在正文中获取编码信息，转换为指定编码
//        URL url = new URL(urlParams);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        String content = HttpUtil.getString(connection.getInputStream(), Charset.forName("UTF-8"), false);
//        System.out.println("--- 6");

        //发送get请求
        urlParams = "http://localhost:8081/test/get/1/2";
        String response = HttpUtil.get(urlParams);
        System.out.println("--- 7");

        //发送get请求
        urlParams = "http://localhost:8081/test/get";
        response = HttpUtil.get(urlParams, paramsMap);
        System.out.println("--- 8");

        //发送post请求
        String url = "http://localhost:8081/test/post1";
        JSONObject obj = JSONUtil.createObj();
        obj.putOpt("key1", "hello hutool");
        response = HttpUtil.post(url, JSONUtil.toJsonStr(obj));
        System.out.println("--- 9");

        //发送post请求
        url = "http://localhost:8081/test/post2";
        response = HttpUtil.post(url, paramsMap);
        System.out.println("--- 10");
    }

    /**
     * AnnotationUtil使用：注解工具类
     */
    @Test
    public void annotationUtil(){
        //获取指定类、方法、字段、构造器上的注解列表
        Annotation[] annotationList = AnnotationUtil.getAnnotations(TestController.class, false);
        System.out.println("--- 1");
        System.out.println(annotationList);

        //获取指定类型注解
        TestAnnotation testAnnotation = AnnotationUtil.getAnnotation(TestController.class, TestAnnotation.class);
        System.out.println("--- 2");
        System.out.println(testAnnotation.value());

        //获取指定类型注解的值
        Object annotationValue = AnnotationUtil.getAnnotationValue(TestController.class, TestAnnotation.class);
        System.out.println("--- 3");
        System.out.println(annotationValue);
    }

    /**
     * ClassPathResource单一资源访问类：在classPath下查找文件
     */
    @Test
    public void classPath() throws IOException {
        //获取定义在src/main/resources文件夹中的配置文件
        ClassPathResource resource = new ClassPathResource("application.properties");
        Properties properties = new Properties();
        properties.load(resource.getStream());
        String port = properties.getProperty("server.port");
        System.out.println(properties);
        System.out.println(port);
    }

}

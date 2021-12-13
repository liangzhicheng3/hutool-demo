package com.liangzhicheng.modules.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.liangzhicheng.config.mvc.annotation.TestAnnotation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@TestAnnotation
@Controller
@RequestMapping(value = "/test")
public class TestController {

    @GetMapping(value = "/get/{param1}/{param2}")
    public void get(@PathVariable("param1") String param1,
                    @PathVariable("param2") String param2){
        int num1 = Integer.parseInt(param1);
        int num2 = Integer.parseInt(param2);
        System.out.println(num1 + num2);
    }

    @GetMapping(value = "/get")
    public void get(@RequestParam Map<String, Object> paramsMap){
        int num1 = MapUtil.getInt(paramsMap, "key1");
        int num2 = MapUtil.getInt(paramsMap, "key2");
        System.out.println(num1 + num2);
    }

    @PostMapping(value = "/post1")
    public void post(@RequestBody String key1){
        JSONObject jsonObject = JSONUtil.parseObj(key1);
        System.out.println(jsonObject.get("key1"));
    }

    @PostMapping(value = "/post2")
    public void post(@RequestParam Map<String, Object> paramsMap){
        int num1 = MapUtil.getInt(paramsMap, "key1");
        int num2 = MapUtil.getInt(paramsMap, "key2");
        System.out.println(num1 + num2);
    }

}

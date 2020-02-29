package com.gll.learn.demo.controller;

import com.gll.learn.demo.dataBase.PeopleRepository;
import com.gll.learn.demo.pojo.People;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("hello")
public class HelloController {
    Gson gson = new Gson();
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    PeopleRepository peopleRepository;

    @RequestMapping("/say")
    String home() {
        //System.out.println("get into");
        return "hello world";
    }

    @RequestMapping("/name/{name}")
    String hello(@PathVariable String name) {
        return "hello" + name;
    }

    @RequestMapping("/people/{id}")
    People getById(@PathVariable Long id) {
        People people = peopleRepository.findById(id);
        return people;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    People addPeople(HttpServletRequest request) {

        String jsonParamStr = getRequest(request);
        Map<String, String> peopleMap = gson.fromJson(jsonParamStr, new HashMap<String, String>().getClass());

        People peopleOne = new People();
        peopleOne.setAge(peopleMap.get("age"));
        peopleOne.setName(peopleMap.get("name"));
        peopleOne.setSex(peopleMap.get("sex"));
        peopleOne.setText(peopleMap.get("text"));

        People people = peopleRepository.save(peopleOne);
        return people;
    }

    //解析入参
    private String getRequest(HttpServletRequest request) {
        String jsonParamStr = "";
        try {
            request.setCharacterEncoding("UTF-8");
            InputStream in = request.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String s = "";
            StringBuffer jsonParam = new StringBuffer();
            while ((s = br.readLine()) != null) {
                jsonParam.append(s);
            }
            jsonParamStr = new String(jsonParam.toString().getBytes("UTF-8"));
        } catch (IOException e) {
            LOGGER.error("解析入参异常！");
        }finally {
            //后面可能关闭流
        }
        return jsonParamStr;
    }
}
package com.example.mybatis.controller;

import com.example.mybatis.entity.User;
import com.example.mybatis.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/findAll")
    public List<User> findAll(){
        return userMapper.findAll();
    }

    @RequestMapping("/findById")
    public User findById(String id){
        return userMapper.findById(Long.valueOf(id));
    }

    @RequestMapping("/insert")
    public String insert(@RequestBody User user){
        userMapper.insert(user);
        return "ok";
    }

    @RequestMapping("/update")
    public String update(@RequestBody User user){
        userMapper.update(user);
        return "ok";
    }

    @RequestMapping("/deleteById")
    public String deleteById(String id){
        userMapper.deleteById(Long.valueOf(id));
        return "ok";
    }
}
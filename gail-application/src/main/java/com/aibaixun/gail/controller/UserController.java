package com.aibaixun.gail.controller;


import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.gail.entity.User;
import com.aibaixun.gail.service.IUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hjhuang
 * @since 2022-01-06
 */
@Api("角色")
@RestController
@RequestMapping("/gail/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @ApiOperation("分页")
    @GetMapping(value = "/page")
    public JsonResult<IPage<User>> page(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        @RequestParam(value = "name", required = false) String name){
        return JsonResult.success(null);
    }

    @ApiOperation("列表")
    @GetMapping("/list")
    public JsonResult<List<User>> list(@RequestParam(value = "name",required = false) String name){
        return JsonResult.success(userService.list());
    }

    @ApiOperation("添加")
    @PostMapping
    public JsonResult<Boolean> add(User user){
        return JsonResult.success(userService.save(user));
    }

    @ApiOperation("修改")
    @PutMapping
    public JsonResult<Boolean> edit(User user){
        return JsonResult.success(userService.updateById(user));
    }

    @ApiOperation("删除")
    @DeleteMapping
    public JsonResult<Boolean> del(Long id){
        return JsonResult.success(userService.removeById(id));
    }

    @ApiOperation("详情")
    @GetMapping(value ="/info")
    public JsonResult<User> info(Long id){
        return JsonResult.success(userService.getById(id));
    }

}


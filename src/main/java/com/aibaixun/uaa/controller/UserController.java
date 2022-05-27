package com.aibaixun.uaa.controller;

import com.aibaixun.basic.exception.BaseException;
import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.uaa.entity.NameJson;
import com.aibaixun.uaa.entity.User;
import com.aibaixun.uaa.service.IUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hjhuang
 * @since 2022-01-06
 */
@Api(tags = "用户")
@RestController
@RequestMapping("/uaa/user")
public class UserController  extends BaseController{
    @Autowired
    private IUserService userService;

    @ApiOperation("分页")
    @GetMapping(value = "/page")
    public JsonResult<IPage<User>> page(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        @RequestParam(value = "groupId", required = false) String groupId,
                                        @RequestParam(value = "name", required = false) String name){
        return JsonResult.success(userService.page(page,pageSize,groupId,name,getCurrentTenantId()));
    }

    @ApiOperation("列表")
    @GetMapping("/list")
    public JsonResult<List<User>> list(@RequestParam(value = "name",required = false) String name){
        return JsonResult.success(userService.list(name,getCurrentTenantId()));
    }

    @ApiOperation("名称")
    @GetMapping("/name")
    public JsonResult<List<NameJson>> name(@RequestParam(value = "name", required = false) String name) {
        List<NameJson> names = userService.list(name, getCurrentTenantId()).stream().map(user -> new NameJson(user.getId(), user.getRealname())).collect(Collectors.toList());
        return JsonResult.success(names);
    }

    @ApiOperation("重名检查")
    @GetMapping("/checkName")
    public JsonResult<Boolean> checkName(@RequestParam(value = "id", required = false) String id, @RequestParam(value = "name", required = true) String name) {
        return JsonResult.success(userService.checkName(id, name, getCurrentTenantId()));
    }

    @ApiOperation("添加")
    @PostMapping
    public JsonResult<Boolean> add(User user) throws BaseException {
        return JsonResult.success(userService.checkSave(user));
    }

    @ApiOperation("修改")
    @PutMapping
    public JsonResult<Boolean> edit(User user) throws BaseException {
        return JsonResult.success(userService.checkUpdate(user));
    }

    @ApiOperation("删除")
    @DeleteMapping
    public JsonResult<Boolean> del(List<String> userIds){
        return JsonResult.success(userService.del(userIds));
    }

    @ApiOperation("详情")
    @GetMapping(value ="/info")
    public JsonResult<User> info(String id){
        return JsonResult.success(userService.info(id));
    }

}


package com.aibaixun.gail.controller;


import com.aibaixun.basic.exception.BaseException;
import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.gail.entity.App;
import com.aibaixun.gail.entity.NameJson;
import com.aibaixun.gail.entity.Permission;
import com.aibaixun.gail.entity.User;
import com.aibaixun.gail.service.IAppService;
import com.aibaixun.gail.service.IPermissionService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hjhuang
 * @since 2022-01-06
 */
@Api(tags = "权限")
@RestController
@RequestMapping("/gail/permission")
public class PermissionController {
    @Autowired
    private IPermissionService permissionService;

    @ApiOperation("分页")
    @GetMapping(value = "/page")
    public JsonResult<IPage<Permission>> page(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                              @RequestParam(value = "name", required = false) String name,
                                              @RequestParam(value = "appId", required = false) String appId) {
        return JsonResult.success(permissionService.page(page, pageSize, name, appId));
    }

    @ApiOperation("列表")
    @GetMapping("/list")
    public JsonResult<List<Permission>> list(@RequestParam(value = "name", required = false) String name,
                                             @RequestParam(value = "appId", required = false) String appId) {
        return JsonResult.success(permissionService.list(name, appId));
    }

    @ApiOperation("名称")
    @GetMapping("/name")
    public JsonResult<List<NameJson>> name(@RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "appId", required = false) String appId) {
        List<NameJson> names = permissionService.list(name, appId).stream().map(app -> new NameJson(app.getId(), app.getName())).collect(Collectors.toList());
        return JsonResult.success(names);
    }

    @ApiOperation("重名检查")
    @GetMapping("/checkName")
    public JsonResult<Boolean> checkName(@RequestParam(value = "id", required = false) String id, @RequestParam(value = "name", required = true) String name) {
        return JsonResult.success(permissionService.checkName(id, name));
    }

    @ApiOperation("添加")
    @PostMapping
    public JsonResult<Boolean> add(Permission permission) throws BaseException {
        return JsonResult.success(permissionService.checkSave(permission));
    }

    @ApiOperation("修改")
    @PutMapping
    public JsonResult<Boolean> edit(Permission permission) throws BaseException {
        return JsonResult.success(permissionService.checkUpdate(permission));
    }

    @ApiOperation("删除")
    @DeleteMapping
    public JsonResult<Boolean> del(List<String> permissionIds) {
        return JsonResult.success(permissionService.del(permissionIds));
    }

    @ApiOperation("详情")
    @GetMapping(value = "/info")
    public JsonResult<Permission> info(String id) {
        return JsonResult.success(permissionService.info(id));
    }
}


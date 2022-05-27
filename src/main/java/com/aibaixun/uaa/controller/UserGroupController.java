package com.aibaixun.uaa.controller;


import com.aibaixun.basic.exception.BaseException;
import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.uaa.entity.NameJson;
import com.aibaixun.uaa.entity.UserGroup;
import com.aibaixun.uaa.service.IUserGroupService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
@Api(tags = "用户组")
@RestController
@RequestMapping("/uaa/userGroup")
public class UserGroupController extends BaseController{
    @Autowired
    private IUserGroupService userGroupService;

    @ApiOperation("分页")
    @GetMapping(value = "/page")
    public JsonResult<IPage<UserGroup>> page(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                             @RequestParam(value = "name", required = false) String name) {
        IPage<UserGroup> pageData = userGroupService.page(page, pageSize, name, getCurrentTenantId());
        return JsonResult.success(pageData);
    }

    @ApiOperation("列表")
    @GetMapping("/list")
    public JsonResult<List<UserGroup>> list(@RequestParam(value = "name", required = false) String name) {
        return JsonResult.success(userGroupService.list(name, getCurrentTenantId()));
    }

    @ApiOperation("名称")
    @GetMapping("/name")
    public JsonResult<List<NameJson>> name(@RequestParam(value = "name", required = false) String name) {
        List<NameJson> names = userGroupService.list(name, getCurrentTenantId()).stream().map(group -> new NameJson(group.getId(), group.getName())).collect(Collectors.toList());
        return JsonResult.success(names);
    }

    @ApiOperation("重名检查")
    @GetMapping("/checkName")
    public JsonResult<Boolean> checkName(@RequestParam(value = "id", required = false) String id, @RequestParam(value = "name", required = true) String name) {
        return JsonResult.success(userGroupService.checkName(id, name, getCurrentTenantId()));
    }

    @ApiOperation("添加")
    @PostMapping
    public JsonResult<Boolean> add(UserGroup group) throws BaseException {
        return JsonResult.success(userGroupService.checkSave(group));
    }

    @ApiOperation("修改")
    @PutMapping
    public JsonResult<Boolean> edit(UserGroup group) throws BaseException {
        return JsonResult.success(userGroupService.checkUpdate(group));
    }

    @ApiOperation("删除")
    @DeleteMapping
    public JsonResult<Boolean> del(List<String> groupIds) {
        return JsonResult.success(userGroupService.del(groupIds));
    }

    @ApiOperation("详情")
    @GetMapping(value = "/info")
    public JsonResult<UserGroup> info(String id) {
        return JsonResult.success(userGroupService.info(id));
    }
}


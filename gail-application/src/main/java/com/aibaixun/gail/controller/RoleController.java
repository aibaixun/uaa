package com.aibaixun.gail.controller;


import com.aibaixun.basic.exception.BaseException;
import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.gail.entity.NameJson;
import com.aibaixun.gail.entity.Role;
import com.aibaixun.gail.service.IRoleService;
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
@Api(tags = "角色")
@RestController
@RequestMapping("/gail/role")
public class RoleController extends BaseController{
    @Autowired
    private IRoleService roleService;

    @ApiOperation("分页")
    @GetMapping(value = "/page")
    public JsonResult<IPage<Role>> page(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        @RequestParam(value = "appId", required = false) String appId,
                                        @RequestParam(value = "name", required = false) String name){
        return JsonResult.success(roleService.page(page,pageSize,appId,name,getCurrentTenantId()));
    }

    @ApiOperation("列表")
    @GetMapping("/list")
    public JsonResult<List<Role>> list(@RequestParam(value = "name",required = false) String name){
        return JsonResult.success(roleService.list(name,getCurrentTenantId()));
    }

    @ApiOperation("名称")
    @GetMapping("/name")
    public JsonResult<List<NameJson>> name(@RequestParam(value = "name", required = false) String name) {
        List<NameJson> names = roleService.list(name, getCurrentTenantId()).stream().map(role -> new NameJson(role.getId(), role.getName())).collect(Collectors.toList());
        return JsonResult.success(names);
    }

    @ApiOperation("重名检查")
    @GetMapping("/checkName")
    public JsonResult<Boolean> checkName(@RequestParam(value = "id", required = false) String id, @RequestParam(value = "name", required = true) String name) {
        return JsonResult.success(roleService.checkName(id, name, getCurrentTenantId()));
    }

    @ApiOperation("添加")
    @PostMapping
    public JsonResult<Boolean> add(Role role) throws BaseException {
        return JsonResult.success(roleService.checkSave(role));
    }

    @ApiOperation("修改")
    @PutMapping
    public JsonResult<Boolean> edit(Role role) throws BaseException {
        return JsonResult.success(roleService.checkUpdate(role));
    }

    @ApiOperation("删除")
    @DeleteMapping
    public JsonResult<Boolean> del(List<String> roleIds){
        return JsonResult.success(roleService.del(roleIds));
    }

    @ApiOperation("详情")
    @GetMapping(value ="/info")
    public JsonResult<Role> info(String id){
        return JsonResult.success(roleService.info(id));
    }

}


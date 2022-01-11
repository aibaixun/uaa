package com.aibaixun.gail.controller;


import com.aibaixun.basic.exception.BaseException;
import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.gail.entity.NameJson;
import com.aibaixun.gail.entity.Role;
import com.aibaixun.gail.entity.Tenant;
import com.aibaixun.gail.service.IRoleService;
import com.aibaixun.gail.service.ITenantService;
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
 *  前端控制器
 * </p>
 *
 * @author hjhuang
 * @since 2022-01-06
 */
@Api(tags = "租户")
@RestController
@RequestMapping("/gail/tenant")
public class TenantController {
    @Autowired
    private ITenantService tenantService;

    @ApiOperation("分页")
    @GetMapping(value = "/page")
    public JsonResult<IPage<Tenant>> page(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          @RequestParam(value = "name", required = false) String name){
        return JsonResult.success(tenantService.page(page,pageSize,name));
    }

    @ApiOperation("列表")
    @GetMapping("/list")
    public JsonResult<List<Tenant>> list(@RequestParam(value = "name",required = false) String name){
        return JsonResult.success(tenantService.list(name));
    }

    @ApiOperation("名称")
    @GetMapping("/name")
    public JsonResult<List<NameJson>> name(@RequestParam(value = "name", required = false) String name) {
        List<NameJson> names = tenantService.list(name).stream().map(tenant -> new NameJson(tenant.getId(), tenant.getNickname())).collect(Collectors.toList());
        return JsonResult.success(names);
    }

    @ApiOperation("重名检查")
    @GetMapping("/checkName")
    public JsonResult<Boolean> checkName(@RequestParam(value = "id", required = false) String id, @RequestParam(value = "name", required = true) String name) {
        return JsonResult.success(tenantService.checkName(id, name));
    }

    @ApiOperation("添加")
    @PostMapping
    public JsonResult<Boolean> add(Tenant tenant) throws BaseException {
        return JsonResult.success(tenantService.checkSave(tenant));
    }

    @ApiOperation("修改")
    @PutMapping
    public JsonResult<Boolean> edit(Tenant tenant) throws BaseException {
        return JsonResult.success(tenantService.checkUpdate(tenant));
    }

    @ApiOperation("删除")
    @DeleteMapping
    public JsonResult<Boolean> del(List<String> tenantIds){
        return JsonResult.success(tenantService.del(tenantIds));
    }

    @ApiOperation("详情")
    @GetMapping(value ="/info")
    public JsonResult<Tenant> info(String id){
        return JsonResult.success(tenantService.info(id));
    }

}


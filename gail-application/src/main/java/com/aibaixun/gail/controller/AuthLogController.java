package com.aibaixun.gail.controller;


import com.aibaixun.basic.exception.BaseException;
import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.gail.entity.AuthLog;
import com.aibaixun.gail.entity.NameJson;
import com.aibaixun.gail.entity.Tenant;
import com.aibaixun.gail.service.IAuthLogService;
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
@Api(tags = "认证记录")
@RestController
@RequestMapping("/gail/authLog")
public class AuthLogController extends BaseController{
    @Autowired
    private IAuthLogService authLogService;

    @ApiOperation("分页")
    @GetMapping(value = "/page")
    public JsonResult<IPage<AuthLog>> page(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           @RequestParam(value = "name", required = false) String name){
        return JsonResult.success(authLogService.page(page,pageSize,name,getCurrentTenantId()));
    }

    @ApiOperation("列表")
    @GetMapping("/list")
    public JsonResult<List<AuthLog>> list(@RequestParam(value = "name",required = false) String name){
        return JsonResult.success(authLogService.list(name,getCurrentTenantId()));
    }

    @ApiOperation("添加")
    @PostMapping
    public JsonResult<Boolean> add(AuthLog authLog) throws BaseException {
        return JsonResult.success(authLogService.checkSave(authLog));
    }

    @ApiOperation("修改")
    @PutMapping
    public JsonResult<Boolean> edit(AuthLog authLog) throws BaseException {
        return JsonResult.success(authLogService.checkUpdate(authLog));
    }

    @ApiOperation("删除")
    @DeleteMapping
    public JsonResult<Boolean> del(List<String> authLogIds){
        return JsonResult.success(authLogService.del(authLogIds));
    }

    @ApiOperation("详情")
    @GetMapping(value ="/info")
    public JsonResult<AuthLog> info(String id){
        return JsonResult.success(authLogService.info(id));
    }
}


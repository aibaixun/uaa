package com.aibaixun.uaa.controller;


import com.aibaixun.basic.exception.BaseException;
import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.uaa.entity.App;
import com.aibaixun.uaa.entity.NameJson;
import com.aibaixun.uaa.service.IAppService;
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
@Api(tags = "应用")
@RestController
@RequestMapping("/uaa/app")
public class AppController extends BaseController{
    @Autowired
    private IAppService appService;

    @ApiOperation("分页")
    @GetMapping(value = "/page")
    public JsonResult<IPage<App>> page(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       @RequestParam(value = "name", required = false) String name){
        return JsonResult.success(appService.page(page,pageSize,name));
    }

    @ApiOperation("列表")
    @GetMapping("/list")
    public JsonResult<List<App>> list(@RequestParam(value = "name",required = false) String name){
        return JsonResult.success(appService.list(name,getCurrentTenantId()));
    }

    @ApiOperation("名称")
    @GetMapping("/name")
    public JsonResult<List<NameJson>> name(@RequestParam(value = "name", required = false) String name) {
        List<NameJson> names = appService.list(name, getCurrentTenantId()).stream().map(app -> new NameJson(app.getId(), app.getName())).collect(Collectors.toList());
        return JsonResult.success(names);
    }

    @ApiOperation("重名检查")
    @GetMapping("/checkName")
    public JsonResult<Boolean> checkName(@RequestParam(value = "id", required = false) String id, @RequestParam(value = "name", required = true) String name) {
        return JsonResult.success(appService.checkName(id, name));
    }

    @ApiOperation("添加")
    @PostMapping
    public JsonResult<Boolean> add(App app) throws BaseException {
        return JsonResult.success(appService.checkSave(app));
    }

    @ApiOperation("修改")
    @PutMapping
    public JsonResult<Boolean> edit(App app) throws BaseException {
        return JsonResult.success(appService.checkUpdate(app));
    }

    @ApiOperation("删除")
    @DeleteMapping
    public JsonResult<Boolean> del(List<String> appIds){
        return JsonResult.success(appService.del(appIds));
    }

    @ApiOperation("详情")
    @GetMapping(value ="/info")
    public JsonResult<App> info(String id){
        return JsonResult.success(appService.info(id));
    }
}


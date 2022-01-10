package com.aibaixun.gail.controller;

import com.aibaixun.basic.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@Api("授权")
@RestController
@RequestMapping("/gail/token")
public class AuthController {
    @ApiOperation("权限")
    @GetMapping(value = "/permission")
    public JsonResult<Boolean> add(HttpServletRequest request){
        System.out.println(request.getHeader("authentication"));
        System.out.println(SecurityContextHolder.getContext().getAuthentication().toString());
        return JsonResult.success(true);
    }
}

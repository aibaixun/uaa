package com.aibaixun.uaa.controller;

import com.aibaixun.basic.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@Api(tags = "授权")
@RestController
@RequestMapping("/uaa/auth")
public class AuthController {
    @ApiOperation("权限校验")
    @GetMapping("/check")
    public JsonResult<Boolean> checkPath(@RequestParam("userid") String userid, @RequestParam("requestPath") String requestPath, @RequestParam("method") String method){

        return JsonResult.success(true);
    }
}

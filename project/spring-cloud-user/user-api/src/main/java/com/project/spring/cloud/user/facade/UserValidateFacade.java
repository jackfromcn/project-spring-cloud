package com.project.spring.cloud.user.facade;

import com.project.spring.cloud.user.dto.UserDto;
import com.util.msf.rpc.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wencheng on 2018/11/14.
 */
@Api(
        tags = {"用户系统-验证"}
)
public interface UserValidateFacade {

    @ApiOperation(value = "根据验证签获验证用户是否登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "验签", required = true, paramType = "query", dataTypeClass = String.class)})
    @GetMapping(value = "/vaildate/user/token", produces = "application/json")
    Result<Boolean> checkToken(@RequestHeader("accessToken") String accessToken);

    @ApiOperation(value = "根据验证签获取用户id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "验签", required = true, paramType = "query", dataTypeClass = String.class)})
    @GetMapping(value = "/vaildate/query/id", produces = "application/json")
    Result<Long> queryUserIdByToken(@RequestHeader("accessToken") String accessToken);


    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账户", required = true, paramType = "query", dataTypeClass = String.class),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataTypeClass = String.class)})
    @PostMapping(value = "/vaildate/login", produces = "application/json")
    Result<UserDto> login(@RequestParam("account") String account, @RequestParam("password") String password);

    @ApiOperation(value = "登出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "验签", required = true, paramType = "query", dataTypeClass = String.class)})
    @PostMapping(value = "/vaildate/logout", produces = "application/json")
    Result<Boolean> logout(@RequestHeader("accessToken") String accessToken);

}

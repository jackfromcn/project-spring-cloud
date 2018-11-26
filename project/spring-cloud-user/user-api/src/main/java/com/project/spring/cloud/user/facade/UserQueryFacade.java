package com.project.spring.cloud.user.facade;

import com.project.spring.cloud.user.dto.UserDto;
import com.util.msf.rpc.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wencheng on 2018/11/14.
 */
@Api(
        tags = {"用户系统-查询"}
)
@RequestMapping(value = "/base/query")
public interface UserQueryFacade {

    @ApiOperation(value = "根据userId查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, paramType = "path", dataTypeClass = Long.class)})
    @GetMapping(value = "/user/id/{id}", produces = "application/json")
    Result<UserDto> queryById(@PathVariable("id") Long id);

}

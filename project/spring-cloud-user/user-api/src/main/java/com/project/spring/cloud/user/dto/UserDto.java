package com.project.spring.cloud.user.dto;

import com.util.msf.rpc.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain=true)
@NoArgsConstructor
@Data
@ToString
public class UserDto extends BaseDto {
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty(value = "登陆名",required = true)
    private String loginName;

    @ApiModelProperty(value = "密码",required = true)
    private String password;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("验签")
    private String accessToken;

}
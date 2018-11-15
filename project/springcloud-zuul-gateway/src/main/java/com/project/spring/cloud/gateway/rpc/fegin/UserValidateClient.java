package com.project.spring.cloud.gateway.rpc.fegin;

import com.project.spring.cloud.user.facade.UserValidateFacade;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * Created by wencheng on 2018/11/15.
 */
@FeignClient("user")
public interface UserValidateClient extends UserValidateFacade {
}

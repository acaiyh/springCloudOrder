package cloud.api.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName: MemberFeign
 * @Auther: Administrator
 * @Date: 2019/7/26 0026 15:00
 * @Description:
 */
@FeignClient(name = "app-cloud-member")
public interface MemberFeign {

    @RequestMapping("/getMember")
    public String getMember();

}

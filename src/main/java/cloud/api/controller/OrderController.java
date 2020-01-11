package cloud.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @ClassName: OrderController
 * @Auther: Administrator
 * @Date: 2019/5/13 0013 18:08
 * @Description:
 */
@RestController
public class OrderController {

    @Value("${server.port}")
    private String port;

    //RestTemplate 需要实例化一个，在启动的时候，AppEureKaOrder 里面
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private MemberFeign memberFeign;

    @RequestMapping(value = "/")
    public String index(){
        return "This is order project " + port;
    }

    @RequestMapping(value = "/getMember")
    public String getMember(){
        return memberFeign.getMember();
    }

    /**
     * restful 调用
     * @author caiyunchun
     * 问题记录：
     *      此处用 getServiceUrl 方法获取到另一个服务的url, getServiceUrl方法里面用 DiscoveryClient 进行获取服务名称的url;
     *      然后用 restTemplate.getForObject(url,class) 方法进行调用其他服务的接口获取数据，
     *          会报错：java.lang.IllegalStateException: No instances available for ping-server 没有可用的服务
     * 解决方案：
     *      因为在 AppEureKaOrder 类中 实例化 restTemplate 的方法 的时候，加的注解是 @LoadBalanced：开启Robbin的负载均衡
     *      把它换成 @Primary：单一的
     * 原因：
     *      From google:
     *      When you use a @LoadBalanced RestTemplate the hostname needs to be a serviceId not an actual hostname.
     *      In your case, it's trying to find a eureka record for localhost and can't find one.
     *      See the documentation for how to use multiple RestTemplate objects, one load balanced, one not.
     */
    @GetMapping(value = "/getMember2Order", produces = {"application/json;charset=UTF-8"})
    public List getOrder2Member(){
        String serviceUrl = getServiceUrl("app-cloud-member") + "/getMember";
        // String serviceUrl = "http://app-cloud-member:8001/getMember";
        List result = restTemplate.getForObject(serviceUrl, List.class);
        System.out.println("订单服务调用会员服务 " + result.toArray().toString());
        return result;
    }

    private String getServiceUrl(String name){
        List<ServiceInstance> list = discoveryClient.getInstances(name);
        if(list != null && list.size() > 0){
            ServiceInstance serviceInstance = list.get(0);
            return serviceInstance.getUri().toString();
        }
        return null;
    }

}

package cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName: AppEureKaOrder
 * @Auther: Administrator
 * @Date: 2019/5/13 0013 18:05
 *
 * 添加 配置中心，一部分配置在 ConfigCenter中，如需添加配置或者修改配置，可在 ConfigCenter -> app-cloud-order-dev.yml 中配置
 * 配置完 需要调用接口刷新到最新配置 http://127.0.0.1:8000/actuator/refresh
 *
 * @Description:
 */
@SpringBootApplication
@EnableEurekaClient
//@EnableDiscoveryClient  //consul注册中心  zookeeper
@EnableFeignClients
@EnableHystrix
public class AppEureKaOrder {

    public static void main(String[] args) {
        SpringApplication.run(AppEureKaOrder.class,args);
    }

    //@Primary
    @Bean
    @LoadBalanced //@LoadBalanced 客户端开启负载均衡功能，ribbon
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

}

package com.zbdemo.hndl.config;

import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class SwaggerProvider implements SwaggerResourcesProvider {
    /**
     * swagger的api json文档路径
     */
    public static final String API_URI = "/v2/api-docs";
    /**
     * 发现功能的方法的名字，注册的服务会加入这个前缀
     */
    public static final String EUREKA_SUB_PFIX = "CompositeDiscoveryClient_";
    /**
     * 服务发现的路由处理器
     */
    private final DiscoveryClientRouteDefinitionLocator routeLocator;

    public SwaggerProvider(DiscoveryClientRouteDefinitionLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Override
    public List<SwaggerResource> get() {

        List<SwaggerResource> resources = new ArrayList<>();

        List<String> routes = new ArrayList<>();
        //从DiscoveryClientRouteDefinitionLocator 中取出routes，构造成swaggerResource
        routeLocator.getRouteDefinitions().subscribe(routeDefinition -> {
            resources.add(swaggerResource(
                    //获取id(服务注册的id)
                    routeDefinition.getId()
                            //去除CompositeDiscoveryClient_前缀
                            .substring(EUREKA_SUB_PFIX.length()),
                    //获取路由定义信息列表
                    routeDefinition.getPredicates()
                            //获取路径信息PredicateDefinition{name='Path', args={pattern=/byb-provider2/**}}
                            .get(0)
                            .getArgs()
                            //将pattern中的/**替换为服务swagger文档路径
                            .get("pattern")
                            .replace("/**", API_URI)));
        });
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {

        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}

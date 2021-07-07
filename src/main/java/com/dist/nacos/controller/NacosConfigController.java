package com.dist.nacos.controller;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-07-06 13:45
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Slf4j
@Api(tags = "Nacos 配置管理中心")
@RestController
@RequestMapping("/nacos/config")
public class NacosConfigController {

    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    @ApiOperation(value = "获取Nacos中Json配置文件", notes = "例如a.json")
    @GetMapping("json/v1")
    public Object getJson(@ApiParam(value = "命名空间ID", defaultValue = "frontend") @RequestParam String namespace,
                          @ApiParam(value = "组", defaultValue = "DEFAULT_GROUP") @RequestParam String group,
                          @ApiParam(value = "配置文件名称", defaultValue = "a.json") @RequestParam String dataid) throws Exception {

        if (!dataid.endsWith(".json")) {
            throw new RuntimeException("必须是以.json结尾的文件类型！");
        }
        String configContent = getConfigContent(namespace, group, dataid);
        log.info(configContent);
        JSONObject json = (JSONObject) JSONObject.parse(configContent);
        return json;
    }

    @ApiOperation(value = "获取Nacos中Text配置文件", notes = "例如a.text")
    @GetMapping("text/v1")
    public Object getText(@ApiParam(value = "命名空间ID", defaultValue = "frontend") @RequestParam String namespace,
                          @ApiParam(value = "组", defaultValue = "DEFAULT_GROUP") @RequestParam String group,
                          @ApiParam(value = "配置文件名称", defaultValue = "a.text") @RequestParam String dataid) throws Exception {
        if (!dataid.endsWith(".text")) {
            throw new RuntimeException("必须是以.text结尾的文件类型！");
        }
        String configContent = getConfigContent(namespace, group, dataid);
        log.info(configContent);
        return configContent;
    }

    @ApiOperation(value = "获取Nacos中Html配置文件", notes = "例如a.html")
    @GetMapping("html/v1")
    public Object getHtml(@ApiParam(value = "命名空间ID", defaultValue = "frontend") @RequestParam String namespace,
                          @ApiParam(value = "组", defaultValue = "DEFAULT_GROUP") @RequestParam String group,
                          @ApiParam(value = "配置文件名称", defaultValue = "a.html") @RequestParam String dataid) throws Exception {
        if (!dataid.endsWith(".html")) {
            throw new RuntimeException("必须是以.html结尾的文件类型！");
        }
        String configContent = getConfigContent(namespace, group, dataid);
        log.info(configContent);
        return configContent;
    }

    /**
     * 获取nacos config
     * @param namespace 例如 dev
     * @param group 默认 DEFAULT_GROUP
     * @param dataid 例如 test.json
     * @return 返回配置文件内容
     * @throws NacosException
     */
    private String getConfigContent(String namespace, String group, String dataid) throws NacosException {

        if (StringUtils.isEmpty(nacosConfigProperties.getServerAddr())) {
            throw new RuntimeException("serverAddr not is null !");
        }

        if (StringUtils.isEmpty(namespace)) {
            throw new RuntimeException("namespace not is null !");
        }

        if (StringUtils.isEmpty(group)) {
            throw new RuntimeException("dataid not is null !");
        }

        if (StringUtils.isEmpty(dataid)) {
            throw new RuntimeException("group not is null !");
        }

        Properties properties = new Properties();
        // nacos服务器地址
        properties.put(PropertyKeyConst.SERVER_ADDR, nacosConfigProperties.getServerAddr());
        // 配置中心的命名空间id
        properties.put(PropertyKeyConst.NAMESPACE, namespace);
        ConfigService configService = NacosFactory.createConfigService(properties);
        // 根据dataId、group定位到具体配置文件，获取其内容. 方法中的三个参数分别是: dataId, group, 超时时间
        return configService.getConfig(dataid, group, 3000L);
    }
}

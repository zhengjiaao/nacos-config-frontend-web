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
import org.apache.commons.io.FilenameUtils;
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
@RequestMapping("/nacos")
public class NacosConfigController {

    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    @ApiOperation(value = "获取Nacos配置文件", notes = "目前支持类型: *.json、*.html、*.text、*.xml、*.yaml、*.properties")
    @GetMapping("/get/config")
    public Object getConfig(@ApiParam(value = "命名空间ID", defaultValue = "frontend") @RequestParam String namespaceId,
                            @ApiParam(value = "组", defaultValue = "DEFAULT_GROUP") @RequestParam(required = false, defaultValue = "DEFAULT_GROUP") String group,
                            @ApiParam(value = "配置文件", defaultValue = "a.json") @RequestParam String dataId) throws Exception {
        //文件扩展名
        String extension = FilenameUtils.getExtension(dataId);
        if (StringUtils.isEmpty(extension)) {
            throw new RuntimeException("参数【dataid】的值，必须带扩展名 例：*.json,*.html,*.text。\n目前仅支持以上三种类型。");
        }

        String configContent = this.getConfigContent(namespaceId, group, dataId);
        log.info("文件：{}", dataId);
        log.info("数据：\n{}", configContent);

        if ("json".equals(extension)) {
            return JSONObject.parse(configContent);
        }

        if ("html".equals(extension)) {
            return configContent;
        }

        if ("text".equals(extension)) {
            return configContent;
        }

        //以下 xml、yaml、properties 需要前端转换 去掉换行符等
        if ("xml".equals(extension)) {
            return configContent;
        }

        if ("yaml".equals(extension)) {
            return configContent;
        }

        if ("properties".equals(extension)) {
            return configContent;
        }

        throw new RuntimeException("暂时不支持获取扩展名为: 【*." + extension + "】的配置文件");
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

        System.out.println(configService.getServerStatus());
        // 根据dataId、group定位到具体配置文件，获取其内容. 方法中的三个参数分别是: dataId, group, 超时时间
        return configService.getConfig(dataid, group, 3000L);
    }
}

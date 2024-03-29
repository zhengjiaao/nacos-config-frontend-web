package com.dist.nacos.interceptors;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-11-18 10:00
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：过滤器属性配置
 */
@Data
@ConfigurationProperties(prefix = "whitelist")
public class IPWhitelist {

    //默认不启用白名单
    private Boolean enabled = false;
    //白名单Ip列表
    private String[] ips;
}

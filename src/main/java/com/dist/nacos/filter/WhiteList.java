package com.dist.nacos.filter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-11-18 10:00
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：过滤器属性配置
 */
@Data
@ConfigurationProperties(prefix = "white.list")
public class WhiteList {

    //默认不启用白名单
    private Boolean enabled = false;
    //白名单Ip列表
    private List<String> ips;
}

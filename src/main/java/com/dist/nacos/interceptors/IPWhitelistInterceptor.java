package com.dist.nacos.interceptors;

import com.alibaba.nacos.common.utils.StringUtils;
import com.dist.nacos.uitl.IPUtils;
import org.jboss.logging.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-07-06 14:45
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class IPWhitelistInterceptor implements HandlerInterceptor {

    private static final Logger LOG = Logger.getLogger(IPWhitelistInterceptor.class.getName());

    private final Boolean enabled;

    private List<String> whitelistIps = new ArrayList<>();

    public IPWhitelistInterceptor(IPWhitelist ipWhitelist) {
        this.enabled = ipWhitelist.getEnabled();
        String[] ips = ipWhitelist.getIps();
        if (enabled && ips != null && 0 < ips.length) {
            this.whitelistIps = new ArrayList<>(Arrays.asList(ips));
        }
        this.whitelistIps.add("127.0.0.1");
        this.whitelistIps.add("0:0:0:0:0:0:0:1");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //过滤ip,若用户在白名单内，则放行
        String ipAddress = IPUtils.getRealIP(request);
        LOG.info("Client IP ADDRESS IS =>" + ipAddress);
        if (StringUtils.isBlank(ipAddress)) {
            return false;
        }

        //启动白名单
        if (enabled && !whitelistIps.isEmpty()) {
            boolean contains = whitelistIps.contains(ipAddress);
            if (!contains) {
                response.getWriter().append("The request is not authorized, please contact the administrator, Configure whitelist IP !");
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}

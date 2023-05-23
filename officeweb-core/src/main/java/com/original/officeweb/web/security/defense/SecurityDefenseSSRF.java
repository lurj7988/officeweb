package com.original.officeweb.web.security.defense;

import com.epoint.boot.web.security.RequestParameterHandler;
import com.epoint.boot.web.security.RequestRejectedException;
import com.epoint.boot.web.security.SecurityProperties;
import com.epoint.boot.web.security.defense.AbstractSecurityDefense;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * SSRF防御：通过限制访问地址来防御
 *
 * @author lurj
 */
@Component
public class SecurityDefenseSSRF extends AbstractSecurityDefense {

    private final SecurityProperties securityProperties;

    public SecurityDefenseSSRF(List<RequestParameterHandler> handlers, SecurityProperties securityProperties) {
        super(handlers);
        this.securityProperties = securityProperties;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String host = getHost((HttpServletRequest) request);
        if (host != null && !securityProperties.getTrusthost().isEmpty()
                && !securityProperties.getTrusthost().contains(host)
                && !host.equals(request.getServerName())) {
            throw new RequestRejectedException("The request was rejected because the domain " + host + " is untrusted.");
        }
        // 如果没有配host限制则判断只有相同host才能访问
        if (host != null && securityProperties.getTrusthost().isEmpty() && !host.equals(request.getServerName())) {
            throw new RequestRejectedException("The request was rejected because the domain " + host + " is untrusted.");
        }
        filterChain.doFilter(request, response);
    }

    private String getHost(HttpServletRequest request) {
        try {
            Map<String, String[]> map = getParameterMap(request);
            String[] values = map.get("furl");
            if (values != null && values.length > 0) {
                String furl = values[0];
                URL url = new URL(furl);
                return url.getHost().toLowerCase();
            }
        } catch (MalformedURLException ignored) {
        }
        return null;
    }
}

package cn.telecom.spring.web.filter;

import cn.telecom.commons.data.UserSession;
import cn.telecom.commons.data.vo.Results;
import cn.telecom.spring.web.HttpHeaderConstants;
import com.google.common.base.Strings;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.function.Function;

public class UserSessionFilter implements Filter {

    private final Function<String, UserSession> userSessionFunction;
    private final Runnable afterFilterRunner;

    public UserSessionFilter(final Function<String, UserSession> userSessionFunction,
                             final Runnable afterFilterRunner) {
        this.userSessionFunction = userSessionFunction;
        this.afterFilterRunner = afterFilterRunner;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest req && response instanceof HttpServletResponse resp) {
            this.doFilter0(req, resp, chain);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void doFilter0(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
        String token = req.getHeader(HttpHeaderConstants.AUTHORIZATION_KEY);
        if (Strings.isNullOrEmpty(token)) {
            chain.doFilter(req, resp);
            return;
        }
        UserSession userSession;
        try {
            userSession = this.userSessionFunction.apply(token);
        } catch (Exception e) {
            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
            resp.getOutputStream().write(Results.toJson(Results.fail("bad token:" + token)).getBytes());
            return;
        }
        req.setAttribute(UserSession.KEY, userSession);
        chain.doFilter(req, resp);
        this.afterFilterRunner.run();
    }
}

package com.springboot.app.controller;

import com.springboot.app.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.payos.PayOS;

@Controller
@RequiredArgsConstructor
public class CheckoutController {
    private final OrderService orderService;


    @RequestMapping(value = "/pay/success/{orderCode}")
    public String Success(@PathVariable("orderCode") Long orderCode) throws Exception {
        orderService.recharge(orderCode);
        return "success";
    }

    @RequestMapping(value = "/pay/cancel/{orderCode}")
    public String Cancel(@PathVariable("orderCode") Long orderCode) {
        orderService.cancel(orderCode);
        return "cancel";
    }



    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        String url = scheme + "://" + serverName;
        if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
            url += ":" + serverPort;
        }
        url += contextPath;
        return url;
    }
}
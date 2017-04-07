package com.by_syk.schttableserver.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.by_syk.schttableserver.bean.ResResBean;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 404 不经过此处
 */
public class GlobalExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request,
            HttpServletResponse response, Object obj, Exception exception) {
        System.out.println("GlobalExceptionHandler - resolveException");
        
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        try {
            ResResBean<String> bean = new ResResBean.Builder<String>()
                    .status(ResResBean.STATUS_ERROR)
                    .msg(exception.getMessage())
                    .build();
            response.getWriter().write((new ObjectMapper()).writeValueAsString(bean));
        } catch (Exception e) {
           e.printStackTrace();
        }
        
        return new ModelAndView();
    }
}

package com.hengxin.platform.common.util.web;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hengxin.platform.common.util.WebUtil;

public class ContentTypeNegotiatingJsonHttpMessageConverter implements HttpMessageConverter<Object> {
    private final MappingJacksonHttpMessageConverter converter;

    public ContentTypeNegotiatingJsonHttpMessageConverter(MappingJacksonHttpMessageConverter converter) {
        this.converter = converter;
    }

    @SuppressWarnings("deprecation")
    public ContentTypeNegotiatingJsonHttpMessageConverter() {
        this(new MappingJacksonHttpMessageConverter());
        converter.setObjectMapper(WebUtil.getObjectMapper());
    }

    @Override
    public Object read(Class<?> clazz, HttpInputMessage inputMessage) throws IOException {
        return converter.read(clazz, inputMessage);
    }

    @Override
    public void write(Object o, MediaType contentType, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {
        ServletServerHttpRequest request = request();
        if (contentType.includes(MediaType.APPLICATION_JSON)
                && !request.getHeaders().getAccept().contains(MediaType.APPLICATION_JSON)) {
            contentType = MediaType.TEXT_PLAIN;
        }
        converter.write(o, contentType, outputMessage);
    }

    private ServletServerHttpRequest request() {
        return new ServletServerHttpRequest(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return converter.canRead(clazz, mediaType);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return converter.canWrite(clazz, mediaType);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return converter.getSupportedMediaTypes();
    }
}

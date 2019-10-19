package com.aaroom.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 很多用户会从excel等文件中拷贝数据到网页输入框， 会留下\v等不可见字符。 导致json序列化失败。 考虑到其它数据来源， 这里做一个统一的兼容处理。
 *
 * @author Scorpio
 */
public class FastJsonHttpMessageConverter extends com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter {
    private static final Logger LOG = LoggerFactory.getLogger(FastJsonHttpMessageConverter.class);

/*
    static {
        ParserConfig.getGlobalInstance().putDeserializer(EntityView.class, new EntityViewDeserializer());
    }
*/

    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {
        OutputStream out = outputMessage.getBody();

        // TODO: 在fastjson 1.1.39版本中， 如果输入中包括了c2a0, fffe字符， 就可能出问题

        String text = JSON.toJSONString(obj, super.getFeatures());
        byte[] bytes = text.replace("\\v", "\\n")
                .replace("\\u0000", " ")
                .getBytes(super.getCharset());
        out.write(bytes);
    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        InputStream in = inputMessage.getBody();

        byte[] buf = new byte[1024];
        for (; ; ) {
            int len = in.read(buf);
            if (len == -1) {
                break;
            }

            if (len > 0) {
                baos.write(buf, 0, len);
            }
        }


        byte[] bytes = baos.toByteArray();
        return JSON.parseObject(bytes, 0, bytes.length, getCharset().newDecoder(), clazz);
    }

 /*   *//**
     * 自定义EntityView的序列号，根据属性model决定反序列化后的具体子类型。
     *//*
    private static class EntityViewDeserializer implements ObjectDeserializer {

        @Override
        public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
            JSONObject data = parser.parseObject();
            String model = data.getString("model");
            if ("User".equals(model)) {
                return (T) JSON.parseObject(JSON.toJSONString(data), UserView.class);
            } else if ("startup".equals(model)) {
                return (T) JSON.parseObject(JSON.toJSONString(data), StartupView.class);
            } else if ("rel".equals(model)) {
                return (T) JSON.parseObject(JSON.toJSONString(data), RelView.class);
            } else if ("Tag".equals(model)) {
                return (T) JSON.parseObject(JSON.toJSONString(data), TagView.class);
            } else {
                throw new UnknownFormatConversionException("entity model");
            }
        }

        @Override
        public int getFastMatchToken() {
            return 0;
        }
    }*/
}

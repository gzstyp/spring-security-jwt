package com.fwtai.tool;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 接收和响应客户端
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-04-28 1:20
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@Component
public class ToolClient{

    public void responseJson(final String json,final HttpServletResponse response){
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control","no-cache");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(json);
            writer.flush();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            if(writer != null){
                writer.close();
            }
        }
    }
}
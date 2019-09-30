package com.wjup.shorturl.controller;

import com.alibaba.fastjson.JSONObject;
import com.wjup.shorturl.entity.UrlEntity;
import com.wjup.shorturl.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.DateUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

/**
 * Create by wjup on 2019/9/29 11:33
 * <p>
 * 短网址生成项目
 */

@Controller
public class UrlController {

    @Autowired
    private UrlService urlService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 创建短链接
     *
     * @param longUrl 原地址
     * @param viewPwd 访问密码
     * @param request 请求
     * @return json
     */
    @RequestMapping("/create")
    @ResponseBody
    public String creatShortUrl(String longUrl, String viewPwd, HttpServletRequest request) {
        JSONObject json = new JSONObject();
        String[] split = longUrl.split("\n|\r");
        StringBuffer msg = new StringBuffer();

        for (int i = 0; i < split.length; i++) {
            UrlEntity urlEntity = new UrlEntity();

            if (!split[i].contains("https://") && !split[i].contains("http://")) {
                split[i] = "http://" + split[i];
            }

            String shortUrlId = getStringRandom(6);
            urlEntity.setShortUrlId(shortUrlId);
            urlEntity.setUuid(UUID.randomUUID().toString());
            urlEntity.setLongUrl(split[i]);
            urlEntity.setCreateTime(DateUtils.format(new Date(), "yyyy-MM-dd HH-mm-ss", Locale.SIMPLIFIED_CHINESE));
            urlEntity.setViewPwd(viewPwd);

            int flag = urlService.createShortUrl(urlEntity);

            String toUrl = "/";
            int serverPort = request.getServerPort();
            if (serverPort == 80 || serverPort == 443) {
                toUrl = request.getScheme() + "://" + request.getServerName();
            } else {
                toUrl = request.getScheme() + "://" + request.getServerName() + ":" + serverPort;
            }

            if (flag > 0) {
                msg.append(toUrl + "/" + shortUrlId + "<br>");
            }
        }

        json.put("shortUrl", msg);
        return json.toJSONString();
    }

    /**
     * 访问短链接
     *
     * @param shortUrlId 短网址id
     * @param response   响应
     * @param request    请求
     * @throws ServletException 异常捕获
     * @throws IOException      异常捕获
     */
    @RequestMapping(value = "/{shortUrlId}")
    public void view(@PathVariable("shortUrlId") String shortUrlId, HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {

        UrlEntity urlEntity = urlService.findByShortUrlId(shortUrlId);
        if (urlEntity != null) {
            if (urlEntity.getViewPwd() != null && !"".equals(urlEntity.getViewPwd())) {
                request.setAttribute("shortUrlId", shortUrlId);
                request.getRequestDispatcher("/viewPwd").forward(request, response);
            } else {
                urlService.updateShortUrl(shortUrlId);
                response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                response.setHeader("Location", urlEntity.getLongUrl());
            }
        } else {
            request.getRequestDispatcher("/noPage").forward(request, response);
        }
    }

    /**
     * 没有该请求跳转到指定页面
     *
     * @return page
     */
    @RequestMapping("/noPage")
    public String noPage() {

        return "noPage";
    }

    /**
     * 有密码打开输入密码页面
     *
     * @return html
     */
    @RequestMapping("/viewPwd")
    public String viewPwd(HttpServletRequest request, Model model) {
        String shortUrlId = request.getAttribute("shortUrlId").toString();
        model.addAttribute("shortUrlId", shortUrlId);
        return "viewPwd";
    }

    /**
     * 验证密码是否正确
     *
     * @param viewPwd    密码
     * @param shortUrlId 短址id
     */
    @RequestMapping("/VerifyPwd")
    @ResponseBody
    public String VerifyPwd(String viewPwd, String shortUrlId) {
        UrlEntity urlEntity = urlService.findByPwd(viewPwd, shortUrlId);

        JSONObject jsonObject = new JSONObject();
        if (urlEntity != null) {
            urlService.updateShortUrl(shortUrlId);
            jsonObject.put("longUrl", urlEntity.getLongUrl());
            jsonObject.put("flag", true);
        } else {
            jsonObject.put("flag", false);
        }
        return jsonObject.toJSONString();
    }


    /**
     * 生成随机数字和字母
     *
     * @param length 生成长度
     * @return shortUrlId
     */
    private String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }


}

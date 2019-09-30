package com.wjup.shorturl.service.serviceimpl;

import com.wjup.shorturl.entity.UrlEntity;
import com.wjup.shorturl.mapper.UrlMapper;
import com.wjup.shorturl.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.DateUtils;

import java.util.Date;
import java.util.Locale;

/**
 * Create by wjup on 2019/9/29 11:34
 */

@Service
public class UrlServiceImpl implements UrlService {

    @Autowired
    private UrlMapper urlMapper;

    @Override
    public int createShortUrl(UrlEntity urlEntity) {
        return urlMapper.createShortUrl(urlEntity);
    }

    @Override
    public UrlEntity findByShortUrlId(String shortUrlId) {
        return urlMapper.findByShortUrlId(shortUrlId);
    }

    @Override
    public void updateShortUrl(String shorlUrlId) {
        String nowDate = DateUtils.format(new Date(), "yyyy-MM-dd HH-mm-ss", Locale.SIMPLIFIED_CHINESE);
        urlMapper.updateShortUrl(shorlUrlId,nowDate);
    }

    @Override
    public UrlEntity findByPwd(String viewPwd, String shortUrlId) {
        return urlMapper.findByPwd(viewPwd,shortUrlId);
    }


}

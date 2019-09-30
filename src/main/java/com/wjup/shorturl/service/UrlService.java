package com.wjup.shorturl.service;

import com.wjup.shorturl.entity.UrlEntity;

/**
 * Create by wjup on 2019/9/29 11:34
 */

public interface UrlService {

    int createShortUrl(UrlEntity urlEntity);

    UrlEntity findByShortUrlId(String shortUrl);

    void updateShortUrl(String shorlUrlId);

    UrlEntity findByPwd(String viewPwd, String shortUrlId);

}

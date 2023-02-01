package com.anyqn.mastodon.pingen.configurations;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

//Autoconfiguration will find magically Ehcache config and provide CacheManager
@Configuration
@EnableCaching
public class CachingConfig {
}
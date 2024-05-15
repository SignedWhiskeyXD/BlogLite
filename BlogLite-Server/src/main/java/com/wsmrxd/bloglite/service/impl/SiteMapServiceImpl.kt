package com.wsmrxd.bloglite.service.impl

import com.wsmrxd.bloglite.mapping.BlogMapper
import com.wsmrxd.bloglite.service.SiteMapService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class SiteMapServiceImpl : SiteMapService {

    @Autowired
    private lateinit var blogMapper: BlogMapper

    @Value("\${myConfig.domain}")
    private lateinit var domain: String

    private var siteMapText: String = ""

    override fun getSiteMapText(): String {
        return siteMapText.ifBlank { loadSiteMapText() }
    }

    @Scheduled(cron = "0 0 0,12 * * *")
    fun loadSiteMapText(): String {
        val blogIDs = blogMapper.selectAllBlogID()
        var ret = ""

        blogIDs.forEach { ret += "https://$domain/blog/$it\n" }

        siteMapText = ret
        return ret
    }
}

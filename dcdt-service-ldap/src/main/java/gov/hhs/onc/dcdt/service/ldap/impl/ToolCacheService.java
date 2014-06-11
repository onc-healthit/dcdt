package gov.hhs.onc.dcdt.service.ldap.impl;

import java.util.Set;
import javax.annotation.Nullable;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.apache.directory.server.core.api.CacheService;

public class ToolCacheService extends CacheService {
    private Set<String> disabledCacheNames;

    public ToolCacheService(CacheManager cacheManager, Set<String> disabledCacheNames) {
        super(cacheManager);

        this.disabledCacheNames = disabledCacheNames;
    }

    @Nullable
    @Override
    public Cache getCache(String cacheName) {
        return (!this.disabledCacheNames.contains(cacheName) ? super.getCache(cacheName) : null);
    }

    public Set<String> getDisabledCacheNames() {
        return this.disabledCacheNames;
    }
}

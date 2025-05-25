package searchLayer;

import bussinessLayer.IndexerConfig;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class CachedSearcherProxy implements SearchService {
    private RealFileSearcher realSearcher;
    private Map<String, String> cache;
    private int cacheSize;

    public CachedSearcherProxy() {
        this.realSearcher = new RealFileSearcher();
        this.cacheSize = IndexerConfig.getSearchCacheSize();
        this.cache = new LinkedHashMap<>(cacheSize, 0.75f, true);
    }

    @Override
    public String getSearchResults(String query) {
        if (cache.containsKey(query)) {
            return "Cache hit!\n" + cache.get(query);
        }

        String result = realSearcher.getSearchResults(query);
        if (cache.size() >= cacheSize) {
            Iterator<String> it = cache.keySet().iterator();
            if (it.hasNext()) {
                it.next();
                it.remove();
            }
        }
        cache.put(query, result);
        return result;
    }

    public Map<String, String> getCache() {
        return cache;
    }

}


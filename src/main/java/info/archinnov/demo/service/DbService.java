package info.archinnov.demo.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import info.archinnov.achilles.persistence.PersistenceManager;
import info.archinnov.achilles.type.TypedMap;
import info.archinnov.demo.model.RateLimit;

@Service
public class DbService {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private int threshold = 5;

    @Inject
    private PersistenceManager manager;


    public void setThresholdForRateLimit(int threshold) {
        this.threshold = threshold;
    }

    public void insertForRateLimit(String value, int ttl) {
        final TypedMap countMap = manager
                .nativeQuery("SELECT count(*) FROM ratelimit WHERE id='ratelimit' LIMIT 100")
                .first();
        if (countMap != null) {
            final Long count = (Long) countMap.get("count");
            if (count >= threshold) {
                throw new IllegalStateException("You cannot have more than " + threshold + " values per slice of " + ttl + " " +
                        "seconds");
            }
        }

        manager.nativeQuery("INSERT INTO ratelimit(id,column,value) VALUES('ratelimit','" + value + "'," +
                "'" + value + "') USING TTL " + ttl).first();
    }

    public List<RateLimit> fetchRateLimitedValues() {
        List<RateLimit> result = new ArrayList<>();
        final List<TypedMap> maps = manager
                .nativeQuery("SELECT value,ttl(value) FROM ratelimit WHERE id='ratelimit' LIMIT 100").get();
        for (TypedMap map : maps) {
            result.add(new RateLimit(map.<String>getTyped("value"), map.<Integer>getTyped("ttl(value)")));
        }
        return result;
    }

    public String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.format(new Date());
    }


    public void resetDb() {
        manager.nativeQuery("TRUNCATE ratelimit").execute();
    }
}

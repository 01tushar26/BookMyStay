package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services.RateLimiter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginRateLimiterService {
    private final Map<String , Bucket> map = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        log.info("Creating the bucket");

        Bandwidth limit = Bandwidth.builder()
                .capacity(5)
                .refillGreedy(5, Duration.ofMinutes(1))
                .build();

        return Bucket
                .builder()
                .addLimit(limit)
                .build();

    }

    public boolean allowRequest(String ip) {
    log.info("Checking whether the bucket exist or not for ip :{}",ip);

//        Bucket bucket = map.get(ip);
//
//        if (bucket == null) {
//            log.info("Creating Bucket for ip :{}",ip);
//            bucket = createNewBucket();
//            map.put(ip, bucket);
//        }

        Bucket bucket = map.computeIfAbsent(ip, k -> {
            log.info("Creating bucket for ip: {}", ip);
            return createNewBucket();
        });

        return bucket.tryConsume(1);
    }


}

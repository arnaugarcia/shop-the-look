package com.klai.stl.service.space.aspect;

import static java.util.Objects.requireNonNull;

import com.klai.stl.repository.SpaceRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SpaceCacheAspect {

    private final Logger log = LoggerFactory.getLogger(SpaceCacheAspect.class);

    private final CacheManager cacheManager;

    public SpaceCacheAspect(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Around(value = "" + "spacePhotosAreUpdated() ||" + "spaceCoordinatesAreUpdated() ||" + "spaceIsDeleted() ||" + "spaceIsUpdated()")
    public Object clearSpaceCache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final String spaceReference = (String) proceedingJoinPoint.getArgs()[0];
        log.info("Cleaning cache for space {}", spaceReference);
        clearSpaceCacheByReference(spaceReference);
        return proceedingJoinPoint.proceed();
    }

    @Pointcut(value = "within(com.klai.stl.service.space.SpacePhotoService+) && execution(* *(String, ..))")
    public void spacePhotosAreUpdated() {}

    @Pointcut(value = "within(com.klai.stl.service.space.SpaceCoordinateService+) && execution(* *(String, ..))")
    public void spaceCoordinatesAreUpdated() {}

    @Pointcut(value = "execution(void com.klai.stl.service.space.SpaceService.delete(String))")
    public void spaceIsDeleted() {}

    @Pointcut(value = "execution(* com.klai.stl.service.space.SpaceService.partialUpdate(String, ..))")
    public void spaceIsUpdated() {}

    private void clearSpaceCacheByReference(String spaceReference) {
        requireNonNull(cacheManager.getCache(SpaceRepository.SPACES_CACHE)).evict(spaceReference);
    }
}

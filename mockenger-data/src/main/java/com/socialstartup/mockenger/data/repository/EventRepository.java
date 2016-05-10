package com.socialstartup.mockenger.data.repository;

import com.socialstartup.mockenger.data.model.persistent.log.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Dmitry Ryazanov
 */
public interface EventRepository<T extends Event> extends PagingAndSortingRepository<T, String> {

    @Query("{ '" + DefaultMongoTypeMapper.DEFAULT_TYPE_KEY + "' : ?0 }")
    Page<T> findByEntityType(String clazz, Pageable pageable);

    Iterable<T> findByEventType(String eventType);
}

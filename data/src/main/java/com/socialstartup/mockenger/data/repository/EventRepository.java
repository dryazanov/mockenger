package com.socialstartup.mockenger.data.repository;

import com.socialstartup.mockenger.data.model.persistent.log.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author Dmitry Ryazanov
 */
public interface EventRepository<T extends Event> extends PagingAndSortingRepository<T, String> {

    @Query("{ 'entity." + DefaultMongoTypeMapper.DEFAULT_TYPE_KEY + "' : { $in : ?0 } }")
    Page<T> findByEntityTypeIn(List<String> eventClassTypes, Pageable pageable);
}

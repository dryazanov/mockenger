package org.mockenger.data.repository;

import org.mockenger.data.model.persistent.log.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper.DEFAULT_TYPE_KEY;

/**
 * @author Dmitry Ryazanov
 */
public interface EventRepository<T extends Event> extends PagingAndSortingRepository<T, String> {

	@Query("{ $and : [ { 'entity." + DEFAULT_TYPE_KEY + "' : { $in : ?0 } }, { eventDate: { $gte: ?1, $lte: ?2 } } ] }")
	Page<T> findByEntityTypeAndEventDate(List<String> eventClassTypes, Date eventStartDate, Date eventEndDate, Pageable pageable);
}

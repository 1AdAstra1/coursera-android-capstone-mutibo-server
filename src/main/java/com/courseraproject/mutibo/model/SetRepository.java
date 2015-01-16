package com.courseraproject.mutibo.model;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SetRepository extends CrudRepository<Set, Long> {
	@Query("FROM Set s WHERE s.id NOT IN :answered_ids ORDER BY RAND()")
	public Page<Set> findNextUnanswered(@Param("answered_ids") List<Long> answeredIds, Pageable page); 
}

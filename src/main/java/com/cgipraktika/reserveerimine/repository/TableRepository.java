package com.cgipraktika.reserveerimine.repository;

import com.cgipraktika.reserveerimine.model.TableEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends CrudRepository<TableEntity, Long> {

}


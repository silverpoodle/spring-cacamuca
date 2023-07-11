package com.goalapa.cacamuca.requestDomain.command.domain.repository;

import com.goalapa.cacamuca.blackListDomain.command.domain.aggregate.entity.Blacklist;
import com.goalapa.cacamuca.requestDomain.command.domain.aggregate.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

}
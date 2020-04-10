package com.bqiao.consul.internal.repo.jpa;

import com.bqiao.consul.internal.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepo extends JpaRepository<Message, String> {
}

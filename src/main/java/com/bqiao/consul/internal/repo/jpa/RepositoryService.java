package com.bqiao.consul.internal.repo.jpa;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class RepositoryService {
    @Autowired
    private MessageRepo messageRepo;
}

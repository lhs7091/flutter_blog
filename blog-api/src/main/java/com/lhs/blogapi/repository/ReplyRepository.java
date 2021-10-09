package com.lhs.blogapi.repository;

import com.lhs.blogapi.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}

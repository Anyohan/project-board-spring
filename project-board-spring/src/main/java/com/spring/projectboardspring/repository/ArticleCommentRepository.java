package com.spring.projectboardspring.repository;

import com.spring.projectboardspring.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCommentRepository extends JpaRepository<Article, Long> {
}

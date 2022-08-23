package com.spring.projectboardspring.repository;

import com.spring.projectboardspring.config.jpaConfig;
import com.spring.projectboardspring.domain.Article;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationStartupAware;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JPA 연결 테스트")
@Import(jpaConfig.class)
@DataJpaTest
class JPARepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JPARepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine(){
        //Given
        //when
        List<Article> articles = articleRepository.findAll();

        //Then
        assertThat(articles)
                .isNotNull()
                .hasSize(123);
    }

    @DisplayName("insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine(){
        //Given
        long previousCount = articleRepository.count(); // 이전의 행들 샌다
        //When
        Article savedArticle = articleRepository.save(Article.of("new content", "new content","#spring"));
        //article 레포에 새로운 행을 삽입하고
        //Then
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
        // 이전의 행들에 비해 1개가 잘 추가 되었는지 확인한다.

    }

    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine(){
        //Given
        Article article = articleRepository.findById(1L).orElseThrow(); // id 를 기준으로 찾고 없으면 쓰로우로 테스트를 끝냄
        String updatedHashtag = "#springboot";
        article.setHashtag(updatedHashtag); // set 메소드를 통해 해당 article 행의 해쉬태그를 업데이트 해준다
        //When
        //Article savedArticle = articleRepository.save(article);
        Article savedArticle = articleRepository.saveAndFlush(article);

        //Then
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag",updatedHashtag);

        // test 는 통과한다 하지만 콘솔에서 update 쿼리문을 확인할 수가 없다. 이는 junit 으로 슬라이스 테스트를 돌릴 때 모든 테스트 메소드는
        // 메소드 단위로 자동 트랙젝션이 걸려있다 Data jPATest 덕분에
        // 기본값 트랜젝션이 롤백이므로 중요하지않다고 판단되는 행위 (update 등) 은 동작하지 않을 수 있다
        // 그렇기에 save 후 flush 를 해주어야 작동함
        // saveFlush 혹은 따로 해주어야힘
    }

    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine(){
        //Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        // cascading 옵션으로 글을 지우면 관련 댓글도 지워져야함 그가 동작하는지 확인해보자
        long previousArticleCommentCount = articleCommentRepository.count();
        long deletedCommentsSize = article.getArticleComments().size();

        //When
        articleRepository.delete(article);
        //article 레포에 새로운 행을 지우고
        //Then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount -1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount- deletedCommentsSize);


        // 이전의 행들에 게시글은 한개, 또한 그와 관련된 모든 댓글이 지워 졌는지 확인한다.

    }

}
package com.feurle.tg.service.mapper;

import com.feurle.tg.domain.ArticleImage;
import com.feurle.tg.domain.NewsArticle;
import com.feurle.tg.domain.User;
import com.feurle.tg.service.dto.ArticleImageDTO;
import com.feurle.tg.service.dto.NewsArticleDTO;
import com.feurle.tg.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NewsArticle} and its DTO {@link NewsArticleDTO}.
 */
@Mapper(componentModel = "spring")
public interface NewsArticleMapper extends EntityMapper<NewsArticleDTO, NewsArticle> {
    @Mapping(target = "articleImage", source = "articleImage", qualifiedByName = "articleImageId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    NewsArticleDTO toDto(NewsArticle s);

    @Named("articleImageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ArticleImageDTO toDtoArticleImageId(ArticleImage articleImage);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}

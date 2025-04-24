package com.feurle.tg.service.mapper;

import com.feurle.tg.domain.ArticleImage;
import com.feurle.tg.service.dto.ArticleImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArticleImage} and its DTO {@link ArticleImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArticleImageMapper extends EntityMapper<ArticleImageDTO, ArticleImage> {}

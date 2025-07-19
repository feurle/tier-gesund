package com.feurle.tg.service.dto;

import com.feurle.tg.domain.enumeration.Language;
import com.feurle.tg.domain.enumeration.Location;
import com.feurle.tg.domain.enumeration.State;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.feurle.tg.domain.NewsArticle} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NewsArticleDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @Lob
    private String content;

    private State state;

    private Instant publishedDate;

    private String author;

    private Language language;

    private Location location;

    private ArticleImageDTO articleImage;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Instant getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Instant publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArticleImageDTO getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(ArticleImageDTO articleImage) {
        this.articleImage = articleImage;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NewsArticleDTO)) {
            return false;
        }

        NewsArticleDTO newsArticleDTO = (NewsArticleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, newsArticleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NewsArticleDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", state='" + getState() + "'" +
            ", publishedDate='" + getPublishedDate() + "'" +
            ", author='" + getAuthor() + "'" +
            ", language='" + getLanguage() + "'" +
            ", location='" + getLocation() + "'" +
            ", articleImage=" + getArticleImage() +
            ", user=" + getUser() +
            "}";
    }
}

package com.feurle.tg.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.feurle.tg.domain.enumeration.Language;
import com.feurle.tg.domain.enumeration.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A NewsArticle.
 */
@Entity
@Table(name = "news_article")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NewsArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @Column(name = "published_date")
    private Instant publishedDate;

    @Column(name = "author")
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;

    @JsonIgnoreProperties(value = { "newsArticle" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ArticleImage articleImage;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NewsArticle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public NewsArticle title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public NewsArticle content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public State getState() {
        return this.state;
    }

    public NewsArticle state(State state) {
        this.setState(state);
        return this;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Instant getPublishedDate() {
        return this.publishedDate;
    }

    public NewsArticle publishedDate(Instant publishedDate) {
        this.setPublishedDate(publishedDate);
        return this;
    }

    public void setPublishedDate(Instant publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getAuthor() {
        return this.author;
    }

    public NewsArticle author(String author) {
        this.setAuthor(author);
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Language getLanguage() {
        return this.language;
    }

    public NewsArticle language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public ArticleImage getArticleImage() {
        return this.articleImage;
    }

    public void setArticleImage(ArticleImage articleImage) {
        this.articleImage = articleImage;
    }

    public NewsArticle articleImage(ArticleImage articleImage) {
        this.setArticleImage(articleImage);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public NewsArticle user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NewsArticle)) {
            return false;
        }
        return getId() != null && getId().equals(((NewsArticle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NewsArticle{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", state='" + getState() + "'" +
            ", publishedDate='" + getPublishedDate() + "'" +
            ", author='" + getAuthor() + "'" +
            ", language='" + getLanguage() + "'" +
            "}";
    }
}

package com.tbp.model;

import javax.persistence.*;

@Entity
@Table(name = "comentario")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "nome_usuario")
    String username;
    @Column(name = "texto", length = 4000)
    String text;
    @ManyToOne
    @JoinColumn(name = "id_postagem")
    Post post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}

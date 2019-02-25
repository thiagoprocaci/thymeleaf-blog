# Aplicação Java Web com Thymeleaf

## Classes


### Comment

```
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


```

### Post

```
package com.tbp.model;

import javax.persistence.*;

@Entity
@Table(name = "postagem")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "nome_usuario")
    String username;
    @Lob
    @Column(name = "texto")
    String text;
    @Column(name = "title")
    String title;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}


```

### CommentRepository

```
package com.tbp.repository;

import com.tbp.model.Comment;
import com.tbp.model.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

}


```

### PostRepository

```
package com.tbp.repository;

import com.tbp.model.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
}


```

### CommentController

```
package com.tbp.controller;

import com.tbp.model.Comment;
import com.tbp.model.Post;
import com.tbp.repository.CommentRepository;
import com.tbp.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("comment")
public class CommentController {

    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String createComment(@RequestParam("username") String username,
                                @RequestParam("text") String text,
                                @RequestParam("postId") Long postId) {

        if(StringUtils.hasText(text) && StringUtils.hasText(username)) {
            Post post = postRepository.findOne(postId);
            Comment comment = new Comment();
            comment.setUsername(username);
            comment.setText(text);
            comment.setPost(post);
            commentRepository.save(comment);
        }
        return "redirect:/post/read/" + postId;

    }

}


```

### PostController

```
package com.tbp.controller;

import com.tbp.model.Comment;
import com.tbp.model.Post;
import com.tbp.repository.CommentRepository;
import com.tbp.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("post")
public class PostController {

    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createPage(Model model) {
        model.addAttribute("myPost", new Post());
        model.addAttribute("message", "Blogging ...");
        return "createPost";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String createPost(@ModelAttribute Post myPost, Model model) {
        if(!StringUtils.hasText(myPost.getText()) || !StringUtils.hasText(myPost.getTitle())
        || !StringUtils.hasText(myPost.getUsername())) {
            model.addAttribute("myPost", myPost);
            model.addAttribute("message", "Don't forget to fill everything!");
            return "createPost";
        }
        postRepository.save(myPost);
        return "redirect:list";
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String listPage(Model model) {
        Iterable<Post> all = postRepository.findAll();
        model.addAttribute("posts", all);
        return "listPost";
    }

    @RequestMapping(value = "read/{id}", method = RequestMethod.GET)
    public String readPage(Model model, @PathVariable Long id) {
        Post post = postRepository.findOne(id);
        List<Comment> commentList = commentRepository.findByPost(post);
        model.addAttribute("myPost", post);
        model.addAttribute("commentList", commentList);
        return "readPost";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete( @PathVariable Long id) {
        Post post = postRepository.findOne(id);
        List<Comment> commentList = commentRepository.findByPost(post);
        for(Comment comment : commentList) {
            commentRepository.delete(comment);
        }
        postRepository.delete(id);
        return "redirect:/post/list";
    }


}


```


### IndexController

```
package com.tbp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "redirect:/post/list";
    }

}


```

### Application

```
package com.tbp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootApplication
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setForceEncoding(true);
        characterEncodingFilter.setEncoding("UTF-8");
        registrationBean.setFilter(characterEncodingFilter);
        return registrationBean;
    }
}


```

## Properties

### application.properties

```
# H2 console - http://localhost:8080/jpa-app/h2
spring.h2.console.enabled=true
spring.h2.console.path=/h2


#Configuracoes do banco de dados

# url de conexao com o banco de dados
spring.datasource.url=jdbc:h2:file:~/blog;DB_CLOSE_ON_EXIT=FALSE

# nome do usuario do banco
spring.datasource.username=sa

# senha do banco de dados
spring.datasource.password=

# nome da classe Java resposavel por conectar no banco de dados
spring.datasource.driver-class-name=org.h2.Driver

# propriedade que diz para a aplicacao criar as tabelas no banco de dados.
# Tudo que estiver com @Entity vai se transformar em uma tabela
spring.jpa.hibernate.ddl-auto=update

#contexto da aplicacao
server.context-path=/blog

#configuracao para o thymeleaf considerar versoes anteriores ao html5
spring.thymeleaf.mode=LEGACYHTML5

```

## HTML

### createPost.html

```
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Getting Started: Handling Form Submission</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.11/summernote-bs4.css" rel="stylesheet">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.11/summernote-bs4.js"></script>
</head>
<body>


<div class="container mt-4">
    <h1>
        <div th:text="${message}"></div>
    </h1>

    <form action="#" th:action="@{/post/create}" th:object="${myPost}" method="post">
        <div class="form-group">
            <label for="username">Username</label>
            <input id="username" class="form-control" type="text" th:field="*{username}" />
        </div>
        <div class="form-group">
            <label for="title">Title</label>
            <input id="title" class="form-control" type="text" th:field="*{title}" />
        </div>
        <div class="form-group">
            Text: <textarea  th:field="*{text}" ></textarea>
        </div>
        <div class="form-group">
            <input type="submit" value="Submit" />
        </div>
    </form>

    <div>
        <a class="btn btn-primary btn-lg active" role="button" aria-pressed="true"th:href="@{'/post/list/'}">Back</a>
    </div>
</div>

</body>

<script>
    $(document).ready(function() {
        $('textarea').summernote();
        });
 </script>
</html>

```

### listPost.html

```
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Getting Started: Handling Form Submission</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css">
</head>
<body>

<div class="container mt-4">
<h1>Blogging...</h1>

    <table class="table table-sm table-striped table-hover table-bordered">
        <thead>
            <tr>
                <th>View</th>
                <th>Delete</th>
                <th>User</th>
                <th>T&iacute;tulo</th>


            </tr>
        </thead>
        <tbody>
            <tr th:each="post : ${posts}">
                <td>
                    <a th:href="@{'/post/read/' + ${post.id}}">view</a>
                </td>
                <td>
                    <a th:href="@{'/post/delete/' + ${post.id}}">delete</a>
                </td>
                <td th:text="${post.username}"></td>
                <td th:text="${post.title}"></td>
            </tr>
        </tbody>
    </table>
    <div>
        <a class="btn btn-primary btn-lg active" role="button" aria-pressed="true"th:href="@{'/post/create/'}">New Post</a>
    </div>
</div>
</body>
</html>

```

### readPost.html

```
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Getting Started: Handling Form Submission</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css">

</head>
<body>


<div class="container mt-4">
    <h1>
        <div th:text="${myPost.title}"></div>
    </h1>

    <div th:remove="tag" th:utext="${myPost.text} "></div>

    <h3>
        Leave a comment
    </h3>

    <form action="#" th:action="@{/comment/create}"  method="post">
        <div class="form-group">
            <label for="username">Username</label>
            <input id="username" class="form-control" name="username" />
        </div>
        <div class="form-group">
            <label for="text">Text</label>
            <textarea id="text"  name="text" class="form-control" ></textarea>

        </div>

        <div class="form-group">
            <input type="submit" value="Submit" />
        </div>

        <input  type="hidden" name="postId"  th:value="${myPost.id}"/>
    </form>

    <div  th:each="comment : ${commentList}" class="alert alert-info" role="alert">
        <div th:text="${' User: ' + comment.username}"></div>
        <div th:text="${comment.text}"></div>
    </div>

    <div>
        <a class="btn btn-primary btn-lg active" role="button" aria-pressed="true"th:href="@{'/post/list/'}">Back</a>
    </div>

</div>

</body>

</html>

```
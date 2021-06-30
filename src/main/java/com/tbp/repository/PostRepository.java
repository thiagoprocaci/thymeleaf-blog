package com.tbp.repository;

import com.tbp.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {


    @Query(value = "select * from postagem where  lower(texto) like '%' || lower(:text) || '%'  or  lower(titulo) like '%' || lower(:text) || '%'", nativeQuery = true)
    List<Post> findByText(@Param("text") String text);

}

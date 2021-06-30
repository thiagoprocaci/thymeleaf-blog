package com.tbp.repository;

import com.tbp.model.Post;
import com.tbp.model.PostLike;
import com.tbp.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostLikeRepository extends CrudRepository<PostLike, Long> {

    PostLike findByPostAndUser(Post post, User user);

    Long countByPost(Post post);

    List<PostLike> findByPost(Post post);

}

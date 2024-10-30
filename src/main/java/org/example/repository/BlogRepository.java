package org.example.repository;

import org.example.model.Blog;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
    List<Blog> findByUser(User user);
    Optional<Blog> findByBlogIdAndUser(Integer blogId, User user); //Added this method for efficient authorization check.
}

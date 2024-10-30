package org.example.service;

import org.example.exception.BlogNotFoundException;
import org.example.exception.UserNotFoundException;
import org.example.model.Blog;
import org.example.model.User;
import org.example.repository.BlogRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;


    public Blog createBlog(Blog blog, Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            blog.setUser(user.get());
            return blogRepository.save(blog);
        } else {
            throw new UserNotFoundException("User not found.");
        }
    }


    public Blog updateBlog(Integer blogId, Blog updatedBlog) {
        Optional<Blog> existingBlog = blogRepository.findById(blogId);
        if (existingBlog.isPresent()) {
            Blog blog = existingBlog.get();
            // Update blog properties - No authorization check
            blog.setTitle(updatedBlog.getTitle());
            blog.setContent(updatedBlog.getContent());
            blog.setCategory(updatedBlog.getCategory());
            return blogRepository.save(blog);
        } else {
            throw new BlogNotFoundException("Blog not found.");
        }
    }


    public void deleteBlog(Integer blogId) {
        Optional<Blog> existingBlog = blogRepository.findById(blogId);
        if (existingBlog.isPresent()) {
            blogRepository.deleteById(blogId); // No authorization check
        } else {
            throw new BlogNotFoundException("Blog not found.");
        }
    }


    public Blog getBlogById(Integer blogId) {
        Optional<Blog> blog = blogRepository.findById(blogId);
        return blog.orElse(null);
    }


    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }


    public List<Blog> getBlogsByUser(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(blogRepository::findByUser).orElse(null);
    }
}

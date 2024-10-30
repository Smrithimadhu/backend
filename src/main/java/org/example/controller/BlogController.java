package org.example.controller;

import org.example.exception.AccessDeniedException;
import org.example.exception.BlogNotFoundException;
import org.example.exception.UserNotFoundException;
import org.example.model.Blog;
import org.example.service.BlogService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController
@RequestMapping("/api")
public class BlogController {

    //-------------------------PROPERTIES------------------------------------------------------------------------
    //Hello guys branch 1 .
    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;


    //-------------------------METHODS--------------------------------------------------------------------------

    @PostMapping("/blogs/users/{userId}")
    public ResponseEntity<Blog> createBlog(@RequestBody Blog blog, @PathVariable Integer userId) {
        try {
            Blog createdBlog = blogService.createBlog(blog, userId);
            return new ResponseEntity<>(createdBlog, HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) { // Catch other potential exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //----------------------------------------------------------------------------------------------------------

    @PutMapping("/blogs/{blogId}")
    public ResponseEntity<Blog> updateBlog(@PathVariable Integer blogId, @RequestBody Blog updatedBlog) {
        try {
            return ResponseEntity.ok(blogService.updateBlog(blogId, updatedBlog));
        } catch (BlogNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //----------------------------------------------------------------------------------------------------------

    @DeleteMapping("/blogs/{blogId}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Integer blogId) {
        blogService.deleteBlog(blogId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //----------------------------------------------------------------------------------------------------------

    @GetMapping("/blogs/{blogId}")
    public ResponseEntity<Blog> getBlogById(@PathVariable Integer blogId) {
        Blog blog = blogService.getBlogById(blogId);
        return blog != null ? ResponseEntity.ok(blog) : ResponseEntity.notFound().build();
    }

    //----------------------------------------------------------------------------------------------------------

    @GetMapping("/blogs")
    public ResponseEntity<List<Blog>> getAllBlogs() {
        return ResponseEntity.ok(blogService.getAllBlogs());
    }

    //----------------------------------------------------------------------------------------------------------

    @GetMapping("/users/{userId}/blogs")
    public ResponseEntity<List<Blog>> getBlogsByUser(@PathVariable Integer userId) {
        List<Blog> blogs = blogService.getBlogsByUser(userId);
        return ResponseEntity.ok(blogs);
    }
}
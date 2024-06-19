package net.skhu.service;

import java.util.List;
import net.skhu.entity.PostEntity;
import net.skhu.repository.PostRepository;
import net.skhu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// PostService.java
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<PostEntity> findPostsByUserid(String userid) {
        return postRepository.findByUser_Userid(userid);
    }

    public void savePost(PostEntity post) {
        postRepository.save(post);
    }

    public PostEntity findPostById(Integer id) {
        return postRepository.findById(id).orElse(null);
    }

    public void deletePostById(Integer id) {
        postRepository.deleteById(id);
    }

}

package net.skhu.service;

import java.util.List;
import net.skhu.entity.PostEntity;
import net.skhu.repository.PostRepository;
import net.skhu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    public List<PostEntity> findPostsByUserid(String userid) {
        return postRepository.findByUser_Userid(userid);  // 메소드명을 업데이트
    }

    public void savePost(PostEntity post) {
        postRepository.save(post);
    }

}

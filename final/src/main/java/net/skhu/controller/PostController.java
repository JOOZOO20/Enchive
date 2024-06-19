package net.skhu.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.skhu.entity.ThemeEntity;
import net.skhu.repository.PostRepository;
import net.skhu.repository.UserRepository;
import net.skhu.service.ThemeService;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import net.skhu.entity.PostEntity;
import net.skhu.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import net.skhu.entity.UserEntity;
import net.skhu.service.UserService;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private ThemeService themeService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostRepository postRepository;

    // 업로드 디렉터리 경로를 애플리케이션의 실행 경로에 uploads 폴더로 설정
    private final String uploadDir = System.getProperty("user.dir") + "/uploads";

    @GetMapping("/bookmain")
    public String bookMain(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId != null) {
            List<PostEntity> posts = postService.findPostsByUserid(userId);
            model.addAttribute("posts", posts);
            return "bookmain";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/writing")
    public String writing() {
        return "writing";
    }

    @PostMapping("/savePost")
    public String savePost(@RequestParam("title") String title,
                           @RequestParam("detail") String content,
                           @RequestParam(value = "photo1", required = false) MultipartFile photo1,
                           HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        UserEntity user = userService.findByUserid(userId);
        if (user == null) {
            return "redirect:/login";
        }

        Integer selectedThemeId = (Integer) session.getAttribute("selectedThemeId");
        if (selectedThemeId == null) {
            return "redirect:/bookmain";
        }

        ThemeEntity theme = themeService.findThemeById(selectedThemeId);

        PostEntity post = new PostEntity();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);
        post.setTheme(theme);
        postService.savePost(post);

        if (photo1 != null && !photo1.isEmpty()) {
            String fileName = saveImage(photo1, post.getId());
            if (fileName != null) {
                post.setImg_Name(fileName);
                post.setImg_Path(Paths.get(uploadDir, "id=" + post.getId(), fileName).toString());
                postService.savePost(post);
            }
        }

        return "redirect:/bookmain";
    }

    @PostMapping("/updatePost")
    public String updatePost(@RequestParam("id") Integer id,
                             @RequestParam("title") String title,
                             @RequestParam("detail") String content,
                             @RequestParam(value = "photo1", required = false) MultipartFile photo1) {
        PostEntity post = postService.findPostById(id);
        if (post != null) {
            post.setTitle(title);
            post.setContent(content);
            if (photo1 != null && !photo1.isEmpty()) {
                String fileName = saveImage(photo1, post.getId());
                if (fileName != null) {
                    post.setImg_Name(fileName);
                    post.setImg_Path(Paths.get(uploadDir, "id=" + post.getId(), fileName).toString());
                }
            }
            postService.savePost(post);
        }
        return "redirect:/bookmain";
    }

    private String saveImage(MultipartFile file, Integer postId) {
        // 업로드 디렉터리 경로 생성
        Path uploadPath = Paths.get(uploadDir, "id=" + postId);

        // 업로드 디렉터리가 존재하지 않으면 생성
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        try {
            String fileName = file.getOriginalFilename();
            fileName = fileName.replace(" ", "_");
            Path filePath = uploadPath.resolve(fileName);

            if (!Files.exists(filePath)) {
                Files.copy(file.getInputStream(), filePath);
            }

            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/edit")
    public String getPostByIdForEdit(@RequestParam("id") Integer id, Model model) {
        PostEntity post = postService.findPostById(id);
        model.addAttribute("post", post);
        return "edit";
    }

    @GetMapping("/delete")
    public String delete(Model model, int id) {
        postRepository.deleteById(id);
        return "redirect:/bookmain";
    }
}
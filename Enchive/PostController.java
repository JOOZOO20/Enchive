package net.skhu.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.skhu.entity.ThemeEntity;
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
    private UserService userService;  // UserService 주입

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


        // UserEntity 객체를 조회
        UserEntity user = userService.findByUserid(userId);
        if (user == null) {
            return "redirect:/login"; // 조회된 사용자가 없으면 로그인 페이지로 리디렉션
        }

        Integer selectedThemeId = (Integer) session.getAttribute("selectedThemeId");
        if (selectedThemeId == null) {
            return "redirect:/bookmain";
        }

        ThemeEntity theme = themeService.findThemeById(selectedThemeId);

        PostEntity post = new PostEntity();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user); // UserEntity 객체를 설정
        post.setTheme(theme);
        if (photo1 != null && !photo1.isEmpty()) {
            String directory = "/path/to/the/directory";
            String filename = photo1.getOriginalFilename();
            Path filePath = Paths.get(directory, filename);
            try {
                photo1.transferTo(filePath);
                post.setPost_image(filePath.toString());
            } catch (IOException e) {
                e.printStackTrace(); // 파일 업로드 실패 처리
            }
        }

        postService.savePost(post);

        return "redirect:/bookmain";
    }


}


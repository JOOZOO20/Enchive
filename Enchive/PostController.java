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
import org.springframework.web.bind.annotation.ResponseBody;
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

    @Autowired
    private PostRepository postRepository;



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


    // 글을 수정할 때 데이터를 불러오는 메서드 추가
    @GetMapping("/edit")
    public String getPostByIdForEdit(@RequestParam("id") Integer id, Model model) {
        PostEntity post = postService.findPostById(id);
        model.addAttribute("post", post);
        return "edit";
    }

    // 글을 업데이트하는 메서드
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
                // 이미지 저장 로직 추가
                String fileName = saveImage(photo1);
                if (fileName != null) {
                    post.setPost_image(fileName);
                }
            }
            postService.savePost(post);
        }
        return "redirect:/bookmain";
    }
    // 이미지 저장 메서드 추가
    private String saveImage(MultipartFile file) {
        // 업로드 파일을 저장할 절대 경로 설정
        String uploadDir = "src/main/resources/uploads";
        Path uploadPath = Paths.get(uploadDir);

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


    @GetMapping("delete")
    public String delete(Model model, int id) {
        postRepository.deleteById(id);
        return "redirect:/bookmain";
    }


    @PostMapping("/image")
    public @ResponseBody String image(MultipartFile pic){
        String imageFileName = pic.getOriginalFilename();

        String path = "C:/SpringBootImage";

        Path imagePath = Paths.get(path+imageFileName);

        try{
            Files.write(imagePath, pic.getBytes());
        }catch (Exception e){

        }

        return imageFileName;
    }


}


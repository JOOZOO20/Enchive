package net.skhu.controller;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import net.skhu.entity.ThemeEntity;
import net.skhu.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MainhomeController {

    @Autowired
    private ThemeService themeService;

    @GetMapping("/mainhome")
    public String home(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId != null) {
            List<ThemeEntity> themes = themeService.findByUserId(userId);
            model.addAttribute("themes", themes);

            if (session.getAttribute("selectedThemeId") == null && !themes.isEmpty()) {
                session.setAttribute("selectedThemeId", themes.get(0).getId());
            }

            return "mainhome";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/addmainbook")
    public String Addmainbook(Model model) {
        return "addmainbook";
    }

    @PostMapping("/addTheme")
    public String addTheme(@RequestParam("title") String title, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        ThemeEntity theme = new ThemeEntity();
        theme.setTitle(title);
        theme.setUserid(userId);
        themeService.saveTheme(theme);

        return "redirect:/mainhome";
    }


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
}

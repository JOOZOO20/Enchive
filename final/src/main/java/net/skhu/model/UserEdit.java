package net.skhu.model;

import lombok.Data;

@Data
public class UserEdit {
    int id;
    String userid;
    String pw;
    String confirmPassword;
    String username;
    String nickname;
}

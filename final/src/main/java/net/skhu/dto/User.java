package net.skhu.dto;

import lombok.Data;

@Data
public class User {
    int id;
    String userid;
    String pw;
    String username;
    String nickname;
}

// requestDTO, responseDTO
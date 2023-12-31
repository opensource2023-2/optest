package com.example.test;
/*
사용자 계정 정보 모델 클래스
*/
public class UserAccount
{
    private String emailId, password, idToken, username; //firebase Uid(고유 토큰 정보)

    public UserAccount() {

    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}

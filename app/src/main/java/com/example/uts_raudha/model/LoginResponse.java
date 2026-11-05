package com.example.uts_raudha.model;

public class LoginResponse {
    private boolean success;
    private String message;
    private Data data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public static class Data {

        private User user;
        private String token;

        public User getUser() {
            return user;
        }

        public String getToken() {
            return token;
        }
    }

    public static class User {

        private int id;
        private String name;
        private String email;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }
}

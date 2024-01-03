package com.hashimte.hashbusdriver.model;


import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ChangePassword {
    @SerializedName("user")
    private User user;

    @SerializedName("newPassword")
    private String newPassword ;

    public ChangePassword(User user, String newPassword) {
        this.user = user;
        this.newPassword = newPassword;
    }
}

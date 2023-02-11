package com.ohh_really.ringdingdong.user.dto;

import lombok.Data;

@Data
public class GoogleUserInfo {
    private String id;
    private String email;
    private Boolean verified_email;
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
    private String locale;
}
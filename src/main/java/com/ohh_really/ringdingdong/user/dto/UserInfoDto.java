package com.ohh_really.ringdingdong.user.dto;

import com.ohh_really.ringdingdong.user.UserRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


import java.util.Set;

@Getter
@Setter
public class UserInfoDto {
    private String password;
    private String username;
    private String email;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private Set<UserRole> roles;
}

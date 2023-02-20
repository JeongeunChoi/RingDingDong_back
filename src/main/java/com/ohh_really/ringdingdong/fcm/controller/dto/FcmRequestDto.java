package com.ohh_really.ringdingdong.fcm.controller.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FcmRequestDto {
    private String title;
    private String content;
    private String targetToken;
    private String topic;
}

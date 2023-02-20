package com.ohh_really.ringdingdong.fcm.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.ohh_really.ringdingdong.fcm.controller.dto.FcmRequestDto;
import com.ohh_really.ringdingdong.fcm.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FcmController {

    private final FcmService firebaseCloudMessageService;

    // 토큰 등록과 토픽 생성을 클라이언트 측에서 진행하여 푸시 알림 기능 확인이 불가능
    @PostMapping("/api/fcm")
    public ResponseEntity pushMessage(@RequestBody FcmRequestDto requestDTO) throws IOException, FirebaseMessagingException {
//        System.out.println("targetToken : " + requestDTO.getTargetToken());
//        System.out.println("topic : " + requestDTO.getTopic());
        System.out.println("title : " + requestDTO.getTitle());
        System.out.println("content : " + requestDTO.getContent());

        // 특정 기기로 푸시 알림 전송
        if(requestDTO.getTargetToken().length()>0){
            System.out.println("targetToken : " + requestDTO.getTargetToken());
            firebaseCloudMessageService.sendMessageToToken(
                    requestDTO.getTargetToken(),
                    requestDTO.getTitle(),
                    requestDTO.getContent());
        }
        // 특정 토픽을 구독하는 기기로 푸시 알림 전송
        if(requestDTO.getTopic().length()>0){
            System.out.println("topic : " + requestDTO.getTopic());
            firebaseCloudMessageService.sendMessageToTopic(
                    requestDTO.getTopic(),
                    requestDTO.getTitle(),
                    requestDTO.getContent());
        }

        return ResponseEntity.ok().build();
    }

}

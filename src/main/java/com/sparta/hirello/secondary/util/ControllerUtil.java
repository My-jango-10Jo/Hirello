package com.sparta.hirello.secondary.util;

import com.sparta.hirello.secondary.base.dto.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public final class ControllerUtil {

    public static ResponseEntity<CommonResponse<?>> getResponseEntity(Object response, String msg) {
        return ResponseEntity.ok().body(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .msg(msg)
                .data(response)
                .build());
    }

    public static void verifyPathIdWithBody(Long pathId, Long bodyId) {
        if (!pathId.equals(bodyId)) {
            throw new IllegalArgumentException("PathVariable의 Id가 RequestBody의 Id와 일치하지 않습니다.");
        }
    }

}

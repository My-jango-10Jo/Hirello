package com.sparta.hirello.primary.column.controller;

import com.sparta.hirello.primary.column.dto.request.ColumnRequest;
import com.sparta.hirello.primary.column.dto.response.ColumnResponse;
import com.sparta.hirello.primary.column.entity.Columns;
import com.sparta.hirello.primary.column.service.ColumnService;
import com.sparta.hirello.secondary.base.dto.CommonResponse;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sparta.hirello.secondary.util.ControllerUtil.getResponseEntity;

@RequestMapping("/api/columns")
@RestController
@RequiredArgsConstructor
public class ColumnController {
    private final ColumnService columnService;

    @PostMapping
    public ResponseEntity<CommonResponse<?>> createColumn(ColumnRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Columns columns=columnService.createColumn(request, userDetails.getUser());
        return getResponseEntity(ColumnResponse.of(columns), "컬럼 생성 성공") ;
    }

    @DeleteMapping("/{columnId}")
    public ResponseEntity<CommonResponse<?>> deleteColumn(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        columnService.deleteColumn(id, userDetails.getUser());
        return getResponseEntity(id, "컬럼 삭제 완료");
    }
}

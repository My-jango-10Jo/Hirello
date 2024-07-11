package com.sparta.hirello.primary.column.controller;

import com.sparta.hirello.primary.column.dto.request.ColumnRequest;
import com.sparta.hirello.primary.column.service.ColumnService;
import com.sparta.hirello.secondary.base.dto.CommonResponse;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/columns")
@RestController
@RequiredArgsConstructor
public class ColumnController {
    private final ColumnService columnService;

    @PostMapping("/{columnName}")
    public ResponseEntity<CommonResponse<?>> createColumn(ColumnRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails){
      return columnService.createColumn(request, userDetails.getUser());
    }
}

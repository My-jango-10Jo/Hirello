package com.sparta.hirello.primary.board.controller;

import com.sparta.hirello.primary.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

//    @PostMapping
//    public ResponseEntity createBoard(@AuthenticationPrincipal UserDetailsImpl userDetails){
//
//    }

}

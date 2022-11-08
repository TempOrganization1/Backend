package com.sparta.actualprcatice.dto.responseDto;

import com.example.consolelog.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class HeartResponseDto {

    private String msg;

    private int heartCount = 0;

    public HeartResponseDto(Board board, String msg) {

        this.msg = msg;

        if (board.getHeartList() != null){
            this.heartCount = board.getHeartList().size();
        }
    }
}

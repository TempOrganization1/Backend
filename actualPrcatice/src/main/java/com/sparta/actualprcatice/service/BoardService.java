package com.sparta.actualprcatice.service;

import com.example.consolelog.dto.requestDto.BoardRequestDto;
import com.example.consolelog.dto.responseDto.BoardResponseDto;
import com.example.consolelog.dto.responseDto.CommentResponseDto;
import com.example.consolelog.dto.responseDto.ImageResponseDto;
import com.example.consolelog.dto.responseDto.ResponseDto;
import com.example.consolelog.entity.*;
import com.example.consolelog.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final ImageRepository imageRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final S3UpaloadService s3UpaloadService;


    // 게시물 작성
    public ResponseDto<?> writeBoard(Member member) {

        Board board = boardRepository.save(new Board(member));

        return ResponseDto.success(BoardResponseDto.builder()
                .boardId(board.getId())
                .build());
    }


    // 게시물 업로드
    @Transactional
    public ResponseDto<?> uploadBoard(Long boardId, BoardRequestDto boardRequestDto, Member member) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("작성중인 게시물이 아닙니다."));

        if (validateMember(member, board))
            throw new IllegalArgumentException("게시물 작성자와 현재 사용자가 일치하지 않습니다.");

        board.update(boardRequestDto);

        return ResponseDto.success("게시물 업로드가 완료되었습니다.");
    }


    // 게시글 작성 취소
    @Transactional
    public ResponseDto<?> cancelBoard(Long boardId, Member member) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("작성중인 게시물이 아닙니다."));

        if (validateMember(member, board))
            throw new IllegalArgumentException("게시물 작성자와 현재 사용자가 일치하지 않습니다.");

        boardRepository.delete(board);

        return ResponseDto.success("게시글 작성 취소");
    }


    // 게시물 전체 조회
    public ResponseDto<?> getBoardList() {

        List<Board> boardList = boardRepository.findAllByOrderByCreatedAtDesc();
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();

        for (Board board : boardList) {

            boardResponseDtoList.add(new BoardResponseDto(board, imageRepository.findTop1ByBoardOrderByCreatedAtAsc(board)));
        }

        return ResponseDto.success(boardResponseDtoList);
    }


    // 게시글 상세 조회
    public ResponseDto<?> getBoard(Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("해당 게시물이 존재하지 않습니다."));

        List<Comment> commentList = commentRepository.findAllByBoardOrderByCreatedAtDesc(board);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentResponseDtoList.add(CommentResponseDto.builder()
                    .commentId(comment.getId())
                    .comment(comment.getContent())
                    .nickname(comment.getMember().getNickname())
                    .dayBefore(Time.calculateTime(comment))
                    .build()
            );
        }

        return ResponseDto.success(BoardResponseDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getMember().getNickname())
                .commentCount(board.getCommentList().size())
                .heartCount(board.getHeartList().size())
                .commentList(commentResponseDtoList)
                .dayBefore(Time.calculateTime(board))
                .build());

    }


    // 게시글 수정
    @Transactional
    public ResponseDto<?> updateBoard(Long boardId, BoardRequestDto boardRequestDto, Member member) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("해당 게시물이 존재하지 않습니다."));

        if (validateMember(member, board))
            throw new IllegalArgumentException("게시물 작성자와 현재 사용자가 일치하지 않습니다.");

        board.update(boardRequestDto);

        return ResponseDto.success("게시물 수정이 완료되었습니다.");
    }


    // 게시글 삭제
    @Transactional
    public ResponseDto<?> deleteBoard(Long boardId, Member member) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("해당 게시물이 존재하지 않습니다."));

        if (validateMember(member, board))
            throw new IllegalArgumentException("게시물 작성자와 현재 사용자가 일치하지 않습니다.");

        boardRepository.delete(board);

        return ResponseDto.success("게시물 삭제가 완료되었습니다.");
    }


    // 게시물 트렌딩정렬 ( 생성날짜 & 하트 개수 기준 정렬하기)
    public ResponseDto<?> getTrendingBoard(String options) {

        List<Board> trendingBoard = new ArrayList<>();
        LocalDateTime currentDateTile = LocalDateTime.now();
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();


        // 오늘 기준
        if (options.equals("today")) {
            //게시물 생성 하루 기준 추출
            trendingBoard = boardRepository.findDistinctByCreatedAtBetween(currentDateTile.minusHours(24), LocalDateTime.now());

            //한 주 기준
        } else if (options.equals("week")) {
            trendingBoard = boardRepository.findDistinctByCreatedAtBetween(currentDateTile.minusWeeks(1), LocalDateTime.now());
            //(currentDateTile.minusWeeks(1), LocalDateTime.now());

            //한 달 기준
        } else if (options.equals("month")) {
            trendingBoard = boardRepository.findDistinctByCreatedAtBetween(currentDateTile.minusMonths(1), LocalDateTime.now());
            //(currentDateTile.minusMonths(1), LocalDateTime.now());
        }

        // 하트 개수 별로 정렬하기
        Collections.sort(trendingBoard, new Comparator<Board>() {
            @Override
            public int compare(Board o1, Board o2) {
                if (o1.getHeartList().size() > o2.getHeartList().size()) {
                    return -1;
                } else if (o1.getHeartList().size() < o2.getHeartList().size()) {
                    return 1;
                }
                return 0;
            }
        });

        for (Board board : trendingBoard) {
            boardResponseDtoList.add(new BoardResponseDto(board, imageRepository.findTop1ByBoardOrderByCreatedAtAsc(board)));
        }

        return ResponseDto.success(boardResponseDtoList);
    }


    // 이미지 업로드
    public ResponseDto<?> uploadImage(Long boardId, MultipartFile multipartFile) throws IOException {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        Image image = new Image(board, s3UpaloadService.upload(multipartFile, "board"));

        imageRepository.save(image);

        ImageResponseDto imageResponseDto = new ImageResponseDto(image.getImageUrl());

        return ResponseDto.success(imageResponseDto);
    }

    // 무한 스크롤
    public Map<String, List<BoardResponseDto>> getBoardListScroll(Integer page, Integer size, String sortBy, Boolean isAsc) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Slice<Board> sliceBoardList = boardRepository.findAllBy(pageable);

        Map<String, List<BoardResponseDto>> listMap = new HashMap<>();
        List<BoardResponseDto> boardList = new ArrayList<>();

        for (Board board : sliceBoardList) {
            BoardResponseDto boards = BoardResponseDto.builder()
                    .boardId(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .writer(board.getMember().getNickname())
                    .commentCount(board.getCommentList().size())
                    .heartCount(board.getHeartList().size())
                    .build();

            boardList.add(boards);
        }

        listMap.put("boardSlice", boardList);

        return listMap;
    }

    public boolean validateMember(Member member, Board board) {
        return !member.getName().equals(board.getMember().getName());
    }
}


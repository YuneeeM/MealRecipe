package doubleni.mealrecipe.model.DTO;

import doubleni.mealrecipe.model.Board;
//import doubleni.mealrecipe.model.UploadImage;
import doubleni.mealrecipe.model.User;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class BoardReq {
    // 게시판 추가, 수정 요청 DTO
    private Long boardId;
    private String nickname;
    private String title;
    private String content;
    private Integer likeCnt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long fileId;

    @Builder
    public Board toEntity(User user)  {
        return Board.builder()
                .user(user)
                .boardId(boardId)
                .title(title)
                .likeCnt(0)
                .content(content)
                .fileId(fileId)
                .build();
    }

    @Builder
    public BoardReq(Long boardId, String nickname, String title, String content, Long fileId, Integer likeCnt) {
        this.boardId = boardId;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.fileId = fileId;
        this.likeCnt = likeCnt;
    }
}

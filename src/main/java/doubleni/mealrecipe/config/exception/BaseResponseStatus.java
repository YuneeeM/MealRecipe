package doubleni.mealrecipe.config.exception;

import lombok.Getter;

/**
 * error code
 * */
@Getter
public enum BaseResponseStatus {

    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),
    USERS_NOT_EXISTS(false, 2011, "존재하지 않는 유저입니다."),
    USERS_ALREADY_REGISTER(false,2012,"이미 저장한 소셜로그인 유저입니다."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"이미 가입된 이메일입니다."),
    POST_USERS_EMPTY_TELNUM(false, 2018, "휴대폰 번호를 입력해주세요."),
    POST_USERS_INVALID_TELNUM(false, 2019, "휴대폰 번호 형식을 확인해주세요."),
    POST_USERS_EXISTS_TELNUM(false,2020,"이미 가입된 휴대폰 번호입니다."),
    POST_USERS_EMPTY_PWD(false, 2021, "비밀번호를 입력해주세요."),
    DELETE_FAIL_USER(false, 2022, "회원 탈퇴가 되지 않았습니다."),
    POST_USERS_EMPTY_NICKNAME(false,2023,"닉네임을 입력해주세요."),
    POST_USERS_EXISTS_NICKNAME(false,2024,"중복된 닉네임입니다."),
    POST_USERS_INVALID_NICKNAME(false, 2025, "닉네임은 2 ~ 20자 사이로 입력해주세요."),
    POST_USERS_INVALID_PHONENO(false, 2026, "휴대폰 번호는 숫자만 입력이 가능합니다."),
    POST_USERS_INVALID_PHONENO_LENGTH(false, 2027, "휴대폰 번호는 최대 11자리까지 입력이 가능합니다."),

    // reviews
    POST_REVIEWS_INVALID_CONTENT(false,2030, "최대 입력 글자 수는 1000자입니다."),
    DELETE_FAIL_REVIEW(false, 2031, "리뷰를 삭제하지 못했습니다."),
    REVIEWS_EMPTY_REVIEW_ID(false,2032,"존재하지 않거나 삭제된 리뷰입니다."),
    POST_REVIEWS_NO_CONTEXT(false,2033,"리뷰 내용을 입력해주세요."),
    POST_REVIEWS_NO_RATINGS(false,2034,"리뷰 평점을 입력해주세요."),
    POST_REVIEWS_FAILS(false,2035,"리뷰 작성 실패하였습니다."),
    REVIEW_NO_EXISTS(false,2036,"저장된 리뷰가 없습니다."),

    // category
    CATEGORIES_EMPTY_CATEGORY(false, 2040, "존재하지 않는 카테고리입니다."),

    // recipe
    RECIPE_NOT_EXISTS(false, 2050, "존재하지 않는 레시피입니다."),
    RECIPE_ID_NO_EXISTS(false,2051,"레시피 아이디를 입력해주세요"),

    // board
    POST_BOARD_EMPTY_TITLE(false,2060, "제목을 입력해주세요."),
    POST_BOARD_EMPTY_CONTENT(false,2061, "내용을 입력해주세요."),
    BOARD_NOT_EXISTS(false,2063,"존재 하지 않거나 삭제된 게시글 입니다."),
    POST_BOARD_FAILS(false, 2064, "게시글 등록을 실패하였습니다."),

    // comment
    COMMENT_NOT_EXISTS(false, 2070, "존재 하지 않거나 삭제된 댓글 입니다."),

    // likes
    LIKE_NOT_EXISTS(false, 2080, "좋아요가 존재 하지 않읍니다."),
    LIKE_BOARD_ALREADY_EXISTS(false, 2081, "이미 좋아요한 게시글입니다."),
    LIKE_RECIPE_ALREADY_EXISTS(false, 2082, "이미 좋아요한 레시피입니다."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),


    // board


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    JOIN_ERROR(false,4002,"회원가입에 실패하였습니다."),
    JSON_ERROR(false,4003,"json 파일 저장 실패하였습니다."),
    SAVE_ERROR(false,4004,"파일 저장 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),
    UPDATE_FAIL_USER(false,4014,"유저 정보를 수정하는데 실패했습니다."),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),
    KAKAO_CONNECTION_ERROR(false, 4013, "카카오톡 연결에 실패하였습니다."),

    // board
    ADD_FAIL_BOARD(false, 4020, "게시판 등록 실패"),
    UPDATE_FAIL_BOARD(false, 4021, "게시판 수정 실패"),
    DELETE_FAIL_BOARD(false, 4022, "게시판 삭제 실패"),
    SHOW_FAIL_BOARD(false, 4023, "게시판 조회 실패"),

    // files
    ADD_FAIL_FILES(false, 4030, "파일 등록 실패"),
    UPDATE_FAIL_FILES(false, 4031, "파일 수정 실패"),
    DELETE_FAIL_FILES(false, 4032, "파일 삭제 실패"),

    // comment
    ADD_FAIL_COMMENT(false, 4040, "댓글 등록 실패"),
    UPDATE_FAIL_COMMENT(false, 4041, "댓글 수정 실패"),
    DELETE_FAIL_COMMENT(false, 4042, "댓글 삭제 실패"),

    // like
    ADD_FAIL_LIKE(false, 4050, "좋아요 추가 실패"),
    DELETE_FAIL_LIKE(false, 4051, "좋아요 삭제 실패");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    //열거 상수에 대한 생성자로, 응답 상태값을 초기화함
    private BaseResponseStatus(boolean isSuccess, int code, String message){
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}

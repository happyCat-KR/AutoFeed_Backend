package kr.soft.autofeed.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 공통 응답 포맷 클래스
 * @param <T> 응답 데이터 타입
 */
@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // null인 필드는 JSON에서 제외
public class ResponseData<T> {
    
    private int code;
    private String message;
    private T data;

    // ✅ 성공 응답
    public static <T> ResponseData<T> success(T data) {
        return new ResponseData<>(200, "성공", data);
    }

    public static <T> ResponseData<T> success(String message, T data) {
        return new ResponseData<>(200, message, data);
    }

    // ✅ 실패 응답
    public static <T> ResponseData<T> error(int code, String message) {
        return new ResponseData<>(code, message, null);
    }
}

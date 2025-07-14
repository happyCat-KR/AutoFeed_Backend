package kr.soft.autofeed.util;

public class HangulFilterUtil {

    /**
     * 문자열 끝 문자가 완성형 한글 또는 미완성 한글이면 한 글자만 제거하고 반환
     * 아니면 원본 그대로 반환
     */
    public static String removeLastIfHangul(String input) {
        if (input == null || input.isEmpty()) return input;

        int len = input.length();
        char lastChar = input.charAt(len - 1);

        if (isCompleteKorean(lastChar) || isIncompleteKorean(lastChar)) {
            return input.substring(0, len - 1);
        }
        return input;
    }

    private static boolean isCompleteKorean(char ch) {
        return (ch >= 0xAC00 && ch <= 0xD7A3);
    }

    private static boolean isIncompleteKorean(char ch) {
        return (ch >= 0x1100 && ch <= 0x1112)  // 초성
            || (ch >= 0x1161 && ch <= 0x1175)  // 중성
            || (ch >= 0x11A8 && ch <= 0x11C2) // 종성
            || (ch >= 0x3131 && ch <= 0x318E);   // 한글 자모 호환 영역 (ㄱ ~ ㆎ)
    }
}

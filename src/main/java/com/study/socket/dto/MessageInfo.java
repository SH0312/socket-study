package com.study.socket.dto;

/**
 * packageName    : com.study.socket.dto  <br>
 * fileName       : MessageInfo  <br>
 * author         : Eom SeungHo  <br>
 * date           : 2024-06-09  <br>
 * description    : <br><br> 메세지 정보 - 수신자 및 메세지
 */
public class MessageInfo {

    /** 메세지 도착지 - 수정이 불가한 내용 */
    private final String receiver;

    /** 메세지 - 수정이 불가한 내용 */
    private final String message;

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public MessageInfo(String originMessage) {
        String[] split = originMessage.split(":");
        if (split.length != 2) {
            throw new RuntimeException("메세지 포멧이 이상합니다.");
        }
        this.receiver = split[0];
        this.message = split[1];
    }
}

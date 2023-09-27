package server.inuappcenter.kr.common.data.dto;

import lombok.*;

@Getter
public class CommonResponseDto {
    private final String msg;
    public CommonResponseDto (String msg) {
        this.msg = msg;
    }
}

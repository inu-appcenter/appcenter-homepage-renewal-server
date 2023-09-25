package server.inuappcenter.kr.common.data.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonResponseDto {
    private String msg;
    public CommonResponseDto (String msg) {
        this.msg = msg;
    }
}

package server.inuappcenter.kr.data.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SignInResponseDto {
    private String[] token;

    public SignInResponseDto(String[] token) {
        this.token = token;
    }
}

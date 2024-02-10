package server.inuappcenter.kr.data.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class GroupPartListResponseDto {
    private final List<String> parts;

    public GroupPartListResponseDto(List<String> parts) {
        this.parts = parts;
    }
}

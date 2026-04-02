package server.inuappcenter.kr.data.dto.response;

import lombok.Getter;
import server.inuappcenter.kr.data.domain.Group;

@Getter
public class MemberGroupInfoDto {
    private final String role;
    private final Double year;
    private final String part;

    private MemberGroupInfoDto(String role, Double year, String part) {
        this.role = role;
        this.year = year;
        this.part = part;
    }

    public static MemberGroupInfoDto from(Group group) {
        return new MemberGroupInfoDto(
                group.getRole().getRoleName(),
                group.getYear(),
                group.getPart()
        );
    }
}

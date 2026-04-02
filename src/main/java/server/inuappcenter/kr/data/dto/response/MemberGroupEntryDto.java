package server.inuappcenter.kr.data.dto.response;

import lombok.Getter;
import server.inuappcenter.kr.data.domain.Group;

@Getter
public class MemberGroupEntryDto {
    private final Long groupId;
    private final String role;
    private final Double year;
    private final String part;

    private MemberGroupEntryDto(Long groupId, String role, Double year, String part) {
        this.groupId = groupId;
        this.role = role;
        this.year = year;
        this.part = part;
    }

    public static MemberGroupEntryDto from(Group group) {
        return new MemberGroupEntryDto(
                group.getGroup_id(),
                group.getRole().getRoleName(),
                group.getYear(),
                normalizePart(group.getPart())
        );
    }

    private static String normalizePart(String part) {
        if (part == null) return null;
        switch (part.toLowerCase()) {
            case "server": case "android": case "ios": case "web": return "Dev";
            default: return part;
        }
    }
}

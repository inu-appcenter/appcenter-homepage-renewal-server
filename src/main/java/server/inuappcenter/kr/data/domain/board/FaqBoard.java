package server.inuappcenter.kr.data.domain.board;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Getter
@Entity
@NoArgsConstructor
public class FaqBoard extends Board{

    private String part;
    private String question;
    private String answer;

}

package com.goalapa.cacamuca.memberDomain.query.domain.aggregate.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member {

    private Integer memberNo;

    private String memberId;

    private String memberPwd;

    private String memberNickname;

    private String memberEmail;

    private String memberGrant;

    private String memberReportCnt;

    private String memberBirth;
}

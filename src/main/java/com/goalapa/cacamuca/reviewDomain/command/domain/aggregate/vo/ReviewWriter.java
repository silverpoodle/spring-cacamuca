package com.goalapa.cacamuca.reviewDomain.command.domain.aggregate.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ReviewWriter {

    @Column(name = "member_no")
    private Integer reviewWriterMemberId;

}
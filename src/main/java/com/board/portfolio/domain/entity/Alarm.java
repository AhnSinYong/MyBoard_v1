package com.board.portfolio.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(name = "TB_ALARM")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Alarm implements EntityDefaultValues{

    @Id
    @Column(name="ALARM_ID")
    private String alarmId;

    @ManyToOne
    @JoinColumn(name = "TARGET_ACCOUNT")
    @JsonManagedReference
    private Account targetAccount;

    @ManyToOne
    @JoinColumn(name = "TRIGGER_ACCOUNT")
    @JsonManagedReference
    private Account triggerAccount;

    //TODO https://lng1982.tistory.com/279   @Converter를 이용하는 방법 고려
    @Column(name = "EVENT_TYPE")
    @Enumerated(value = EnumType.STRING)
    private AlarmEventType eventType;

    @Column(name="EVENT_CONTENT_ID")
    private String eventContentId;

    @Column(name = "RECIEVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recieveDate;

    @Column(name = "CHECK_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkDate;


    @PrePersist
    @Override
    public void setDefaultValues() {
        this.recieveDate = Optional.ofNullable(this.recieveDate).orElse(new Date());
    }
}

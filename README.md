## 표준 정의서
### 게시판
- SPA web
- vue.js
- spring boot
- jpa
- spring security
- MySQL

### Git Branch
- master/origin
- dev

## DataBase
### TB_MEMBER
 |항목            | 설명                |key type    |data type              | unique   | nullable  |비고               |
 |----------------|--------------------|------------|-----------------------|----------|----------|-------------------|
 |EMAIL           |회원의 이메일        |primary     |varchar(100)             | unique   | not null |   이메일 패턴이여야 함|   
 |PASSWORD     	 |회원의 비밀번호		   |            | varchar(50)            |         | not null |숫자,영문,특문 8자이상 16이하|
 |NICKNAME     	 |회원의 별명		   |           | varchar(30)            | unique   |          |최소4자 이상, 10자이하      |
 |SIGNUP_DATE  	 |회원 가입일		   |           | DATETIME default now() |          | not null |                      |                       |
 |ROLE       	 |회원의 권한		   |           | varchar(30)            |          | not null |member, admin 중 하나  |
 |SOCIAL_ID    	 |소셜 아이디		   |           | varchar(100)            | unique   | null     |소셜에서 제공하는 ID     |

### TB_BOARD
 |항목            | 설명                |key type                            |data type              | unique   | nullable  |비고                 |
 |---------------|---------------------|-----------------------------------|-----------------------|----------|-----------|---------------------|
 |BID            |게시글 식별 번호      | primary(auto inc)                        | int                     |   unique | not null  | 0부터 시작하는 숫자  |
 |TITLE          |게시글의 제목			|                                 |varchar(110)            |           | not null  | 공백금지,50자 이하   |
 |CONTENT     	|게시글의 내용			|                                 |varchar(750)            |           | not null  |공백금지,350자 이하   |
 |LIKE_     	|게시글의 좋아요			|                                 | int default 0          |          | not null  |                      |
 |VIEW       	|게시글의 조회수			 |                               | int default 0           |          | not null |                       |
 |REG_DATE     	|게시글 생성 날짜		    |                                | datetime default now()  |            |not null |                      |
 |UP_DATE     	|게시글 수정 날짜		    |                                | datetime                |           | null   |                        |
 |EMAIL         |게시글의 작성자         |	foreign(TB_MEMBER) on delete set null   |varchar(100)              |            |        |   이메일 패턴이여야 함|
 
 ### TB_COMMENT
 |항목            | 설명                |key type                            |data type              | unique   | nullable  |비고                 |
 |---------------|---------------------|-----------------------------------|-----------------------|----------|-----------|---------------------|
 |CID            |댓글을 식별하는 문자(ID)|primary                           | varchar(40)         | unique     | not null  | UUID                 |
 |BID            |댓글이 작성된 게시물 ID |foreign(TB_BOARD) on delete casecade| int              |             | not null  |0부터 시작하는 숫자     |
 |CONTENT        |댓글의 내용            |                                  | varchar(350)       |           |   not null  |공백금지,150자이하      |
 |LIKE_     	|댓글의 좋아요		    |                                  | int default 0      |           |   not null  |                      |
 |REG_DATE       |댓글의 생성 날짜        |                                 | datetime default now()|        |  not null |                         |
 |UP_DATE       |댓글의 수정 날짜        |                                  |datetime|                      | null |                              |
 |EMAIL         |댓글의 작성자           | foreign(TB_MEMBER) on delete set null| varchar(100)     |         |   null |이메일 패턴이여야 함            |
 
 ### TB_LIKE_BOARD
 |항목            | 설명                          |key type                             |data type                      | unique   | nullable  |비고                 |
 |---------------|-------------------------------|-------------------------------------|-------------------------------|----------|-----------|---------------------|
 |ID             |좋아요 이력을 식별하는 UUID      | primary                             | varchar(40)                            | unique  | not null  |             |
 |BID            | "좋아요"가 눌린 게시물 ID       | foreign(TB_BOARD)  on delete cascade| int                           |          | not null  |                    |
 |EMAIL          | "좋아요"를 누른 유저             | foreign(TB_MEMBER) on delete set null |varchar(100)| 중복 "좋아요" 불가 |          |    null | 이메일 패턴이여야 함  |
 |REG_DATE      	 | "좋아요"를 누른 날짜             |                                     | datetime default now()        |          |not null |                      |
 
 ### TB_LIKE_COMMENT
  |항목            | 설명                          |key type                             |data type                      | unique   | nullable  |비고                 |
  |---------------|-------------------------------|-------------------------------------|-------------------------------|----------|-----------|---------------------|
  |ID             |좋아요 이력을 식별하는 UUID      | primary                             | varchar(40)                    | unique  | not null  |                     |
  |CID            | "좋아요"가 눌린 댓글 ID         | foreign(TB_COMMENT)  on delete cascade| varchar(40)                   |          | not null  |                    |
  |EMAIL          | "좋아요"를 누른 유저             | foreign(TB_MEMBER) on delete set null |varchar(100)| 중복 "좋아요" 불가 |          |  null | 이메일 패턴이여야 함  |
  |REG_DATE      	 | "좋아요"를 누른 날짜          |                                  | datetime default now()        |          |not null |                      |
 
 
 ### TB_FILE
 |항목            | 설명                          |key type                             |data type                      | unique   | nullable  |비고                 |
 |---------------|-------------------------------|-------------------------------------|-------------------------------|----------|-----------|---------------------|
 |FID            | 파일을 식별하는 UUID            |primary                              | varchar(40)                  | unique    | not null |                      |
 |BID            | 파일이 저장된 게시물 ID         |foreign(TB_BOARD) on delete cascade   | int                         |           |  not null   |                   |
 |ORIGIN_NAME    | 파일의 원래 이름                |                                     |  varchar(70)                 |             |not null      |  공백금지, 확장자 필요|
 |SAVE_NAME     | 파일이 서버에 저장된 이름(FID+확장자)|                                  |  varchar(70)                  |   unique    |   not null   공백금지, 확장자 필요  |
 |EXTENSION      | 파일의 확장자                   |                                   | varchar(20)                   |              |not null   |     10글자 이상불가     |
 |DOWN           | 파일 다운로드 횟수              |                                    |   int default 0             |               | not null |                    |
 
 ### TB_ALARM
 |항목            | 설명                                           |key type                             |data type                      | unique   | nullable  |비고                 |
 |---------------|------------------------------------------------|-------------------------------------|-------------------------------|----------|-----------|---------------------|
 |AID            | 알람을 식별하는 UUID                             |primary                              | varchar(40)                   | unique   | not null  |                    |
 |TARGET_MEMBER   | 알람을 받은 회원                                |foreign(TB_MEMBER) on delete cascade | varchar(100)                  |          | not null  |  이메일 패턴      |
 |ALARM_TRIGGER   | 알람을 발생시킨 대상(시스템, 관리자, 다른 회원 등) |                                     |  varchar(100)                 |         |  not null  |  40자 이상 불가         |
 |EVENT_TYPE      | 어떤 알람 이벤트인지(좋아요 알림, 대댓글알림 , 시스템 공지 등)|                           |  varchar(100)                |          | not null   |  40자 이상 불가 |
 |ALARM_DATE      | 알람을 받은 날짜                                 |                                   | datetime default now()         |         |  not null  |                 |
 |ALARM_CHECK_DATE| 알람을 읽은 날짜                                 |                                    | datetime                       |          | null      |                 |
 
 
 
 ### SQL(DDL)
~~~
create table TB_MEMBER(
    EMAIL varchar(100),
    PASSWORD varchar(50) not null ,
    NICKNAME varchar(30) unique ,
    SIGNUP_DATE datetime default now(),
    ROLE varchar(30) default 'ROLE_MEMBER',
    SOCIAL_ID varchar(100) unique,
    primary key(EMAIL)
);
~~~

~~~
create table TB_BOARD(
    BID int auto_increment,
    TITLE varchar(110) not null ,
    CONTENT varchar(750) not null ,
    LIKE_ int default 0,
    VIEW int default 0,
    REG_DATE datetime default now(),
    UP_DATE datetime,
    EMAIL varchar(100) ,
    primary key (BID),
    foreign key (EMAIL) REFERENCES TB_MEMBER(EMAIL) on delete set null
);
~~~
~~~
create table TB_COMMENT(
    CID varchar(40),
    BID int not null ,
    CONTENT varchar(350) not null ,
    LIKE_ int default 0,
    REG_DATE datetime default now(),
    UP_DATE datetime,
    EMAIL varchar(100),
    primary key (CID),
    foreign key (EMAIL) REFERENCES TB_MEMBER(EMAIL) on delete set null
);
~~~
~~~
create table TB_LIKE_BOARD(
    ID varchar(40),
    BID int not null ,
    EMAIL varchar(100) ,
    REG_DATE datetime default now(),
    primary key (ID),
    foreign key (BID) REFERENCES TB_BOARD(BID) on delete cascade ,
    foreign key (EMAIL) REFERENCES TB_MEMBER(EMAIL) on delete set null
);
~~~
~~~
create table TB_LIKE_COMMENT(
    ID varchar(40),
    CID varchar(40) not null ,
    EMAIL varchar(100) ,
    REG_DATE datetime default now(),
    primary key (ID),
    foreign key (CID) REFERENCES TB_COMMENT(CID) on delete cascade ,
    foreign key (EMAIL) REFERENCES TB_MEMBER(EMAIL) on delete set null
);
~~~
~~~
create table TB_FILE(
    FID varchar(40),
    BID int not null,
    ORIGIN_NAME varchar(70) not null ,
    SAVE_NAME varchar(70) unique ,
    EXTENSION varchar(20) not null ,
    DOWN int default 0,
    primary key(FID),
    foreign key (BID) REFERENCES TB_BOARD(BID) on delete cascade
);
~~~
~~~
create table TB_ALARM(
    AID varchar(40),
    TARGET_MEMBER varchar(100) not null ,
    ALARM_TRIGGER varchar(100) not null ,
    EVENT_TYPE varchar(100) not null ,
    ALARM_DATE datetime default now(),
    ALARM_CHECK_DATE datetime,
    primary key (AID),
    foreign key (TARGET_MEMBER) REFERENCES TB_MEMBER(EMAIL) on delete cascade
)
~~~
 
 
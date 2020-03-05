# Database
<details>
 <summary>DDL</summary>
<p>
 
 ~~~
create table TB_ACCOUNT(
    EMAIL varchar(100),
    PASSWORD varchar(100) not null ,
    NICKNAME varchar(30) unique ,
    SIGNUP_DATE datetime default now(),
    ROLE varchar(30) default 'MEMBER',
    IS_SOCIAL boolean default false,
    AUTH_KEY varchar(40) not null,
    IS_AUTH boolean default false,
    primary key(EMAIL)
);
~~~

~~~
create table TB_BOARD(
     BOARD_ID int auto_increment,
     TITLE varchar(110) not null ,
     CONTENT varchar(5000) not null ,
     LIKE_ int default 0,
     VIEW int default 0,
     REG_DATE datetime default now(),
     UP_DATE datetime,
     EMAIL varchar(100) ,
     primary key (BOARD_ID),
     foreign key (EMAIL) REFERENCES TB_ACCOUNT(EMAIL) on delete set null
);
~~~
~~~
create table TB_COMMENT(
    COMMENT_ID int auto_increment,
    BOARD_ID int not null ,
    CONTENT varchar(350) not null ,
    LIKE_ int default 0,
    REG_DATE datetime default now(),
    UP_DATE datetime,
    EMAIL varchar(100),
    GROUP_ int not null, ##COMMENT_ID 값이 들어감
    DEL_PARENT_CNT int default 0,
    HAS_DEL_TYPE_PARENT boolean,
    TYPE varchar(30) not null, ##PARENT_ID가 NULL 인데 TYPE이 CHILD면 알수없음 댓글 만들면 됨
    primary key (COMMENT_ID),
    foreign key (BOARD_ID) REFERENCES  TB_BOARD(BOARD_ID) on delete cascade ,
    foreign key (EMAIL) REFERENCES TB_ACCOUNT(EMAIL) on delete set null
);
~~~
~~~
create table TB_LIKE_BOARD(
    LIKE_BOARD_ID varchar(40),
    BOARD_ID int not null ,
    EMAIL varchar(100) ,
    REG_DATE datetime default now(),
    primary key (LIKE_BOARD_ID),
    foreign key (BOARD_ID) REFERENCES TB_BOARD(BOARD_ID) on delete cascade ,
    foreign key (EMAIL) REFERENCES TB_ACCOUNT(EMAIL) on delete set null
);
~~~
~~~
create table TB_LIKE_COMMENT(
    LIKE_COMMENT_ID varchar(40),
    COMMENT_ID int not null ,
    EMAIL varchar(100) ,
    REG_DATE datetime default now(),
    primary key (LIKE_COMMENT_ID),
    foreign key (COMMENT_ID) REFERENCES TB_COMMENT(COMMENT_ID) on delete cascade ,
    foreign key (EMAIL) REFERENCES TB_ACCOUNT(EMAIL) on delete set null
);
~~~
~~~
create table TB_FILE_ATTACHMENT(
    FILE_ID varchar(40),
    BOARD_ID int not null,
    ORIGIN_NAME varchar(70) not null ,
    SAVE_NAME varchar(70) unique ,
    EXTENSION varchar(20) not null ,
    DOWN int default 0,
    SAVE_DATE datetime default now(),
    EMAIL varchar(100) not null,
    primary key(FILE_ID),
    foreign key (BOARD_ID) REFERENCES TB_BOARD(BOARD_ID) on delete cascade,
    foreign key (EMAIL) REFERENCES TB_ACCOUNT(EMAIL) on delete cascade
);
~~~
~~~
create table TB_ALARM(
    ALARM_ID varchar(40),
    TARGET_ACCOUNT varchar(100) not null ,
    TRIGGER_ACCOUNT varchar(100) ,
    EVENT_TYPE varchar(100) not null ,
    EVENT_CONTENT_ID varchar(100) not null ,
    RECIEVE_DATE datetime default now(),
    CHECK_DATE datetime,
    primary key (ALARM_ID),
    foreign key (TARGET_ACCOUNT) REFERENCES TB_ACCOUNT(EMAIL) on delete cascade,
    foreign key (TRIGGER_ACCOUNT) REFERENCES TB_ACCOUNT(EMAIL) on delete set null
);
~~~
 
</p>
</details>

<details>
 <summary>TB_ACCOUNT</summary>
<p>

|항목            | 설명                |key type    |data type              | unique   | nullable  |비고               |
 |----------------|--------------------|------------|-----------------------|----------|----------|-------------------|
 |EMAIL           |회원의 이메일        |primary     |varchar(100)             | unique   | not null |   이메일 패턴이여야 함, 최소 5자, 최대 40자|   
 |PASSWORD     	 |회원의 비밀번호		   |            | varchar(100)            |         | not null |숫자,영문,특문 8자이상 16이하 -> 암호화|
 |NICKNAME     	 |회원의 별명		   |           | varchar(30)            | unique   |          |최소4자 이상, 10자이하      |
 |SIGNUP_DATE  	 |회원 가입일		   |           | DATETIME default now() |          | not null |                      |                       |
 |ROLE       	 |회원의 권한		   |           | varchar(30) default 'MEMBER'|          | not null |MEMBER, ADMIN, SYSTEM 중 하나  |
 |IS_SOCIAL    	 |소셜 아이디 여부   |           | boolean default false    |          | not null       |             |
 |AUTH_KEY       |이메일 인증을 위한 키 |           | varchar(40)           |            | not null | 무작위 문자열   |
 |IS_AUTH        |이메일 인증을 한 계정인지 명시|    | boolean default false  |             | not null|  이메일 인증을 마치면 true  |

</p>
</details>

<details>
 <summary>TB_BOARD</summary>
<p>
 
  |항목            | 설명                |key type                            |data type              | unique   | nullable  |비고                 |
 |---------------|---------------------|-----------------------------------|-----------------------|----------|-----------|---------------------|
 |BOARD_ID            |게시글 식별 번호      | primary(auto inc)                        | int                     |   unique | not null  |   |
 |TITLE          |게시글의 제목			|                                 |varchar(110)            |           | not null  | 공백금지,50자 이하   |
 |CONTENT     	|게시글의 내용			|                                 |varchar(5000)            |           | not null  |                   |
 |LIKE_     	|게시글의 좋아요			|                                 | int default 0          |          | not null  |                      |
 |VIEW       	|게시글의 조회수			 |                               | int default 0           |          | not null |                       |
 |REG_DATE     	|게시글 생성 날짜		    |                                | datetime default now()  |            |not null |                      |
 |UP_DATE     	|게시글 수정 날짜		    |                                | datetime                |           | null   |                        |
 |EMAIL         |게시글의 작성자         |	foreign(TB_ACCOUNT) on delete set null   |varchar(100)              |            |        |   이메일 패턴이여야 함|

 
</p>
</details>


<details>
 <summary>TB_COMMENT</summary>
<p>
 
 |항목            | 설명                |key type                            |data type              | unique   | nullable  |비고                 |
 |---------------|---------------------|-----------------------------------|-----------------------|----------|-----------|---------------------|
 |COMMENT_ID            |댓글을 식별하는 ID|primary(auto inc)                | int         | unique     | not null  |                  |
 |BOARD_ID            |댓글이 작성된 게시물 ID |foreign(TB_BOARD) on delete casecade| int              |             | not null  |     |
 |CONTENT        |댓글의 내용            |                                  | varchar(350)       |           |   not null  |공백금지,150자이하      |
 |LIKE_     	|댓글의 좋아요		    |                                  | int default 0      |           |   not null  |                      |
 |REG_DATE       |댓글의 생성 날짜        |                                 | datetime default now()|        |  not null |                         |
 |UP_DATE       |댓글의 수정 날짜        |                                  |datetime|                      | null |                              |
 |EMAIL         |댓글의 작성자           | foreign(TB_ACCOUNT) on delete set null| varchar(100)     |         |   null |이메일 패턴이여야 함            |
 |GROUP_        |댓글의 그룹 번호(COMMNET_ID)|                              |int                  |           |not null      |대댓글 관계를 구분하기 위함|
 |DEL_PARENT_CNT|삭제된 부모댓글의 개수    |                                | int default 0        |           |not null||
 |HAS_DEL_TYPE_PARENT |삭제된 부모댓글들 중 타입 PARENT가 있는지 |           | boolean             |            |          |                       |
 |TYPE          |댓글이 부모인지, 자식인지 구분|                               |varchar(30)        |           |not null|              |
 
 
</p>
</details>
 
 
 <details>
 <summary>TB_LIKE_BOARD</summary>
<p>
 
 |항목            | 설명                          |key type                             |data type                      | unique   | nullable  |비고                 |
 |---------------|-------------------------------|-------------------------------------|-------------------------------|----------|-----------|---------------------|
 |LIKE_BOARD_ID             |좋아요 이력을 식별하는 UUID      | primary                             | varchar(40)                   | unique  | not null  |             |
 |BOARD_ID            | "좋아요"가 눌린 게시물 ID       | foreign(TB_BOARD)  on delete cascade| int                           |          | not null  |                    |
 |EMAIL          | "좋아요"를 누른 유저             | foreign(TB_ACCOUNT) on delete set null |varchar(100)                 |          |    null | 이메일 패턴이여야 함  |
 |REG_DATE      	 | "좋아요"를 누른 날짜             |                                     | datetime default now()     |          |not null |                      |
 
 
</p>
</details>
 
 <details>
 <summary>TB_LIKE_COMMENT</summary>
<p>
 
  |항목            | 설명                          |key type                             |data type                      | unique   | nullable  |비고                 |
  |---------------|-------------------------------|-------------------------------------|-------------------------------|----------|-----------|---------------------|
  |LIKE_COMMENT_ID             |좋아요 이력을 식별하는 ID      | primary                             | int                    | unique  | not null  |                     |
  |COMMENT_ID            | "좋아요"가 눌린 댓글 ID         | foreign(TB_COMMENT)  on delete cascade| varchar(40)                   |          | not null  |                    |
  |EMAIL          | "좋아요"를 누른 유저             | foreign(TB_ACCOUNT) on delete set null |varchar(100)|                |  null | 이메일 패턴이여야 함  |
  |REG_DATE      	 | "좋아요"를 누른 날짜          |                                  | datetime default now()        |          |not null |                      |
 
 
</p>
</details>
 
  
 <details>
 <summary>TB_FILE_ATTACHMENT</summary>
<p>
 
 |항목            | 설명                          |key type                             |data type                      | unique   | nullable  |비고                 |
 |---------------|-------------------------------|-------------------------------------|-------------------------------|----------|-----------|---------------------|
 |FILE_ID            | 파일을 식별하는 UUID            |primary                              | varchar(40)                  | unique    | not null |                      |
 |BOARD_ID            | 파일이 저장된 게시물 ID         |foreign(TB_BOARD) on delete cascade   | int                         |           |  not null   |                   |
 |ORIGIN_NAME    | 파일의 원래 이름                |                                     |  varchar(70)                 |             |not null      |  공백금지, 확장자 필요|
 |SAVE_NAME     | 파일이 서버에 저장된 이름(FID+확장자)|                                  |  varchar(70)                  |   unique    |   not null   공백금지, 확장자 필요  |
 |EXTENSION      | 파일의 확장자                   |                                   | varchar(20)                   |              |not null   |     10글자 이상불가     |
 |DOWN           | 파일 다운로드 횟수              |                                    |   int default 0             |               | not null |                    |
 |SAVE_DATE      | 파일이 저장된 날짜              |                                    |  datetime default now()     |                | not null|                    |
 |EMAIL          | 파일을 저장한 사용자의 이메일    | foreign(TB_ACCOUNT) on delete cascade |  varchar(100)              |              |  not null |                   |
 
 
</p>
</details>

 <details>
 <summary>TB_ALARM</summary>
<p>
 
 |항목            | 설명                                           |key type                             |data type                      | unique   | nullable  |비고                 |
 |---------------|------------------------------------------------|-------------------------------------|-------------------------------|----------|-----------|---------------------|
 |ALARM_ID            | 알람을 식별하는 UUID                             |primary                              | varchar(40)                   | unique   | not null  |                    |
 |TARGET_ACCOUNT   | 알람을 받은 대상(시스템, 관리자, 다른 회원 등)      |foreign(TB_ACCOUNT) on delete cascade | varchar(100)                  |          | not null  |  대상 ID      |
 |TRIGGER_ACCOUNT   | 알람을 발생시킨 대상(시스템, 관리자, 다른 회원 등) |foreign(TB_ACCOUNT) on delete cascade |  varchar(100)                 |         |  null  |  대상 ID         |
 |EVENT_TYPE      | 어떤 알람 이벤트인지(좋아요 알림, 대댓글알림 , 시스템 공지 등)|                           |  varchar(100)                |          | not null   |  40자 이상 불가 |
 |EVENT_CONTENT_ID   | 알람이벤트가 발생한 컨텐츠의 ID                  |                                   |  varchar(100)                  |         |not null    |                |
 |RECIEVE_DATE      | 알람을 받은 날짜                                 |                                   | datetime default now()         |         |  not null  |                 |
 |CHECK_DATE      | 알람을 읽은 날짜                                 |                                    | datetime                       |          | null      |                 |

 
</p>
</details>
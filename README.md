## 표준 정의서
## 할 것
- 커스텀한 예외를 못던지는 경우들을 해결해야해
- security의 exception 처리를 추가해야함
    - @initBinder로 처리하는 validator가 떨구는 예외도 처리해서 내보내야함
    - message관련, mail관련 exception 발생하면 클라이언트에 "메일발송실패" 뜨게하자, rollback도 되야하고
- 메일인증 링크 클릭시 보일 화면 추가
- 인증유효시간 30분이 지날경우를 적용
    - 스레드세이프하게 다시 생각해보자(syncronized?)
- 페이징 관련 repository에서 start 값 -1하는거 좀더 보기좋게 고쳐야함
- @FileSize , @FileExtension 대충 복붙한거 리팩토링필요함
    - extension 유효성 검사에서 . 으로 나누고 length 2아닐때 예외던지게 했는데 고쳐야함(ex: abc.def.hwp)
- 프로퍼티즈 이용해서 확장자 제한 관리를 하는 것 필요
- @AuthenticationPrincipal이 AccountDetails에 자동으로 주입이 안되네 
    - 내가 구성한 AccountDetails가 문제일까?? 고민이 필요함
- @PreAuthorize 에 대한 예외처리가 필요함
- post.js에서 board_id값 초기화처리를 해보자(지금은 초기화로 값바뀌면 한번더 호출해서 오류남)
- boardService의 파일다운로드에서 byte[4096]인거 설정파일에서 읽게 수정해야함
- deletePost()에서 검증관력 로직을 분리하고 싶어
- postView boardId 이슈 ----> vue router를 써야 근본적인 해결이 가능할듯
- axios통해서 파일다운로드를 구현하고, down 값이 실시간으로 반영되게 만들자
- 쿠키유효시간이 다되서 소멸할때 로그인 정보도 갱신되게 만들자
- 삭제된 댓글처리가 시원치않음....db설계 변경이 필요할듯함
- ERD 이미지 파일 수정해야함
- 패키지, 클래스 관계도 그려보자 순환참조라던가 양방향참조가 존재하는지...
- Controller에 @PathVariable, @RequestParam 에 대한 @CustomValidator 사용을 고려하자

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
##프로젝트 구성
- 내용추가 필요


## DataBase
### ERD
![portfolio_erd_v3](./README_RESOURCES/portfolio_erd_v3.png)
### TB_ACCOUNT
 |항목            | 설명                |key type    |data type              | unique   | nullable  |비고               |
 |----------------|--------------------|------------|-----------------------|----------|----------|-------------------|
 |EMAIL           |회원의 이메일        |primary     |varchar(100)             | unique   | not null |   이메일 패턴이여야 함, 최소 5자, 최대 40자|   
 |PASSWORD     	 |회원의 비밀번호		   |            | varchar(100)            |         | not null |숫자,영문,특문 8자이상 16이하 -> 암호화|
 |NICKNAME     	 |회원의 별명		   |           | varchar(30)            | unique   |          |최소4자 이상, 10자이하      |
 |SIGNUP_DATE  	 |회원 가입일		   |           | DATETIME default now() |          | not null |                      |                       |
 |ROLE       	 |회원의 권한		   |           | varchar(30) default 'MEMBER'|          | not null |MEMBER, ADMIN, SYSTEM 중 하나  |
 |SOCIAL_ID    	 |소셜 아이디		   |           | varchar(100)            | unique   | null     |소셜에서 제공하는 ID     |
 |AUTH_KEY       |이메일 인증을 위한 키 |           | varchar(40)           |            | not null | 무작위 문자열   |
 |IS_AUTH        |이메일 인증을 한 계정인지 명시|    | boolean default false  |             | not null|  이메일 인증을 마치면 true  |

### TB_BOARD
 |항목            | 설명                |key type                            |data type              | unique   | nullable  |비고                 |
 |---------------|---------------------|-----------------------------------|-----------------------|----------|-----------|---------------------|
 |BOARD_ID            |게시글 식별 번호      | primary(auto inc)                        | int                     |   unique | not null  |   |
 |TITLE          |게시글의 제목			|                                 |varchar(110)            |           | not null  | 공백금지,50자 이하   |
 |CONTENT     	|게시글의 내용			|                                 |varchar(750)            |           | not null  |공백금지,350자 이하   |
 |LIKE_     	|게시글의 좋아요			|                                 | int default 0          |          | not null  |                      |
 |VIEW       	|게시글의 조회수			 |                               | int default 0           |          | not null |                       |
 |REG_DATE     	|게시글 생성 날짜		    |                                | datetime default now()  |            |not null |                      |
 |UP_DATE     	|게시글 수정 날짜		    |                                | datetime                |           | null   |                        |
 |EMAIL         |게시글의 작성자         |	foreign(TB_ACCOUNT) on delete set null   |varchar(100)              |            |        |   이메일 패턴이여야 함|
 
 ### TB_COMMENT
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
 
 ### TB_LIKE_BOARD
 |항목            | 설명                          |key type                             |data type                      | unique   | nullable  |비고                 |
 |---------------|-------------------------------|-------------------------------------|-------------------------------|----------|-----------|---------------------|
 |LIKE_BOARD_ID             |좋아요 이력을 식별하는 UUID      | primary                             | varchar(40)                   | unique  | not null  |             |
 |BOARD_ID            | "좋아요"가 눌린 게시물 ID       | foreign(TB_BOARD)  on delete cascade| int                           |          | not null  |                    |
 |EMAIL          | "좋아요"를 누른 유저             | foreign(TB_ACCOUNT) on delete set null |varchar(100)                 |          |    null | 이메일 패턴이여야 함  |
 |REG_DATE      	 | "좋아요"를 누른 날짜             |                                     | datetime default now()     |          |not null |                      |
 
 ### TB_LIKE_COMMENT
  |항목            | 설명                          |key type                             |data type                      | unique   | nullable  |비고                 |
  |---------------|-------------------------------|-------------------------------------|-------------------------------|----------|-----------|---------------------|
  |LIKE_COMMENT_ID             |좋아요 이력을 식별하는 ID      | primary                             | int                    | unique  | not null  |                     |
  |COMMENT_ID            | "좋아요"가 눌린 댓글 ID         | foreign(TB_COMMENT)  on delete cascade| varchar(40)                   |          | not null  |                    |
  |EMAIL          | "좋아요"를 누른 유저             | foreign(TB_ACCOUNT) on delete set null |varchar(100)|                |  null | 이메일 패턴이여야 함  |
  |REG_DATE      	 | "좋아요"를 누른 날짜          |                                  | datetime default now()        |          |not null |                      |
 
 
 ### TB_FILE_ATTACHMENT
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
 
 
 
 ### TB_ALARM
 |항목            | 설명                                           |key type                             |data type                      | unique   | nullable  |비고                 |
 |---------------|------------------------------------------------|-------------------------------------|-------------------------------|----------|-----------|---------------------|
 |ALARM_ID            | 알람을 식별하는 UUID                             |primary                              | varchar(40)                   | unique   | not null  |                    |
 |TARGET_ACCOUNT   | 알람을 받은 대상(시스템, 관리자, 다른 회원 등)      |foreign(TB_ACCOUNT) on delete cascade | varchar(100)                  |          | not null  |  대상 ID      |
 |TRIGGER_ACCOUNT   | 알람을 발생시킨 대상(시스템, 관리자, 다른 회원 등) |foreign(TB_ACCOUNT) on delete cascade |  varchar(100)                 |         |  null  |  대상 ID         |
 |EVENT_TYPE      | 어떤 알람 이벤트인지(좋아요 알림, 대댓글알림 , 시스템 공지 등)|                           |  varchar(100)                |          | not null   |  40자 이상 불가 |
 |EVENT_CONTENT_ID   | 알람이벤트가 발생한 컨텐츠의 ID                  |                                   |  varchar(100)                  |         |not null    |                |
 |RECIEVE_DATE      | 알람을 받은 날짜                                 |                                   | datetime default now()         |         |  not null  |                 |
 |CHECK_DATE      | 알람을 읽은 날짜                                 |                                    | datetime                       |          | null      |                 |
 
 
 ### SQL(DDL)
~~~
create table TB_ACCOUNT(
    EMAIL varchar(100),
    PASSWORD varchar(100) not null ,
    NICKNAME varchar(30) unique ,
    SIGNUP_DATE datetime default now(),
    ROLE varchar(30) default 'MEMBER',
    SOCIAL_ID varchar(100) unique,
    AUTH_KEY varchar(40) not null,
    IS_AUTH boolean default false,
    primary key(EMAIL)
);
~~~

~~~
create table TB_BOARD(
     BOARD_ID int auto_increment,
     TITLE varchar(110) not null ,
     CONTENT varchar(750) not null ,
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
### 사용한 테스트 SQL
~~~
## table 제거
drop table TB_ALARM;
drop table TB_FILE_ATTACHMENT;
drop table TB_LIKE_BOARD;
drop table TB_LIKE_COMMENT;
drop table TB_COMMENT;
drop table TB_BOARD;
drop table TB_ACCOUNT;
~~~

 ## Spring Security
 ### Filter
 |경로                    |예외경로            |Filter         | Provider                                  |success                             |fail                      |
 |------------------------|-------------------|-------------|--------------------------------------------|-------------------------------------|-----------------------|
 |/api/account/signIn     |                   |SignInFilter   | 로그인 성공 여부 판단                      | jwt토큰을 쿠키에 담아 반환           | Exception 발생        |
 |/api/**                 |/api/account/signIn|JwtFilter      | 토큰 유효성 판단(토큰이 없는 경우는 success)| chain.doFilter()                    | Exception 발생        |
 
 ## 생각해볼 것
 - TB_ALARM에서 TB_CONTENT_ID 는 외래키로 설정하지 않았다.
    - COMMENT,BOARD,LIKE 등등 많은 교차엔티티와 관계가 발생되고 불필요한 칼럼이 예상되었기때문
    - 이를 해결할 더 좋은 방법이 있었을까?
    
 - TB_Account에서 @OneToMany가 많은데, 이게 성능에 악영향을 주지 않을까?
 - TB_EMAIL 에서 기본키를 email로 하였는데, 이것이 인덱싱 관련해서 문제를 일으킬 수 있지 않을까?
    - MySQL은 기본키에 디폴트 인덱스를 거는 개념이 혹시 존재하나?
    
 - ResponseEntity를 사용하지 않고 응답을 구성하는법...
    - 단순히 클래스 리턴이 아니라 에러, 응답코드 등의 정보를 부여
 - axios를 export 하는 파일들 안에서 명시적으로 쓰는 것 고려
    - 경고 밑줄이 그어짐, import 같은 것을 써야 할까?
    
 - jwt 쿠키가 탈취 당할 경우를 고려해서 매 요청마다 새로운 토큰을 주는 게 나을까?
 
 - contextHolder 안에 있는 authentication은 요청이 끝나면 사라지게 되는걸까???
 - jpa의 update를 하지 않고 findby 이후 setter를 이용해도 괜찮은걸까?
 - @initBinder에서 validator의 클래스비교가 false가 뜨면 에러가 발생하는데 이 에러를 캐치해주면 하나의 @initBinder에서 깔끔하게 처리 가능하지 않을까?
     - @initBinder(name) 에서 name은 모델명, 클래스명을 타겟하는구나

- JwtCookieUtil을 제대로 구성한게 맞는걸까?
    - jwtTokenName에 @Value를쓰기 위해서 클래스에 @Component를 사용
    - 그럼에도 불구하고 @Value가 null로 보여서 생성자에 @Autowired를 걸고 @Value를 씀
        - 안됬던 이유는 static 메소드와 관련이 있을까???
        - 스프링 빈 라이프 사이클에 대한 이해 필요
        - @autowired 더 정확히 이해하자(메소드,생성자에 대한 쓰임)
        - 스프링 빈 라이프 사이클에 대한 이해 필요
        - @value가 JwtCookieUtil에서 왜 안되었던거지??
        - 참고  https://codeday.me/ko/qa/20190411/301654.html 
        
- 커스텀 @Valid 만들어서 사용하는 거에 대한 의견이 궁금
- @autowired를 생성자 주입으로 써야하는 이유
    - https://yaboong.github.io/spring/2019/08/29/why-field-injection-is-bad/
    - 꽤 생각해볼만한 이야기
    
- new Thread에서 @Transactional이 안돼서 repository에 사용했음 개선점은 무엇이 있을까?

- spa형식으로 만들때 url을 통한 접근성이 떨어질 수 있겠네 이에 대한 해결법이 있을까?
    - 자바스크립트로 location을 정의해줄까?

- 클라이언트에서 file을 리스트([])로 만들어서 formData에 담아 보냈지만 컨버터에서는 "[Object object]"라는 문자열로 인식했음
    - 이를 해결하기 위해서 formData에 file[0], file[1] ... 이런식으로 여러개의 값을 추가해줫음
    - List는 왜 안됬던걸까?
    
- 컨트롤러에서 원시타입을 @Valid 할 방법은 무엇이 있을까???
- 뭔가 boardId, fileId 이런거에서 느낀건데, @Valid로 구지 확인안해도 로직상에서 어쩔수 없이 orElsethrow로 걸러지는거 같아 앞으로는 고려해보자
- vue 에서 :key 값을 통해서 랜더링을 관리하는 구나, 그런데 updatePost.js에서 왜 deleteFile()했을때 업데이트가 안됬지????
    - 이를 해결하기 위한 방법으로 :key의 값을 바꿔주는법
    - this.$forceUpdate() 를 실행하는 법이 있다고함
- @Valid를 클래스 안에 클래스에서 사용하기도 했는데 이에 대한 생각...좋은 패턴?(BoardDTO.Update)
- controllerAdvice에 @ModelAttribute를 추가 했는데 이게 좋은 패턴일까???
- 커스텀한 느낌이 나는 검증로직은 @initBinder의 validator같은 느낌으로 처리할수 없을까???
    - 비밀번호 비교로직은 가능(회원가입)
    - deleteComment같은 경우는 모르겠어.....억지로 객체하나에 다 담아서 클라이언트에서 쏴줘야하나???
- commentDelete에서 Transactional의 설정이 필요하지않을까? 락을 건다거나....(delete의 경우는 락을걸어도 사양에 큰 문제는 없을거같아)    
- 나는 db를 잘 설계한걸까??(성능적인면에서?)

### 메모
- JPA 
    - findTop3ByBoardAndLikeGreaterThanOrderByLikeDesc
    - @Transactional이 끝나는 순간 엔티티의 setter로 설정한게 db에 반영되는구나
    
- valid 
    - message 정의하던 부분     
    ~~~
          if (isExistEmail) {
              cxt.disableDefaultConstraintViolation();
              cxt.buildConstraintViolationWithTemplate(
                      MessageFormat.format("이미 존재하는 이메일 입니다. \n ({0})", email))
                      .addConstraintViolation();
          }
    ~~~
    - @valid 를 사용하는 여러가지 사례 
            - https://www.logicbig.com/how-to/code-snippets/jcode-bean-validation-valid.html
            - 이거 말고도 다양한 쓰임이 가능할듯 @Valid + a 느낌으로 사용
    - @NotBlank는 String에 대해서만 정상동작함, Long, Integer는 NotNull 정도로 사용하자
    - @PathVariable과 @RequestParam에서 @Valid를 사용하는 방법
        - https://www.baeldung.com/spring-validate-requestparam-pathvariable
            - Spring의 경우 @Bean을 설정해줘야하는데 Boot에서 해보니깐 예외가 발생한다 왜그런거지???
        - https://cnpnote.tistory.com/entry/SPRING-Spring-MVC-PathVariable-%EA%B0%92%EC%9D%84-%EA%B2%80%EC%A6%9D%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95%EC%9D%80-%EB%AC%B4%EC%97%87%EC%9E%85%EB%8B%88%EA%B9%8C
        - Controller 파라미터에 사용하기 위해서 @CustomValidator에 ElementType.PARMETER 를 추가하였음
- socket
    - Websocket은 IE10이상부터인데 SocketJS는 IE6이상부터 지원한다고함
    
- enum 
    - @converter를 사용하는 방법 고려
        - https://lng1982.tistory.com/279   @Converter를 이용하는 방법 고려
# @Valid layer 구성
- 모든 DTO(VO)에서 검증된 값만을 받도록 하는 @Valid layer를 구성하였습니다.
- @Valid 어노테이션을 적극(커스텀) 사용하여 검증(validate)코드를 줄였습니다.
- 서비스 로직에서 검증(validate) 관련된 코드를 최대한 분리하였습니다.
- 사용법은 블로그를 참조해주세요. 
    - [@Valid 활용하기](https://dingdongdeng.tistory.com/7).
    - [@Valid와 MessageSource 사용하기](https://dingdongdeng.tistory.com/19).

## StoreManager
- StoreConfig에서 bean으로 생성하여 초기에 어떤 값을 캐싱할지 설정합니다.
    - 아래 코드는 게시판 목록을 캐싱하기 위해 임의 개수만큼의 게시물을 쿼리하여 StoreManager에게 전달하고 있습니다.
    - 전달할때는 Store 클래스를 사용하여 전달합니다.
    
- 아래는 사용 예시입니다.
    - @PasswordCompare  : 비밀번호, 비밀번호 확인 비교
        ~~~
        @Data
        @PasswordCompare(message = "{password.compare}")
        public class PasswordDTO {
            @NotBlank(message = "{password.not.blank}")
            @Pattern(regexp ="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$"
                    ,message = "{password.pattern}")//영문,숫자,특문 8글자 이상
            private String password;
            @NotBlank(message = "{passwordCheck.not.blank}")
            private String passwordCheck;
        }
        ~~~
    - @EmailDuplicate : 이메일 중복 확인
        ~~~
          public static class SignUp extends PasswordDTO{
              @NotBlank(message = "{nickname.not.blank}")
              @Size(min=5,max=10,message = "{nickname.size}")
              @NicknameDuplicate(message = "{nickname.duplicate}")
              private String nickname;
      
              @NotBlank(message = "{email.not.blank}")
              @Email(message = "{email.email}")
              @Size(min=5,max=40,message = "{email.size}")
              @EmailDuplicate(message = "{email.duplicate}")
              private String email;
          }
        ~~~
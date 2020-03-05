# AuthMailManager
- 발송된 인증메일의 유효시간을 별개의 스레드에서 관리합니다.
- 서비스로직에서 인증메일 유효시간에 대한 관심사를 분리할 수 있습니다.
- 메일이 발송되면 AuthMailManager의 List authMailList에 인증메일 정보가 추가되고, 스레드가 동작합니다.
- 스레드는 적절한 로직으로 sleep을 하며 리소스를 절약합니다.
    - 예) 유효시간이 5분, 8분, 14분 남은 메일이 순차적으로 있다면,  
    5분간 sleep후 처리, 다시 3분간 sleep후 처리, 다시 6분간 sleep후 처리합니다.
        처리내용은 해당 인증메일을 무효화할 것인지, 패스할 것 인지 입니다. 
~~~
@Component
public class AuthMailManager implements Runnable{

    @Autowired
    private AccountRepository accountRepository;

    private Long limitTime;
    private final List<AuthMail> authMailList = new ArrayList<>();
    private boolean isWork;

    @Autowired
    public AuthMailManager(@Value("${mail.auth.limit}") Long limit){
        this.limitTime = limit;
    }

    @PostConstruct
    private void init(){
        startManage();
    }
    private void startManage(){
        setAuthMailList();
        if(isHasItem(authMailList)){
            startNewThread();
        }
    }
    private boolean isHasItem(List list){
        return list.size()!=0;
    }

    public void startNewThread(){
        Thread thread = new Thread(this);
        thread.start();
    }

    private void setAuthMailList(){
        List<Account> accountList = accountRepository.findAllByIsAuthOrderBySignUpDateAsc(false);
        for(Account account : accountList){
            authMailList.add(parseFromAccount(account));
        }
    }

    public void startManage(AuthMail authMail){
        addAuthMailList(authMail);
        if(isWork){
            return;
        }
        startNewThread();
    }
    private void addAuthMailList(Account account){
        authMailList.add(parseFromAccount(account));
    }
    private void addAuthMailList(AuthMail authMail){
        authMailList.add(authMail);
    }
    public void removeAuthMailList(String email){
        for(AuthMail authMail : authMailList){
            if(authMail.getEmail().equals(email)){
                authMailList.remove(authMail);
                break;
            }
        }
    }

    private AuthMail parseFromAccount(Account account){
        return AuthMail.builder()
                .email(account.getEmail())
                .sendDate(account.getSignUpDate())
                .authKey(account.getAuthKey())
                .build();
    }


    @Override
    public void run() {
        this.isWork = true;
        while(true){
            if(authMailList.size()==0)
                break;
            AuthMail authMail = authMailList.get(0);
            if(isExpire(authMail)){//인증메일 유효시간이 지났다면
                accountRepository.deleteByEmailAndIsAuth(authMail.getEmail(),false);
                authMailList.remove(0);
                continue;
            }
            try {//지나지 않았다면
                Thread.sleep(howLeave(authMail));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.isWork = false;
    }

    private boolean isExpire(AuthMail authMail){
        long now = System.currentTimeMillis();
        long expireTime = this.limitTime +authMail.getSendDateMillis();
        return now>expireTime;
    }
    private long howLeave(AuthMail authMail){
        long now = System.currentTimeMillis();
        long leaveTime = (this.limitTime +authMail.getSendDateMillis()) - now;
        return leaveTime;
    }
}
~~~

    

## Spring Web Chess

- Spring MVC, Spring JDBC
- Cookie 기반 인증 (ft. hashing)
- 이벤트 소싱 기반 데이터 관리

## 개발환경 셋업

1. 도커 데스크탑 실행
2. 해당 프로젝트의 /docker 경로에서 `docker-compose -p 컨테이너명 up -d` 실행
3. 컨테이너가 정상적으로 생성되어 실행되고 있을 때 애플리케이션 실행
4. 애플리케이션 종료 후 /docker 경로에서 `docker-compose -p 컨테이너명 down` 실행

### 디버깅

- `application.properties`의 `spring.datasource.url`에 사용되는 포트정보(XXXX)에 별도의 프로그램이 실행되는 중인지 확인
- 이러한 경우 XXXX의 값을 수정하고, `docker-compose.yml`의 `ports` 정보도 "XXXX:3306" 형식으로 수정 후 컨테이너 재생성

## 기능 요구사항

- 기본 미션 : 체스 게임을 진행할 수 있는 방을 만들어서 동시에 여러 게임이 가능하도록 한다.
- 추가 미션 : 멀티 게임 기능. 두 명의 플레이어가 함께 체스 게임을 참여하도록 한다.
    - 화면이 자동으로 갱신될 필요는 없다. (실시간 X)

### 홈 화면 "/"

- [x] 새로운 게임 버튼과 기존 게임들의 목록을 조회하는 버튼이 제공된다.
    - [x] "새로운 게임 시작" 버튼을 누르면 "/game" 경로로 이동한다.
    - [x] "기존 게임 재개" 버튼을 누르면 "/games" 경로로 이동한다.
- [x] 기본적으로 현재 생성된 게임의 개수와 현재 진행 중인 게임의 개수가 조회된다.

### 게임 생성 화면 "/game"

- [x] 체스방 이름과 비밀번호를 입력하면 새로운 체스 게임이 생성된다.
    - [x] 입력된 비밀번호는 플레이어1(백색 진영)의 비밀번호가 된다.
    - [x] 개별 체스 게임에는 고유 식별값 id가 부여된다.
    - [x] 데이터베이스에는 비밀번호의 해쉬 값이 저장된다.
- [x] 체스방 생성 성공시, 해당 경로로 이동하며 계정에 해당하는 쿠키가 브라우저에 설정된다.
- [x] 생성하려는 방 이름과 중복되는 게임이 이미 존재하는 경우, 예외가 발생하며 알람이 뜬다.
- [x] 생성하려는 방의 비밀번호가 5글자 미만인 경우, 예외가 발생하며 알람이 뜬다.

### 체스방 목록 조회 화면 "/games"

- [x] 체스방 제목들로 구성된 목록이 제시된다.
- [x] 해당 게임에 참여하기 위해 비밀번호를 입력 받는다.
    - [x] 플레이어1 혹은 플레이어2의 비밀번호가 입력된 경우, 브라우저에 인증을 위한 정보가 저장되며, 해당 체스방으로 이동한다.
    - [x] 아직 플레이어2(흑색 진영)가 등록되지 않은 경우, 해당 비밀번호가 플레이어2의 비밀번호로 저장된다.
- [x] id를 검색하여 해당되는 게임으로 이동하는 폼도 제공된다.
    - [x] id에 해당되는 게임이 존재하지 않는 경우 알람이 뜬다.
- [x] 기본적으로 현재 생성된 게임의 개수와 현재 진행 중인 게임의 개수가 조회된다.

### 게임 플레이 화면 "/game/:id"

- [x] 기본적으로 누구나 체스방에 접근할 수는 있다.
    - [x] id에 대응되는 체스게임의 현재 상태가 화면에 제공된다.
    - [x] 비밀번호를 입력하여 다시 로그인할 수 있는 폼이 화면에 별도로 제공된다.
- [x] 체스말을 클릭하고, 특정 체스칸을 클릭하면 해당 위치로 이동시키고자 시도한다.
    - [x] **말을 움직일 때마다 클라이언트에 저장된 각 플레이어의 비밀번호도 함께 서버에 전달**한다.
    - [x] HTTP request header에 유효한 비밀번호 정보가 포함되지 않은 경우, 예외가 발생한다.
    - [x] 현재 움직일 수 없는 진영의 플레이어의 비밀번호가 전달된 경우, 예외가 발생한다.
    - [x] 선택된 체스말을 다시 클릭하면 선택이 해제된다.
    - [x] 해당 체스말이 이동할 수 없는 위치거나, 현재 움직일 수 없는 상태인 경우 예외가 발생한다.
    - [x] 이동에 실패하는 경우 알람에 그 이유가 제시된다.
- [x] 체스방 목록에서 체스방 제목을 클릭하면 체스 게임을 이어서 진행할 수 있다.
- [x] 게임이 종료된 경우 결과 조회 화면으로 이동하는 버튼이 제공된다.

### 결과 조회 화면 "/result/:id"

- [x] id에 대응되는 체스게임의 마지막 보드 상태와 점수 및 승자 정보가 화면에 제공된다.
- [x] 폼을 통해 체스방의 비밀번호를 입력받아 종료된 게임을 삭제할 수 있다.
    - [x] 체스방의 비밀번호는 게임 생성시 설정한 비밀번호로 플레이어1의 비밀번호와 동일하다.
    - [x] 입력된 비밀번호의 해쉬값은 DB에 저장된 해쉬값과 대조되어 삭제 가능 여부가 판단된다.
- [x] 진행중인 게임의 삭제는 불가능하다.
    - [x] 서버에서도 종료된 게임에 대해서만 제거를 시도하며, 실패한 경우 예외를 발생시킨다.

### 프로그래밍 요구사항

- [x] 예외가 발생했을 때 사용자가 이해할 수 있는 명시적인 메시지를 응답한다.
- [x] 예상치 못한 예외가 발생한 경우 생성되는 디폴트 예외 메시지를 전송한다.

# Spring_Board_JWT

<div align="center">

----

### LEVEL 2

</div>

----

## ERD

![image](https://user-images.githubusercontent.com/76714304/218249012-2863d597-00e0-482c-ba5a-78dc13e19cb8.png)

-----

## API 명세서

![image](https://user-images.githubusercontent.com/76714304/218251264-a471729a-f1ed-4872-b827-f40ad32bbd17.png)

-----

### 2023.02.11 요구사항 진행도

#### 1. 회원가입 API
- username, password를 Client에서 전달받기
- username은  `최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)`로 구성되어야 한다.
- password는  `최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)`로 구성되어야 한다.
- DB에 중복된 username이 없다면 회원을 저장하고 Client 로 성공했다는 메시지, 상태코드 반환하기

#### 2. 로그인 API
- username, password를 Client에서 전달받기
- DB에서 username을 사용하여 저장된 회원의 유무를 확인하고 있다면 password 비교하기
- 로그인 성공 시, 로그인에 성공한 유저의 정보와 JWT를 활용하여 토큰을 발급하고, 
발급한 토큰을 Header에 추가하고 성공했다는 메시지, 상태코드 와 함께 Client에 반환하기

#### 3. 전체 게시글 목록 조회 API
- 제목, 작성자명(username), 작성 내용, 작성 날짜를 조회하기
- 작성 날짜 기준 내림차순으로 정렬하기

#### 4. 게시글 작성 API
- 토큰을 검사하여, 유효한 토큰일 경우에만 게시글 작성 가능
- 제목, 작성자명(username), 작성 내용을 저장하고
- 저장된 게시글을 Client 로 반환하기

#### 5. 선택한 게시글 조회 API
-선택한 게시글의 제목, 작성자명(username), 작성 날짜, 작성 내용을 조회하기 

#### 6. 선택한 게시글 수정 API
- 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 수정 가능
- 제목, 작성 내용을 수정하고 수정된 게시글을 Client 로 반환하기

#### 7. 선택한 게시글 삭제 API
- 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 삭제 가능
- 선택한 게시글을 삭제하고 Client 로 성공했다는 메시지, 상태코드 반환하기

-----

<div align="center">

### LEVEL 3

</div>

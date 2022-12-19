# 프로젝트명 + 용도 

스터디용 커머스 백엔드 서버

## 언어

Java 11


## 구성

Spring Boot 2.5.2으로 구성


## Database and cache

PostgresSQL 11

## 환경

Amazon Linux 2 


## 기타 관련 계정 정보

없음

 
## Commerce Service Features

### 회원가입

이메일, 닉네임, 비밀번호, 휴대폰 번호를 입력받는다.

이메일과 닉네임 중복이 되지 않는다.

### 로그인 / 로그아웃

이메일, 비밀번호를 이용하여 로그인한다.

### Product 

Product 목록을 조회한다.(Product 등록은 Admin 영역이라 제외)

Product 상세를 조회한다.

### Order

Product 선택 후 Order를 등록해서 주문서를 출력한다.

유저가 Product 을 구매한다.

구매하기 위한 조건

* 상품 수량이 1 이상인 경우 구매 가능
* 유저가 보유한 금액이 상품 금액 이상인 경우 가능

유저가 구매한 Product 내역 상세 조회

### 


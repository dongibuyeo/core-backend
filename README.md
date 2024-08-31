![image](https://github.com/user-attachments/assets/535b0945-5d9b-4847-a742-86794c89fdee)
# 돈기부여 👋

## 예치금을 걸고 금융 목표에 도전하는 SOL인앱 챌린지 서비스

- 예치금을 걸고 다양한 금융 챌린지에 도전하여 상금을 분배받는다 ~
- 사용자의 이체 내역을 자동 추적하여 별도의 인증이 필요없습니다 !!!!

## 기능 소개

### 👍 커피,술,배달비 줄이기 챌린지!

- 하루동안 사용자 통장의 이체 내역을 자동 추적하여 점수 부여
- It's FEVER TIME ~!! 각 챌린지 별 특정 시간대에 소비를 하지 않을 경우 추가 점수 부여

### 🎯 행운의 777적금 ~!

- 7주동안 7일(매일) 7천원씩 적금하자
- 매일 적금에 성공한 사용자 중 7명을 추첨하여 상품 증정

### 🛣️ 매일매일 금융 퀴즈 챌린지!

- 퀴즈 풀어 공부도 하고 경품 추첨도 받자 ~!
- 매일 매일 랜덤하게 부여되는 금융 관련 퀴즈

### 🗺️ 참여하는 챌린지의 채팅방에 참여하여 소통

- 각 챌린지별 개설된 채팅방에 참여하여 소통해보세요

### 📱 FCM을 응용한 실시간 알림 / Slack Message

- 각 챌린지 종료 시점, FEVER TIME에 사용자에게 FCM 알림 전송
- 경품을 받을 사용자 정보를 Slack Message로 전송하여 투명성 제공


## 시스템 아키텍처
![image](https://github.com/user-attachments/assets/43841537-53ea-4e56-a7c9-87d967188447)

## DB ERD
![image](https://github.com/user-attachments/assets/e47e4d62-4975-4be6-b0af-432b978ef976)

## 사용 기술
|Frontend|Backend|Infra/DevOps|
|:---:|:---:|:---:|
|<img src="https://img.shields.io/badge/react-F05138?style=for-the-badge&logo=React&logoColor=white"><br><img src="https://img.shields.io/badge/typescript-F1007E?style=for-the-badge&logo=typescript"><br><img src="https://img.shields.io/badge/nextjs-F1007E?style=for-the-badge"><br><img src="https://img.shields.io/badge/reactquery-2396F3?style=for-the-badge&logo=reactquery&logoColor=white">|<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white"><br><img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"><br><img src="https://img.shields.io/badge/hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white"> <br> <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"><br><img src="https://img.shields.io/badge/stomp-010101?style=for-the-badge">|<img src="https://img.shields.io/badge/amazonrds-569A31?style=for-the-badge&logo=amazonrds&logoColor=white"><br><img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"><br><img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"><br><img src="https://img.shields.io/badge/slack-2496ED?style=for-the-badge&logo=slack&logoColor=white"><br><img src="https://img.shields.io/badge/firebase-2496ED?style=for-the-badge&logo=firebase&logoColor=white">|

## 구현적 특징

### Backend
1. 사용자의 소비 내역 추적 후 점수 부여를 위해 SSAFY OPEN API 호출 시점에 AOP를 적용하였습니다.
2. 챌린지 종료 후 사용자에게 상금을 분배하기 위해 스케줄링과 배치 작업을 수행하였습니다.
3. 각 챌린지 별 채팅방을 만들기 위해 STOMP와 Pub/Sub 패턴을 활용하였습니다.
4. Slack의 WebHook을 활용하여 당첨자 정보를 Slack Message로 전송하였습니다.


## 멤버 소개
|박수진|이윤하|곽성재|진주원|정연서|
|:----:|:----:|:----:|:----:|:----:|
|Frontend|Frontend|Frontend|Backend|Backend|
|[@s0ojin](https://github.com/s0ojin)|[@yoonha97](https://github.com/yoonha97)|[@kwakseongjae](https://github.com/kwakseongjae)|[@jinjoo-lab](https://github.com/jinjoo-lab)|[@yeondori](https://github.com/yeondori)|
 | <img src = "https://avatars.githubusercontent.com/u/100757599?v=4" width ="120" height = "150"> | <img src = "https://avatars.githubusercontent.com/u/155246172?v=4" width ="120" height = "150">| <img src = "https://avatars.githubusercontent.com/u/87296259?v=4" width ="120" height = "150">| <img src = "https://avatars.githubusercontent.com/u/84346055?v=4" width ="120" height = "150">| <img src = "https://github.com/user-attachments/assets/ef45be79-0342-4d18-bd78-bf86388824d4" width ="120" height = "150">|

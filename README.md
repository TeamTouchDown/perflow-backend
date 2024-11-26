# perflow
![Group 135](https://github.com/user-attachments/assets/651db799-39ba-4012-8cef-cf7b927c7c61)


# :one: 프로젝트 개요


### 2. 기술 스택


협업
Backend
Frontend
Server
CI/CD

<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white"><img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"><img src="https://img.shields.io/badge/Vue-4FC08D?style=for-the-badge&logo=Vue.js&logoColor=white"><img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=MariaDB&logoColor=white"><img src="https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=MongoDB&logoColor=white"><img src="https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=Redis&logoColor=white"><img src="https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=Jenkins&logoColor=white"><img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"><img src="https://img.shields.io/badge/kubernetes-326CE5?style=for-the-badge&logo=kubernetes&logoColor=white"><img src="https://img.shields.io/badge/amazon s3-569A31?style=for-the-badge&logo=amazon s3&logoColor=white"><img src="https://img.shields.io/badge/elastic search-005571?style=for-the-badge&logo=elasticsearch&logoColor=white">

### 3. 프로젝트 아키텍처

# :two: 개발 팀 소개
### 한화시스템 BEYOND SW캠프 10기 터치다운 ###

| [![](https://avatars.githubusercontent.com/u/77000498?v=4)](https://github.com/we4sley) | [![](https://avatars.githubusercontent.com/u/103301589?v=4)](https://github.com/kimjm9911) | [![](https://avatars.githubusercontent.com/u/174981455?v=4)](https://github.com/eunseo-76) | [![](https://avatars.githubusercontent.com/u/50124987?v=4)](https://github.com/JungUiJin) | [![](https://avatars.githubusercontent.com/u/58172997?v=4)](https://github.com/enking) | [![](https://avatars.githubusercontent.com/u/132972216?v=4)](https://github.com/HanDJ00)|
|:---:|:---:|:---:|:---:|:---:|:---:|
| 김영기 | 김지민 | 이은서 | 정의진 | 최두혁 | 한동주


# :three: 협업 전략
### 1. 브랜치 전략
![git-flow](https://github.com/user-attachments/assets/2d7e2ef4-df03-4c5f-8c78-316065100dde)
- **main**: 배포용 branch
- **develop**: 실질적 main branch
- **feature**: 각 기능을 개발하는 branch
- 브랜치명은 '태그/도메인/#이슈번호' 의 형식을 따른다.
  - 예) feat/user/#12
- 기능 단위로 브랜치를 생성하며, 커밋 단위는 가능한 작게 조절한다. 한 번에 여러 클래스를 커밋하지 않고, Pull Request에 너무 많은 커밋이 포함되지 않도록 한다.

### 2. Issue
- 이슈 템플릿을 활용하여 다른 사람이 알기 쉽게 이슈를 작성한다.
- 이슈 작성 후 발급된 #이슈번호를 이용해 브랜치를 생성한다.

![issue_template](https://github.com/user-attachments/assets/eed8bc02-ed2c-49d3-8262-c96478b21c5b)
![issue](https://github.com/user-attachments/assets/c5f1a66d-467d-404e-b26f-b37f3992a7f5)


### 3. Pull Request
- Pull Request의 제목은 '태그/도메인 개발 내용' 의 형식을 따른다.
- 예) feat/user 개발 내용

![pr_1](https://github.com/user-attachments/assets/8f3f4f03-5e50-4d26-8963-18ece4f3ebfa)
![pr_2](https://github.com/user-attachments/assets/fd1b75e4-9a81-49b4-b3d3-d1fd365b8ca4)
![pr_3](https://github.com/user-attachments/assets/fb8650a6-6183-4644-a971-3ca22412544d)

### 4. 커밋 메시지 컨벤션
- 태그<br/>
```feat```: 기능 개발<br/>
```fix```: 버그 수정<br/>
```docs```: 문서 작성 및 수정<br/>
```style```: 코드 리팩토링 등 내용상 변경이 없는 경우<br/>
```test```: 테스트 코드<br/>
```chore```: 자잘한 수정사항<br/><br/>
- 커밋 메시지는 '태그: #이슈번호 내용' 의 형식을 따른다.
- 예) docs: #43 ReadMe 수정

![commit](https://github.com/user-attachments/assets/f4261d3e-7bba-41b8-8b21-5ade721c9da5)

### 5. 네이밍 규칙
- **클래스명**: PascalCase
- **메소드명**, **필드명**: camelCase
```
public class User{

    public void fetchUserInfo(){

        private Long userId;
       private String userName;
    }
}
```
- DTO명: ~DTO 예) ```UserRequestDTO```
- 기타 모호한 단어는 도메인 정의서에 작성한다.

### 6. 기타 규칙
- 가독성을 위해 클래스 첫 줄은 띄어쓰고, 의미 없는 공백은 줄인다.
- 최대 tap depth = 2
- magic number 사용 자제, 상수화
- 커밋 전 정렬(```ctrl + alt + L```)
- 커밋 전 사용하지 않는 import문 삭제(```ctrl + alt + o```)



# :four: 프로젝트 설계 문서
## 1. DDD 설계
[DDD 설계 보기 - Miro](https://miro.com/app/board/uXjVLNZH4nw=/)

## 2. 요구사항 명세서
[요구사항 명세서](https://docs.google.com/spreadsheets/d/1oddc-l1flUAqUNzAMt8jnrshAidp2dHnd1w2Sz5JxWA/edit?gid=1147247037#gid=1147247037)

![요구사항 명세서 이미지](./asset/image/요구사항%20명세서%20캡쳐본.PNG)

## 3. ERD
![ERD 이미지](./asset/image/ERD%20캡쳐본.PNG)

# :five: 기능 수행 테스트
 <details>
   <summary>도서 추천 챗봇</summary>
   <div markdown="1">
  
## 도서 추천 챗봇 응답 받아오기
    
![챗봇 응답](./asset/image/도서%20챗봇%20기능/도서%20추천%20챗봇%20응답%20불러오기%20gif.gif)   

- 채팅창에 사용자가 메시지를 입력해 챗봇에게 보내면 챗봇이 그 채팅에 대해 알맞은 답변을 응답하여 받아온 후 위의 채팅창에 그 내용이 나타난다.
    
## 도서 추천 챗봇 채팅 불러오기
    
![챗봇 채팅 불러오기](./asset/image/도서%20챗봇%20기능/도서%20추천%20챗봇%20채팅%20불러오기%20gif.gif)

- 사용자가 새롭게 페이지에 들어오더라도 이전에 챗봇과 나누던 채팅이 채팅창에 나타난다.
 
</div>
</details>

<details>
   <summary>엘라스틱 서치를 활용한 검색</summary>
   <div markdown="1">
  
## 엘라스틱 서치를 통해 미리 보기가 지원되는 도서 검색 기능
    
![일레스틱 서치를 이용한 검색](https://github.com/user-attachments/assets/49b06ba5-fe2d-4dd4-9ac0-8bb7fa275d73)  

- 사용자가 도서의 제목을 기준으로 검색이 가능하다.
    
</div>
</details>

 <details>
   <summary>채팅 기능을 이용한 독서토론방</summary>
   <div markdown="1">
  
## 채팅방 생성
    
![1_채팅방 생성](https://github.com/user-attachments/assets/b059ce43-7818-4a20-890c-825c10801e58)

- 채팅방 제목과 도서 ID, 최대 인원을 정해 채팅방을 생성한다.

## 채팅
    
![2_채팅](https://github.com/user-attachments/assets/a9912136-f461-44d9-9fbb-9f06da6ef7d2)

- 실시간으로 다수의 유저가 채팅을 진행한다.

## 채팅 내용 및 채팅방 나가기
    
![3_채팅 내용 및 채팅방 나가기](https://github.com/user-attachments/assets/5ee2285a-e50a-4de7-81f1-246e2141c37b)   

- 기존 채팅 내용이 저장되어 있고 채팅방을 나가면 현재 채팅방 인원이 줄어든다.
    
</div>
</details>

 <details>
   <summary>로그인 & 로그아웃</summary>
   <div markdown="1">

## 로그인 & 로그아웃

![로그인 & 로그아웃](https://github.com/user-attachments/assets/f3258c13-3b12-4a98-9c6e-1b2968d2c373)

- 사용자가 로그인 버튼을 누르면 서버로 로그인 url 요청을 보낸다.
- 서버는 카카오 인증 서버에게 로그인 url을 요청해 사용자에게 전달하고, 사용자가 로그인하면 카카오 인증 서버가 인가 코드를 서버에 전달한다.
- 서버는 인가 코드를 이용해 카카오 인증 서버로부터 필요한 정보를 얻고, DB에 사용자를 회원가입 또는 로그인 처리한다. 그리고 서버에서 생성한 서비스 자체 토큰을 함께 보낸다.
- 로그아웃 시 서버에서 토큰을 삭제한다. 

</div>
</details>

 <details>
   <summary>도서 목록</summary>
   <div markdown="1">

## 도서 목록 보기

![도서 목록 보기](https://github.com/user-attachments/assets/a973b471-9d87-49f8-97dd-863e5a8af69c)

- 메인 페이지 화면에서 도서 목록으로 이동해서 등록 된 도서들의 목록을 볼 수 있다.

## 도서 목록 페이지 이동

![도서 목록 페이지 이동](https://github.com/user-attachments/assets/e5865f1d-6448-4c75-863c-b1460bbf3730)

- 도서 목록 내에서 페이징바를 이용하여 페이지 이동을 할 수 있습니다.

</div>
</details>

 <details>
   <summary>도서 상세페이지</summary>
   <div markdown="1">

## 도서 상세페이지

![도서 상세페이지로 이동](https://github.com/user-attachments/assets/b0a8e37f-c79f-40c7-aef7-f002c499d3a3)

- 도서 목록에서 특정 도서 상세페이지로 이동할 수 있습니다.

</div>
</details>

 <details>
   <summary>리뷰</summary>
   <div markdown="1">

## 리뷰 작성

![리뷰 작성](https://github.com/user-attachments/assets/5365f213-1076-4e29-8d6d-90ad580db89c)

- 도서 상세페이지에서 리뷰를 작성할 수 있다.

## 리뷰 작성 취소

![리뷰 작성 취소](https://github.com/user-attachments/assets/cf51f634-4a8b-4476-8793-b1c975e2a406)

- 리뷰를 작성 중에 취소하여 도서 상세페이지로 이동할 수 있다.

## 리뷰 수정

![리뷰 수정](https://github.com/user-attachments/assets/29ee01bc-46ad-46c2-b484-26c6e4bc7feb)

- 리뷰를 수정 할 수 있다.

## 리뷰 정렬

![리뷰 정렬](https://github.com/user-attachments/assets/a3935c4d-cf0c-4415-a4be-4484ee952a02)

- 리뷰를 최신순이나 별점순으로 정렬할 수 있다.

## 리뷰 삭제

![리뷰 삭제](https://github.com/user-attachments/assets/1c597d87-8a17-4d02-98f4-c1a8a4ba252e)

- 리뷰를 삭제할 수 있다.

## 리뷰 신고

![리뷰 신고](https://github.com/user-attachments/assets/b72df4d2-cab5-4e68-990c-555eedbbe2c8)

- 리뷰를 신고 할 수 있고 신고 버튼을 잘못 클릭 했다면 취소할 수도 있다.

</div>
</details>

 <details>
   <summary>예시(여기에 필요한 제목 추가)</summary>
   <div markdown="1">
  
## 작성하고 싶은 기능 제목(예시)
    
![원하는 제목 지정(그냥 이름 지정하는 것이므로 따로 신경 안쓰고 편하게 지으세요)](외부 이미지 링크든 내부에 넣어둔 이미지든 여기에 이미지 링크를 남기시면 됩니다.)   

- 설명하고 싶은 내용 쓰기 여기까지 한세트 필요하면 복붙해서 더 쓰기
    
</div>
</details>

# CI/CD

🔗[k8s Menifests Manage Repository ](https://github.com/MENDITTZO/k8s.git)  

<details><summary>Webhook 설정</summary>
   
![webhook](https://github.com/user-attachments/assets/0f7af8d0-e8d0-4ff2-9a8d-ec59832f2818)

</details>


<details><summary>Jenkins Pipeline</summary>

```
pipeline {
    agent any

    tools {
        gradle 'gradle'   // Jenkins에서 설정된 Gradle 버전
        jdk 'openJDK17'   // Jenkins에서 설정된 JDK 버전
    }

    environment {
        DOCKERHUB_CREDENTIALS = credentials('DOCKERHUB_PASSWORD')
        GITHUB_URL = 'https://github.com/MENDITTZO/MENDITTZO-DEBOOK.git'  // GitHub URL 입력
        FRONTEND_IMAGE = 'debook_vue_project'  // 프론트엔드 Docker 이미지 경로
        BACKEND_IMAGE = 'debook_boot_project'    // 백엔드 Docker 이미지 경로
        MANIFESTS_GITHUB_URL = 'https://github.com/MENDITTZO/k8s.git' // k8s github 경로로
        GIT_USERNAME = 'junguijin'
        GIT_EMAIL = 'yealkki38@gmail.com'
    }

    stages {
        stage('Preparation') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker --version'
                    } else {
                        bat 'docker --version'
                    }
                }
            }
        }
        stage('Source Build') {
            steps {
                git branch: 'main', url: "${env.GITHUB_URL}"
                
                script {
                    // 프론트엔드 빌드
                    dir('MENDITTZO-Frontend') {
                        if (isUnix()) {
                            sh 'npm install'
                            sh 'npm run build'
                        } else {
                            bat 'npm install'
                            bat 'npm run build'
                        }
                    }
                    
                    // 백엔드 빌드
                    dir('MENDITTZO-Backend') {
                        if (isUnix()) {
                            sh "chmod +x ./gradlew"
                            sh "./gradlew clean build -x test"
                        } else {
                            bat "gradlew.bat clean build -x test"
                        }
                    }
                }
            }
        }
        stage('Container Build and Push') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_PASSWORD', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        // 프론트엔드 Docker 이미지 빌드 및 푸시
                        dir('MENDITTZO-Frontend') {
                            if (isUnix()) {
                                sh "docker build --no-cache -t ${DOCKER_USER}/${FRONTEND_IMAGE}:latest ."
                                sh "docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}"
                                sh "docker push ${DOCKER_USER}/${FRONTEND_IMAGE}:latest"
                            } else {
                                bat "docker build --no-cache -t ${DOCKER_USER}/${FRONTEND_IMAGE}:latest ."
                                bat "docker login -u %DOCKER_USER% -p %DOCKER_PASS%"
                                bat "docker push ${DOCKER_USER}/${FRONTEND_IMAGE}:latest"
                            }
                        }

                        // 백엔드 Docker 이미지 빌드 및 푸시
                        dir('MENDITTZO-Backend') {
                            if (isUnix()) {
                                sh "docker build -t ${DOCKER_USER}/${BACKEND_IMAGE}:latest ."
                                sh "docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}"
                                sh "docker push ${DOCKER_USER}/${BACKEND_IMAGE}:latest"
                            } else {
                                bat "docker build -t ${DOCKER_USER}/${BACKEND_IMAGE}:latest ."
                                bat "docker login -u %DOCKER_USER% -p %DOCKER_PASS%"
                                bat "docker push ${DOCKER_USER}/${BACKEND_IMAGE}:latest"
                            }
                        }
                    }
                }
            }
        }
        
    }

    post {
        always {
            script {
                if (isUnix()) {
                    sh 'docker logout'
                } else {
                    bat 'docker logout'
                }
            }
        }
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}

```

</details>

<details><summary>K8s 배포 상태(Argocd)</summary>
   
![argocd](https://github.com/user-attachments/assets/87eafd03-09b4-4795-906c-cd95336dbda4)

</details>

###

# :six: 회고
|   팀원   | 회고 내용 |
|:---:|-----------|
| 김영기 | 회고 |
| 김지민 | 회고 |
| 이은서 | 회고 |
| 정의진 | 회고 |
| 최두혁 | 회고 |
| 한동주 | 회고 |

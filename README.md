# 🖥️ Appcenter-Homepage-Renewal-Server
```
'인천대학교 INU 앱센터 홈페이지 리뉴얼'은 기존에 존재하지 않던 홈페이지의 BackEnd 파트를 새롭게 구축하여
홈페이지 정보를 체계적으로 저장하고 관리할 수 있도록 하는 프로젝트입니다.
```

## 👩‍💻 참여인원
### **개발**
|김원정|홍정우|
|:-:|:-:|
|<a href="https://github.com/NARUBROWN"><img src="https://avatars.githubusercontent.com/u/38902021?v=4" width=120></a>|<a href="https://github.com/Martinelli-3535"><img src="https://avatars.githubusercontent.com/u/79641160?v=4" width=120></a>|
|Leader, Backend|FrontEnd|
|[@NARUBROWN](https://github.com/NARUBROWN)|[@Martinelli-3535](https://github.com/Martinelli-3535)|


### **코드 검수**
|이주원|
|:-:|
|<a href="https://github.com/Juser0"><img src="https://avatars.githubusercontent.com/u/108407945?v=4" width=120></a>|
|[@Juser0](https://github.com/Juser0)|


## 📝 ERD
![서버 ERD](https://github.com/BCD-q/.github/assets/38902021/deb37e96-b722-4cd3-a0bb-a8f29500be65)

## 🔊 서비스 특징
### Redis Cache 구현을 통한 서비스 응답 속도 개선
#### 평균 TPS
|초기값(개선 전)|최종값(개선 후)|결과|
|:-:|:-:|:-:|
|371.1|523.2|✅약41% 개선

![dddddd](https://github.com/inu-appcenter/appcenter-homepage-renewal-server/assets/38902021/74ada4a1-a5f6-42a9-90a7-11e322455c55)

#### 평균 테스트시간
|초기값(개선 전)|최종값(개선 후)|결과|
|:-:|:-:|:-:|
|259.07|184.09|✅약28.9% 개선

![ddddddddd](https://github.com/inu-appcenter/appcenter-homepage-renewal-server/assets/38902021/125c54d5-5af5-4ab5-aa9d-422e99d89058)

#### 테스트 환경
|CPU|RAM|OS|테스트 도구|동작 시간|가상 유저
|:-:|:-:|:-:|:-:|:-:|:-:|
|64-bit ARM Cortex-A72 CPU|2GB|Ubuntu Server 23.10 (64-bit)|Ngrinder|5min|99명|

# appcenter-homepage-renewal-server
인천대학교 앱센터 홈페이지 리뉴얼 프로젝트 서버 저장소입니다



<h2>API 명세</h2>
<h3>role-controller (/roles)</h3>
역할 컨트롤러: 권한을 관리합니다. 파트원, 파트장, 센터장.. 같은 정보를 저장하고 관리합니다. <br>

GET /roles: 역할 (1개) 조회하기

POST /roles: 역할 (1개) 저장하기

DELETE /roles: 역할 (1개) 삭제하기 / 역할이 그룹에 등록되어 있으면 삭제되지 않습니다.

PATCH /roles: 역할 (1개) 수정하기 / 수정된 역할은 그룹 내 구성원에게 영향을 미칩니다.

GET /roles/all-roles: 역할 (전체) 가져오기

<h3> member-controller (/members) </h3>
동아리원 컨트롤러: 동아리원을 관리합니다. 이름, 소개, 프로필 URL.. 같은 정보를 저장하고 관리합니다.<br>

GET /members: 동아리원 (1명) 조회하기

POST /members: 동아리원 (1명) 저장하기

DELETE /members: 동아리원 (1명) 삭제하기 / 동아리원이 그룹에 등록되어 있으면 삭제되지 않습니다.

PATCH /members: 동아리원 (1명) 수정하기 / 수정된 동아리원은 그룹에 영향을 미칩니다.

GET /members/all-members: 동아리원 (전체) 가져오기

<h3> group-controller (/groups) </h3>
그룹 컨트롤러: 동아리 내 그룹(조직)을 관리합니다. 기수, 멤버, 역할... 같은 정보를 저장하고 관리합니다.<br>

GET /groups: 그룹 멤버 (1명) 조회하기

POST /groups: 그룹 멤버 (1명) 저장하기

DELETE /groups: 그룹 멤버 (1명) 삭제하기 / 동아리원이 그룹에 등록되어 있으면 삭제되지 않습니다.

PATCH /groups: 그룹 (1명) 수정하기

GET /groups/all-groups-members: 동아리원 (전체) 가져오기

<h3> introduction-board-controller (/introduction-boards) </h3>
앱 소개 게시판 컨트롤러: 앱센터에서 만들어진 애플리케이션을 소개하는 게시판을 위한 컨트롤러입니다.<br>

GET /introduction-boards: 게시글 (1개) 조회하기

예정 POST /introduction-boards: 게시글 (1개) 등록하기

DELETE /introduction-boards: 게시글 (1개) 삭제하기

PATCH /introduction-boards: 게시글 (1개) 수정하기

GET /introduction-boards/all-introduction-boards : 게시글 (전체) 가져오기

<h3> photo-board-controller (/photo-boards) </h3>
사진 게시판 컨트롤러: 사진 게시판을 위한 컨트롤러입니다.<br>

예정 GET /photo-boards: 게시글 (1개) 조회하기

예정 POST /photo-boards: 게시글 (1개) 등록하기

예정 DELETE /photo-boards: 게시글 (1개) 삭제하기

PATCH /photo-boards: 게시글 (1개) 수정하기

예정 GET /photo-boards/all-photo-boards : 게시글 (전체) 가져오기

<h3> faq-board-controller (/faq-boards) </h3>
FAQ 게시판 컨트롤러: FAQ 게시판을 위한 컨트롤러입니다.<br>

**⚠️예정 GET /faq-boards: 게시글 (1개) 조회하기**

**⚠️예정 POST /faq-boards: 게시글 (1개) 등록하기**

**⚠️예정 DELETE /faq-boards: 게시글 (1개) 삭제하기**

**⚠️예정 PATCH /faq-boards: 게시글 (1개) 수정하기**

**⚠️예정 GET /faq-boards/all-faq-boards : 게시글 (전체) 가져오기**

<h3> image-controller (/image) </h3>
이미지 컨트롤러: 업로드 된 이미지에 접근할 수 있는 URL을 제공하는 컨트롤러 입니다.<br>

예정 GET /photo/{id}: 사진 (1개) 조회하기

<h3> sign-controller (/sign) </h3>
로그인 컨트롤러: 관리자 권한으로 로그인하기 위한 컨트롤러입니다. / 회원가입은 제공되지 않습니다. <br>

**⚠️예정 GET /sign: 로그인**


<h2>⚠️ 참고 아티클 </h2>
[JPA query did not return a unique result 에러 해결방법] [https://velog.io/@mooh2jj/SpringBoot-File-uploaddownload-구현](https://wakestand.tistory.com/943) <br>

# Spring_Board_JWT

## ERD

![image](https://user-images.githubusercontent.com/76714304/218249012-2863d597-00e0-482c-ba5a-78dc13e19cb8.png)

-----

## API 명세서

| 기능 | API URL | Method | Request Header | Request | Response | Response Header |
| --- | --- | --- | --- | --- | --- | --- |
| 회원가입 | /api/auth/signup | POST |  | {
"username" : "name",
"password" : "paswword"
} | {
"msg": "회원가입 완료!",
"statusCode": 200
} |  |
| 로그인 | /api/auth/login | POST |  | {
"username" : "name",
"password" : "paswword"
} | {
"msg": "로그인 완료!",
"statusCode": 200
} |  |
| 게시글 작성 | /api/post | POST | Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYW1lIiwiZXhwIjoxNjc2MTEwMjAxLCJpYXQiOjE2NzYxMDY2MDF9.6x4muFdC3AXlGWDt89e5oaOeWzLQFVNN37Hvhd0HAd4 | {
"title" : "title1",
"content" : "comment1"
} | {
"id": 2,
"title": "title1",
"content": "comment1",
"username": "name",
"createdAt": "2023-02-11T18:12:10.7332627",
"modifiedAt": "2023-02-11T18:12:10.7332627"
} |  |
| 게시글 목록 조회 | /api/posts | GET |  |  | [
{
"id": 3,
"title": "title3",
"content": "comment3",
"username": "name",
"createdAt": "2023-02-11T18:12:39.190104",
"modifiedAt": "2023-02-11T18:12:39.190104"
},
{
"id": 2,
"title": "title1",
"content": "comment1",
"username": "name",
"createdAt": "2023-02-11T18:12:10.733263",
"modifiedAt": "2023-02-11T18:12:10.733263"
}
] |  |
| 게시글 상세 조회 | /api/post/{id} | GET |  |  | {
"id": 2,
"title": "title1",
"content": "comment1",
"username": "name",
"createdAt": "2023-02-11T18:12:10.733263",
"modifiedAt": "2023-02-11T18:12:10.733263"
} |  |
| 게시글 수정 | /api/post/{id} | PUT | Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYW1lIiwiZXhwIjoxNjc2MTEwMjAxLCJpYXQiOjE2NzYxMDY2MDF9.6x4muFdC3AXlGWDt89e5oaOeWzLQFVNN37Hvhd0HAd4 | {
"title" : "update_title3",
"content" : "update_comment3"
} | {
"id": 3,
"title": "update_title3",
"content": "update_comment3",
"username": "name",
"createdAt": "2023-02-11T18:12:39.190104",
"modifiedAt": "2023-02-11T18:12:39.190104"
} |  |
| 삭제 | /api/post/{id} | DELETE | Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYW1lIiwiZXhwIjoxNjc2MTEwMjAxLCJpYXQiOjE2NzYxMDY2MDF9.6x4muFdC3AXlGWDt89e5oaOeWzLQFVNN37Hvhd0HAd4 |  | {
"msg": "게시글 삭제 성공",
"statusCode": 200
} |  |

1. 获取Json Web Token（JWT）
   - 地址：http://127.0.0.1:3000/user/account/token/
   - 方法：POST
   - 是否验证jwt：否
   - 输入参数：
     - username: 用户名
     - password: 密码
   - 返回结果：
     - error_message: 状态信息
     - token: jwt
1. 刷新JWT令牌
   - 地址：http://localhost:3000/api/token/refresh/
   - 方法：POST
   - 是否验证jwt：否
   - 输入参数：
     - refresh: 刷新令牌
   - 返回结果：
     - access: 访问令牌，有效期5分钟
1. 获取用户列表
   - 地址：http://localhost:3000/user/account/userlist/
   - 方法：GET
   - 是否验证jwt：是
   - 输入参数：无
   - 返回结果：返回用户的信息
1. 获取某个用户的信息
   - 地址：http://127.0.0.1:3000/user/account/info/
   - 方法：GET
   - 是否验证jwt：是
   - 返回结果：该登录用户的信息
1. 获取某个用户的信息
   - 地址：http://localhost:3000/user/account/info/get/
   - 方法：GET
   - 是否验证jwt：是
   - 输入参数：
     - user_id：用户的ID
   - 返回结果：该用户的信息
1. 获取某个用户的所有帖子
   - 地址：http://localhost:3000/user/myspace/posts/get/
   - 方法：GET
   - 是否验证jwt：是
   - 输入参数：
     - user_id：用户的ID
   - 返回结果：该用户的所有帖子
1. 创建一个帖子
   - 地址：http://localhost:3000/user/myspace/posts/create/
   - 方法：POST
   - 是否验证jwt：是
   - 输入参数：
     - content：帖子的内容
   - 返回结果：error_message: success
1. 删除一个帖子
   - 地址：http://localhost:3000/user/myspace/posts/del/
   - 方法：DELETE
   - 是否验证jwt：是
   - 输入参数：
     - post_id：被删除帖子的ID
   - 返回结果：error_message: success
1. 更改关注状态
   如果未关注，则关注；如果已关注，则取消关注。
    - 地址：http://127.0.0.1:3000/user/myspace/friends/changefollow/
    - 方法：POST
    - 是否验证jwt：是
    - 输入参数：
      - target_id: 被关注的用户ID
    - 返回结果：error_message: success
1. 查看关注状态
    - 地址：http://127.0.0.1:3000/user/myspace/friends/isfollow/
    - 方法：GET
    - 是否验证jwt：是
    - 输入参数：
      - target_id: 被关注的用户ID
    - 返回结果：
      - error_message: success
      - state: follow/unfollow
1. 注册账号
   - 地址：http://127.0.0.1:3000/user/account/register/
   - 方法：POST
   - 是否验证jwt：否
   - 输入参数：
     - username: 用户名
     - password：密码
     - confirmedPassword：确认密码
   - 返回结果：
     - error_message: success
     - error_message: 用户名为空
     - error_message: 两个密码不一致
     - error_message: 用户名已存在
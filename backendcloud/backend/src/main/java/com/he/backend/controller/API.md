
1.获取Json Web Token（JWT）
   - 地址：http://127.0.0.1:3000/api/user/account/token/
   - 方法：POST
   - 是否验证jwt：否
   - 输入参数：
     - username: 用户名
     - password: 密码
   - 返回结果：
     - error_message: 状态信息
     - token: jwt

1.注册账号
- 地址：http://127.0.0.1:3000/api/user/account/register/
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

1.刷新JWT令牌 (未实现)
   - 地址：http://localhost:3000/api/token/refresh/
   - 方法：POST
   - 是否验证jwt：否
   - 输入参数：
     - refresh: 刷新令牌
   - 返回结果：
     - access: 访问令牌，有效期5分钟

1.获取用户列表
   - 地址：http://localhost:3000/user/account/userlist/
   - 方法：GET
   - 是否验证jwt：是
   - 输入参数：无
   - 返回结果：返回用户的信息

1.获取某个用户的信息
   - 地址：http://127.0.0.1:3000/api/user/account/info/
   - 方法：GET
   - 是否验证jwt：是
   - 返回结果：该登录用户的信息

1.获取某个用户的信息
   - 地址：http://localhost:3000/user/account/info/get/
   - 方法：GET
   - 是否验证jwt：是
   - 输入参数：
     - user_id：用户的ID
   - 返回结果：该用户的信息

1.获取某个用户的所有帖子
   - 地址：http://localhost:3000/user/myspace/posts/get/
   - 方法：GET
   - 是否验证jwt：是
   - 输入参数：
     - user_id：用户的ID
   - 返回结果：该用户的所有帖子

1.创建一个帖子
   - 地址：http://localhost:3000/user/myspace/posts/create/
   - 方法：POST
   - 是否验证jwt：是
   - 输入参数：
     - content：帖子的内容
   - 返回结果：error_message: success

1.删除一个帖子
   - 地址：http://localhost:3000/user/myspace/posts/del/
   - 方法：DELETE
   - 是否验证jwt：是
   - 输入参数：
     - post_id：被删除帖子的ID
   - 返回结果：error_message: success

1.更改关注状态
   如果未关注，则关注；如果已关注，则取消关注。
    - 地址：http://127.0.0.1:3000/api/user/myspace/friends/changefollow/
    - 方法：POST
    - 是否验证jwt：是
    - 输入参数：
      - target_id: 被关注的用户ID
    - 返回结果：error_message: success

1.查看关注状态
    - 地址：http://127.0.0.1:3000/api/user/myspace/friends/isfollow/
    - 方法：GET
    - 是否验证jwt：是
    - 输入参数：
      - target_id: 被关注的用户ID
    - 返回结果：
      - error_message: success
      - state: follow/unfollow

1. 查看粉丝列表
    - 地址：http://127.0.0.1:3000/api/user/myspace/friends/follower/
    - 方法：GET
    - 是否验证jwt：是
    - 输入参数：
        - target_id: 被关注的用户ID
    - 返回结果：
        - error_message: success

1. 查看关注列表
    - 地址：http://127.0.0.1:3000/api/user/myspace/friends/focuser/
    - 方法：GET
    - 是否验证jwt：是
    - 输入参数：
        - follower_id: 关注的用户ID
    - 返回结果：
        - error_message: success

1. 查看未关注列表
    - 地址：http://127.0.0.1:3000/api/user/myspace/friends/unfocus/
    - 方法：GET
    - 是否验证jwt：是
    - 输入参数：
        - follower_id: 关注的用户ID
    - 返回结果：
        - error_message: success

1. 创建bot
    - 地址：http://127.0.0.1:3000/api/user/bot/add/
    - 方法：POST
    - 是否验证jwt：是
    - 输入参数：
        - title: 标题
        - description：描述
        - content：代码
    - 返回结果：
        - error_message: success
        - error_message: 标题不能为空
        - error_message: 标题长度不能大于30
        - error_message: 用户名已存在
        - error_message: success

1. 删除bot
    - 地址：http://127.0.0.1:3000/api/user/bot/del/
    - 方法：DELETE
    - 是否验证jwt：是
    - 输入参数：
        - bot_id: 机器人id
    - 返回结果：
        - error_message: success
        - error_message: bot不存在或已被删除

1. 修改bot
    - 地址：http://127.0.0.1:3000/api/user/bot/update/
    - 方法：POST
    - 是否验证jwt：是
    - 输入参数：
        - bot_id: 机器人id 
        - title: 标题
        - description：描述
        - content：代码
    - 返回结果：
        - error_message: success
        - error_message: 用户名为空
        - error_message: 两个密码不一致
        - error_message: 用户名已存在

1. 查询bot
    - 地址：http://127.0.0.1:3000/api/user/bot/getlist/
    - 方法：GET
    - 是否验证jwt：是
    - 返回结果：
      - 该用户的所有bot
      - error_message: success

1. 查询对局信息
    - 地址：http://127.0.0.1:3000/api/record/getlist/
    - 方法：GET
    - 是否验证jwt：是
    - 输入参数：
      - page: 页号
    - 返回结果 JSONObject：
      - 查询对局记录,按时间倒序输出分页
      - records: JSONObject-> 对局中双方信息以及对局信息
      - records_count: 共有对局记录条数

1. 排行榜
    - 地址：http://127.0.0.1:3000/api/ranklist/getlist/
    - 方法：GET
    - 是否验证jwt：是
    - 输入参数：
        - page: 页号
    - 返回结果 JSONObject：
        - 排行榜,按rating分数倒序输出分页
        - users: JSONObject-> 对局中双方信息以及对局信息
        - users_count: 共有用户数量

1. 创建群
    - 地址：http://127.0.0.1:3000/api/team/add/
    - 方法：POST
    - 是否验证jwt：是
    - 输入参数：
        - name: 群名
    - 返回结果：
        - error_message: 每人最多创建5个群！！！
        - error_message: 群组已存在！
        - error_message: success

1. 解散群
    - 地址：http://127.0.0.1:3000/api/team/del/
    - 方法：POST
    - 是否验证jwt：是
    - 输入参数：
        - id: 群id
    - 返回结果：
        - error_message: 组不存在或没有权限解散组
        - error_message: success

1. 修改群
    - 地址：http://127.0.0.1:3000/api/team/upd/
    - 方法：POST
    - 是否验证jwt：是
    - 输入参数：
        - id: 群id
        - name: 群名
        - own_id: 群主
    - 返回结果：
        - error_message: 组不存在或没有权限修改组
        - error_message: success

1. 获取所有群
    - 地址：http://127.0.0.1:3000/api/team/get/
    - 方法：GET
    - 是否验证jwt：是
    - 输入参数：
        - page: 分页号
    - 返回结果 JSONObject：
        - teams: 当前页的所有组
        - teams_count: 所有组数量

1. 进群/退群(非本人或群主不能进群)
   - 地址：http://127.0.0.1:3000/api/team/join/
   - 方法：POST
   - 是否验证jwt：是
   - 输入参数：
      - team_id: 组id
      - user_id: 用户id
   - 返回结果：
      - error_message: 非法访问！！！
      - error_message: success

1. 获取该群的所有用户
   - 地址：http://127.0.0.1:3000/api/team/getusers/
   - 方法：GET
   - 是否验证jwt：是
   - 输入参数：
      - team_id: 组id
      - page: 分页号
   - 返回结果 JSONObject：
      - users: 组内当前页的所有用户
      - users_count: 组中成员数量






1. 内部接口-> 将玩家添加到匹配池
    - 地址：http://127.0.0.1:3001/player/add/
    - 方法：POST
    - 是否验证jwt：是
    - 输入参数：
        - user_id: 用户id
        - rating: 用户天梯分
        - bot_id: 用户选择的ai, -1表示亲自上阵
    - 返回结果：
        - 匹配池添加一位player且根据rating匹配

1. 内部接口-> 将玩家从匹配池移除
    - 地址：http://127.0.0.1:3001/player/remove/
    - 方法：POST
    - 是否验证jwt：是
    - 输入参数：
        - user_id: 用户id
    - 返回结果：
        - 匹配池移除这位player

1. 内部接口-> 接受匹配信息后开始游戏
    - 地址：http://127.0.0.1:3000/pk/start/game/
    - 方法：POST
    - 是否验证jwt：是
    - 输入参数：
        - a_id: 玩家a
        - a_bot_id: a玩家选择的bot
        - b_id: 玩家b
        - b_bot_id: b玩家选择的bot
    - 返回结果：
        - 将两位玩家开始游戏

1. 内部接口-> 将bot添加到运行池中
    - 地址：http://127.0.0.1:3002/bot/add/
    - 方法：POST
    - 是否验证jwt：是
    - 输入参数：
        - user_id: 用户id
        - bot_code: 玩家bot的代码
        - input: 将当前局面编码为字符串-> 地图 # 自己的sx # sy # 我的操作 # 对手的sx # sy # 对手的操作 #
    - 返回结果：
        - 通过当前局面返回下一步方向

1. 内部接口-> 接收 下一步方向
    - 地址：http://127.0.0.1:3000/pk/receive/bot/move/
    - 方法：POST
    - 是否验证jwt：是
    - 输入参数：
        - user_id: 游戏中的用户
        - direction: 下一步方向
    - 返回结果：
        - 将下一步方向进行判断是否违规或继续执行



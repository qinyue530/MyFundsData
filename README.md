# 基金管理系统（我的基金）

## 项目简介

我的基金是一个基于Spring Boot和Vue 3开发的基金管理系统，提供基金查询、持仓管理、估值计算、定投计划等功能。系统支持多用户，每个用户的数据独立管理。

## 技术栈

### 后端技术
- **Spring Boot**: 2.7.18
- **Java**: 8
- **MyBatis**: ORM框架，使用XML配置
- **MySQL**: 数据库
- **Spring Cloud**: 微服务架构支持
- **Quartz**: 定时任务调度

### 前端技术
- **Vue**: 3.5.12
- **Vue Router**: 4.x
- **Axios**: HTTP客户端
- **Vite**: 构建工具

## 目录结构

```
MyFundsData/
├── MyFundsData-java/       # 后端代码
├── MyFundsData-vue/        # 前端代码
└── MyFundsData-sql/        # 数据库初始化脚本
```

### 后端目录结构

```
MyFundsData-java/
├── src/main/java/com/example/myfunds/
│   ├── controller/         # 控制器
│   ├── entity/             # 实体类
│   ├── mapper/             # Mapper接口
│   ├── scheduler/          # 定时任务
│   ├── service/            # 业务逻辑
│   └── MyFundsApplication.java # 启动类
├── src/main/resources/
│   ├── mapper/             # MyBatis XML配置
│   └── application.yml     # 配置文件
└── pom.xml                 # Maven配置
```

### 前端目录结构

```
MyFundsData-vue/
├── src/
│   ├── components/         # Vue组件
│   ├── router/             # 路由配置
│   ├── services/           # API服务
│   ├── App.vue             # 根组件
│   ├── main.js             # 入口文件
│   └── style.css           # 全局样式
├── index.html              # HTML模板
└── package.json            # 依赖配置
```

## 功能模块

### 1. 用户管理
- 用户注册
- 用户登录
- 用户信息管理

### 2. 基金查询
- 基金基本信息查询
- 基金持仓股票查询
- 基金数据刷新
- 基金估值计算

### 3. 我的持仓
- 手动添加持仓
- 基金加仓
- 基金减仓
- 持仓盈亏计算
- 预估收益显示

### 4. 定投计划
- 设置定投计划
- 暂停/恢复定投
- 停止定投
- 定投计划执行

### 5. 数据管理
- 基金数据定时更新
- 历史数据存储
- 交易记录管理

## 快速开始

### 环境要求

- **Java**: 8
- **Maven**: 3.6+
- **Node.js**: 14+
- **MySQL**: 5.7+

### 1. 数据库初始化

1. 创建数据库：
   ```sql
   CREATE DATABASE myfunds CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. 执行初始化脚本：
   ```bash
   mysql -u root -p myfunds < MyFundsData-sql/init.sql
   ```

### 2. 后端服务配置与启动

#### 2.1 配置修改

1. 进入后端目录：
   ```bash
   cd MyFundsData-java
   ```

2. 修改数据库连接配置（根据实际情况调整）：
   编辑 `src/main/resources/application.yml` 文件，修改以下配置：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/myfunds?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
       username: root    # 你的数据库用户名
       password: 123456  # 你的数据库密码
   ```

#### 2.2 启动服务

1. 编译并启动后端服务：
   ```bash
   mvn spring-boot:run
   ```

2. 服务将在 `http://localhost:8080/myfunds` 启动

3. 验证服务是否启动成功：
   ```bash
   curl http://localhost:8080/myfunds/api/user/test
   ```
   如果返回 `"测试成功"`，说明后端服务正常启动

### 3. 前端服务配置与启动

#### 3.1 安装依赖

1. 进入前端目录：
   ```bash
   cd MyFundsData-vue
   ```

2. 安装依赖：
   ```bash
   npm install
   ```

#### 3.2 启动开发服务器

1. 启动前端开发服务器：
   ```bash
   npm run dev
   ```

2. 访问 `http://localhost:5173`

### 4. 测试流程

1. **注册新用户**：
   - 打开 `http://localhost:5173`
   - 点击"注册"按钮，填写用户名和密码
   - 点击"注册"完成用户注册

2. **用户登录**：
   - 填写刚才注册的用户名和密码
   - 点击"登录"按钮

3. **功能测试**：
   - 登录成功后，进入系统主界面
   - 点击"基金查询"标签，输入基金代码（如：000001），点击"查询"
   - 可以添加持仓、设置定投计划等

### 5. 常见问题解决

#### 5.1 端口被占用

- 如果端口 8080 被占用，可以修改后端服务端口：
  编辑 `application.yml` 文件，修改 `server.port` 配置

- 如果端口 5173 被占用，可以修改前端服务端口：
  ```bash
  npm run dev -- --port 5174
  ```

#### 5.2 数据库连接失败

- 检查数据库服务是否正常启动
- 检查 `application.yml` 中的数据库连接配置是否正确
- 检查数据库用户是否有足够的权限

#### 5.3 前端无法访问后端接口

- 检查后端服务是否正常启动
- 检查浏览器控制台是否有跨域错误
- 确认后端服务的 context-path 配置为 `/myfunds`

### 6. 开发环境测试

#### 6.1 API 测试工具

可以使用 Postman 或 curl 工具测试 API 接口，例如：

1. 测试用户注册：
   ```bash
   curl -X POST -H "Content-Type: application/json" -d '{"username":"test","password":"123456"}' http://localhost:8080/myfunds/api/user/register
   ```

2. 测试用户登录：
   ```bash
   curl -X POST -H "Content-Type: application/json" -d '{"username":"test","password":"123456"}' http://localhost:8080/myfunds/api/user/login
   ```

#### 6.2 查看日志

- 后端日志位置：`MyFundsData-java/logs/myfunds.log`
- 前端日志：浏览器控制台

## 测试账号

为了方便测试，提供以下测试账号：

- 用户名：test1，密码：123456
- 用户名：test2，密码：123456

这些账号已经预加载了一些测试数据，可以直接登录使用。

## 核心功能说明

### 基金估值计算

系统会根据基金的持仓股票和各股票的涨跌幅，计算出基金的当日预估涨跌幅和预估收益：

1. 基于各股票的持仓比例和涨跌幅计算加权平均预估涨跌幅
2. 根据最新净值计算预估净值
3. 根据预估净值变化计算预估收益

### 涨跌颜色显示

系统采用"涨红跌绿"的颜色方案：
- 上涨数据显示为红色 (#F56C6C)
- 下跌数据显示为绿色 (#67C23A)

### 数据与用户关联

所有数据都与当前登录用户关联，确保数据隔离：
- 用户持仓数据
- 交易记录
- 定投计划

## 主要API接口

### 用户相关

- `POST /api/user/register` - 用户注册
- `POST /api/user/login` - 用户登录
- `GET /api/user/{id}` - 获取用户信息
- `PUT /api/user/{id}` - 更新用户信息

### 基金相关

- `GET /api/fund/info/{fundCode}` - 获取基金基本信息
- `GET /api/fund/holdings/{fundCode}` - 获取基金持仓股票
- `POST /api/fund/refresh/{fundCode}` - 刷新基金数据
- `POST /api/fund/update-all` - 更新所有基金数据

### 交易相关

- `POST /api/trade/add-holding` - 添加基金持仓
- `POST /api/trade/buy` - 加仓
- `POST /api/trade/sell` - 减仓
- `POST /api/trade/fixed-investment/set` - 设置定投计划
- `GET /api/trade/user-funds/{userId}` - 获取用户持仓
- `GET /api/trade/transactions/{userId}` - 获取交易记录

## 开发说明

### 代码规范

- 后端代码：采用Google Java Style Guide
- 前端代码：采用Vue官方推荐的代码风格
- 数据库：所有字段都有中文注释

### 配置文件

- 后端配置：`MyFundsData-java/src/main/resources/application.yml`
- 前端API配置：`MyFundsData-vue/src/services/api.js`

### 定时任务

- 基金数据更新：每天定时更新基金数据
- 定投计划执行：根据定投计划的频率执行定投

## 部署说明

### 生产环境部署

1. 后端打包：
   ```bash
   cd MyFundsData-java
   mvn clean package -DskipTests
   ```

2. 前端构建：
   ```bash
   cd MyFundsData-vue
   npm run build
   ```

3. 将构建产物部署到服务器

## 注意事项

1. 系统使用简单的密码存储方式，生产环境建议使用BCrypt等安全加密方式
2. 基金数据来源为模拟数据，实际使用时需要接入真实的基金数据API
3. 定时任务的执行频率可根据实际需求调整

## 后续计划

1. 添加基金历史数据图表展示
2. 支持基金对比功能
3. 增加更多的基金分析指标
4. 支持导出持仓报告
5. 开发移动端应用

## 贡献

欢迎提交Issue和Pull Request，共同完善系统功能。

## 许可证

MIT License

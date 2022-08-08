# spring-base
[![OSCS Status](https://www.oscs1024.com/platform/badge/CodeLFC/spring-base.svg?size=small)](https://www.oscs1024.com/project/CodeLFC/spring-base?ref=badge_small)
<br>对springBoot项目中的统一异常捕获，统一结果格式，header校验，系统日志，提供一个基础实现。这个基础模块在构建SpringBoot项目时给我带来了很大的方便，添加依赖就可以轻松构建标准的SpringBoot项目
# 使用方法
1. 下载源代码编译,安装到本地仓库，可修改springBoot(2.3.10.RELEASE)以及springCloud(Hoxton.SR11)版本。

2. 直接使用远程maven仓库(如果没有修改版本的需求，推荐使用远程仓库的模式)
    1. 添加属性
      ```
      <!--设置父项目为本项目-->
      <parent>
          <groupId>gaozhi.online</groupId>
          <artifactId>spring-base</artifactId>
          <version>1.0</version>
       </parent>
      <!--添加基础依赖-->
      <dependency>
          <groupId>gaozhi.online</groupId>
           <artifactId>base</artifactId>
           <version>1.0</version>
      </dependency>
      
      <repositories>
          <!--添加git远程仓库-->
          <repository>
              <id>spring-base</id>
              <url>https://github.com/CodeLFC/maven-repository/spring-base</url>
              <snapshots>
               <enabled>true</enabled>
               <updatePolicy>always</updatePolicy>
              </snapshots>       
          </repository>
      </repositories>
      ``` 
# 简单应用到项目
1. 统一结果格式：在Application中添加包扫描配置即可自动将Controller层返回结果封装为JSON
     ``` 
    @SpringBootApplication
    //添加包（gaozhi.online.base.ScanClass.class）的扫描
    @ComponentScan(basePackageClasses = {gaozhi.online.base.ScanClass.class,Application.class})
    public class Application {
        public static void main(String[] args) {
            SpringApplication.run(Application.class);
        }
    }
    ``` 
    统一返回结果格式：
    ```
    //格式
    {
        "code": 200(返回码),
        "message": "请求成功"（提示信息）,
        "data": "{\"id\":2147483648}" （返回对象的json串）
    }
    //实例
    {
      "code": 200,
      "message": "请求成功",
      "data": "{\"id\":2147483648,\"headUrl\":\"https://gimg2.baidu.com/image_search/src\\u003dhttp%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202107%2F17%2F20210717232533_2edcf.thumb.1000_0.jpg\\u0026refer\\u003dhttp%3A%2F%2Fc-ssl.duitang.com\\u0026app\\u003d2002\\u0026size\\u003df9999,10000\\u0026q\\u003da80\\u0026n\\u003d0\\u0026g\\u003d0n\\u0026fmt\\u003dauto?sec\\u003d1651719991\\u0026t\\u003d7765c89c73904f25968e23b718075a8b\",\"nick\":\"张三2\",\"remark\":\"快乐p2p\",\"gender\":\"male\",\"birth\":0,\"email\":\"lfc_guest@163.com\",\"createTime\":1634974238958,\"updateTime\":1649048839660,\"banTime\":0,\"status\":1,\"vip\":0}"
    }
    ```
   
2. 统一异常处理：在接口逻辑任意地方抛出的运行时异常都将被捕获并封装为JSON格式，推荐实现BusinessRuntimeException接口编写自定义异常并枚举异常类型。
   
   - 自定义异常：
   ```
   /**
   * 自定义异常
   */
   public class UserException extends BusinessRuntimeException {
   
       public UserException(Result.ResultEnum exception) {
           super(exception);
       }
   
       public UserException(Result.ResultEnum exception, String msg) {
           super(exception, msg);
       }
   }
   /**
   * 枚举异常值
   */
   public enum UserExceptionEnum implements Result.ResultEnum {
       USER_AUTH_ERROR(2000,"API请求权限校验失败:请重新登陆，您的账号有被盗用的风险，请及时修改密码"),
       USER_PASS_ERROR(2001,"密码错误"),
       USER_ACCOUNT_FORBIDDEN(2002,"账号已被禁用"),
       USER_ACCOUNT_CANCEL(2003,"账号已注销"),
       USER_NOT_EXIST(2004,"用户不存在");
       private final int  code;
       private final String message;
   
       UserExceptionEnum(int code, String message) {
           this.code = code;
           this.message = message;
       }
   
       @Override
       public int code() {
           return code;
       }
   
       @Override
       public String message() {
           return message;
       }
   }
   ```
   - 抛出异常：
   ```
      userInfo = userService.findUserInfo(userId);
      if (userInfo == null) {
         //推荐实现
         throw new UserException(UserExceptionEnum.USER_NOT_EXIST,"其余附加信息");
      }
   ```
3. 统一header校验：被@HeaderChecker注解的方法将会被拦截经过自定义的参数校验逻辑，并自动在请求属性中注入客户端远程ip和接口路径信息
   - 自定义校验逻辑（示例）
    ``` 
   import com.google.gson.Gson;
   import gaozhi.online.base.exception.BusinessRuntimeException;
   import gaozhi.online.base.interceptor.HeaderChecker;
   import gaozhi.online.base.interceptor.HeaderPropertyChecker;
   import gaozhi.online.peoplety.entity.Token;
   import gaozhi.online.peoplety.entity.UserInfo;
   import gaozhi.online.peoplety.exception.UserException;
   import gaozhi.online.peoplety.exception.enums.UserExceptionEnum;
   import gaozhi.online.peoplety.user.service.UserService;
   import gaozhi.online.peoplety.util.DateTimeUtil;
   import org.apache.commons.logging.Log;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.core.log.LogDelegateFactory;
   import org.springframework.stereotype.Component;
   import javax.servlet.http.HttpServletRequest;
   import javax.servlet.http.HttpServletResponse;
   
   
   /**
    * 检查token
    */
   @Component(HeaderChecker.accessToken)
   public class TokenChecker implements HeaderPropertyChecker<Token> {
       public static final String HEADER_ATTRIBUTE_USER = "user";
       private final Log log = LogDelegateFactory.getHiddenLog(TokenChecker.class);
       private final Gson gson = new Gson();
       @Autowired
       private UserService userService;
   
       @Override
       public Token check(String value, String url, String ip,HttpServletRequest request,HttpServletResponse response) {
           //log.info("url=" + url + " 检查用户token:" + value);
           Token token = gson.fromJson(value, Token.class);
           if (token == null) {
               throw new BusinessRuntimeException(UserExceptionEnum.USER_AUTH_ERROR);
           }
           UserInfo userInfo = userService.findUserInfo(token.getUserid());
           if (userInfo == null) {
               throw new UserException(UserExceptionEnum.USER_NOT_EXIST);
           }
           //校验token
           if (!userService.checkToken(token)) {
               //失败返回上次登陆ip
               throw new UserException(UserExceptionEnum.USER_AUTH_ERROR, userInfo.getIp());
           }
   
           //更新ip地址
           userInfo.setIp(ip);
           userService.updateIp(token.getUserid(), ip);
           //检查账号是否被封禁
           if (userInfo.getBanTime() > System.currentTimeMillis()) {
               throw new UserException(UserExceptionEnum.USER_ACCOUNT_FORBIDDEN, "如有疑问请进行申诉，解封时间为" + DateTimeUtil.getDefaultFormatTime(userInfo.getBanTime()));
           }
           //校验url权限
           if (!userService.checkURLPrivilege(userInfo.getStatus(), url)) {
               throw new UserException(UserExceptionEnum.USER_AUTH_ERROR, "没有权限访问 url=" + url);
           }
   
           request.setAttribute(HEADER_ATTRIBUTE_USER, userInfo);
           return token;
       }
   
   }

    ```
   - 获取自动注入的属性（可通过参数注解方式）
    ```
      //客户端调用的接口url，如果经过微服务转发值为客户端调用的接口，可以用于鉴权
      request.getAttribute(HeaderChecker.rpcURLKey);
      //客户端IP
      request.getAttribute(HeaderChecker.rpcClientIp);
   
      //参数注解方式获取
      @RequestAttribute(HeaderChecker.rpcClientIp) String clientIp
    ```
4. AOP日志
  - 实现日志切面，覆盖切入点表达式，注入日志服务
    ```
    @Component
    @Aspect
    @Slf4j
    public class ControllerLogAop extends LogAop {
        {
            log.info("ControllerLogAop注入");
        }
        
        @Override
        @Pointcut("execution(* gaozhi.online.peoplety.user.controller.*.*(..))")
        public void pointcut() {
    
        }
    
        @Autowired
        @Override
        public void setSysLogService(ILogService sysLogService) {
            super.setSysLogService(sysLogService);
        }
    }

    ```
  - 实现日志服务
    ```
    @Service
    @Slf4j
    public class ControllerLogServiceImpl implements ILogService {
      
        @Override
        public void handle(SysLog log) {
            //自定义的日志操作
            log.setType(systemPropertyConfig.getApplicationName());
            sysLogFeignClient.writeLog(log);
        }
    }
    ```
    
    
# 博客系统安全审查报告

> 审查日期：2026-03-03
> 审查人：QA测试工程师

## 发现的安全问题

### 1. [中等风险] Markdown渲染器允许原始HTML

**位置**: `blog-web/src/utils/markdown.ts:7`

**问题描述**:
```typescript
const md: MarkdownIt = new MarkdownIt({
  html: true,  // 允许渲染原始HTML
  // ...
})
```

Markdown渲染器配置了 `html: true`，允许用户在Markdown内容中嵌入原始HTML，包括 `<script>` 标签。这可能导致存储型XSS攻击。

**攻击场景**:
1. 攻击者在文章内容中插入 `<script>alert('XSS')</script>`
2. 管理员或用户查看文章时，恶意脚本被执行

**修复建议**:
- 将 `html: true` 改为 `html: false`
- 或者使用 DOMPurify 对渲染结果进行净化：
  ```typescript
  import DOMPurify from 'dompurify'
  export const renderMarkdown = (content: string): string => {
    return DOMPurify.sanitize(md.render(content))
  }
  ```

**影响范围**: 文章详情页、评论区域

---

### 2. [低风险] CORS配置过于宽松

**位置**: `blog-backend/blog-infrastructure/src/main/java/com/blog/infrastructure/config/SecurityConfig.java:65`

**问题描述**:
```java
configuration.setAllowedOrigins(List.of("*"));
```

CORS配置允许所有来源访问API，在生产环境中可能存在安全风险。

**修复建议**:
- 开发环境可保持当前配置
- 生产环境应限制为具体的域名：
  ```java
  configuration.setAllowedOrigins(List.of("https://yourdomain.com", "https://www.yourdomain.com"));
  ```

---

### 3. [低风险] 滑动验证码后端校验较弱

**位置**: `blog-backend/blog-application/src/main/java/com/blog/application/service/UserService.java:159-176`

**问题描述**:
验证码校验仅验证时间戳有效性（5分钟内），未验证验证码是否真正由前端完成。

**当前校验逻辑**:
```java
private boolean validateCaptchaToken(String token) {
    // 仅解码Base64并检查时间戳
    String decoded = new String(java.util.Base64.getDecoder().decode(token));
    String[] parts = decoded.split("\\|");
    long timestamp = Long.parseLong(parts[0]);
    return System.currentTimeMillis() - timestamp < 5 * 60 * 1000;
}
```

**潜在风险**:
攻击者可以构造任意符合格式的token绕过验证。

**修复建议**:
- 使用服务器端Session或Redis存储验证状态
- 前端完成验证后，后端生成唯一token并存储
- 登录时验证token是否存在于服务端存储中

---

### 4. [信息] JWT Token有效期确认

**位置**: `blog-backend/blog-infrastructure/src/main/java/com/blog/infrastructure/security/JwtUtils.java`

**状态**: 符合设计要求
- Access Token有效期：2小时（7200秒）
- 使用BCrypt进行密码加密

---

## 安全测试待执行项

以下测试需要在应用部署完成后执行：

1. **SQL注入测试**
   - 登录接口用户名注入测试
   - 搜索接口关键词注入测试

2. **XSS攻击测试**
   - 评论内容XSS测试
   - 文章内容XSS测试（待修复上述问题后验证）

3. **权限边界测试**
   - 普通用户访问管理员API
   - 未登录用户访问需认证API

4. **会话管理测试**
   - Token过期后访问
   - Token篡改测试

---

## 建议优先级

| 问题 | 优先级 | 建议处理时间 |
|------|--------|--------------|
| Markdown XSS风险 | 高 | 部署前修复 |
| CORS配置 | 中 | 上线前修复 |
| 验证码校验 | 低 | 可作为后续优化 |

---

## 附录：安全最佳实践检查清单

- [x] 密码使用BCrypt加密存储
- [x] JWT Token有效期设置合理（2小时）
- [x] API权限分层（公开/用户/管理员）
- [ ] XSS防护完善（待修复Markdown配置）
- [ ] CORS限制（生产环境需配置）
- [x] CSRF防护（REST API无状态，无需CSRF）
- [ ] 输入验证（后端需加强参数校验）
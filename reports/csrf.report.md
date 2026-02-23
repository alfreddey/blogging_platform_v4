## Introduction
This report explains why CSRF protection is enabled for a simple form submission and shows how the CSRF token works on the `/form` endpoint. The goal is straightforward: stop malicious sites from secretly submitting requests on behalf of a user. Even when an endpoint is publicly accessible, it can still be abused if the browser automatically sends cookies. That’s why the configuration intentionally enables CSRF protection in the security setup.

## Why CSRF Protection Is Enabled
The main reason for enabling CSRF is to protect the endpoint from cross-site request forgery attacks. Without protection, a logged-in user could visit a malicious page that silently sends a POST request to `/form` using the user’s session. The server would think the request is legitimate because the session cookie is valid. In the security configuration, CSRF is enabled using  
`csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))`.  
This tells Spring Security to generate a CSRF token and store it in a cookie that the browser can read. The server then expects that token to be sent back with every state-changing request. If it’s missing or incorrect, the request is rejected. So even though `/form` allows POST requests, only requests with a valid token succeed.

## Form Endpoint Workflow
The form workflow is simple and clean. In the controller, the GET handler for `/form` prepares a model attribute using  
`model.addAttribute("form", new SimpleForm())`  
and returns the view. When the page is rendered, Spring Security automatically exposes the CSRF token. In a typical form template, the token is included as a hidden field. When the user submits the form, the browser sends both the form data and the CSRF token. The POST handler then processes the submission with  
`@ModelAttribute SimpleForm form`,  
adds a flash message using `RedirectAttributes`, and redirects back to `/form`. From the user’s perspective, nothing feels different, but behind the scenes, every submission is verified.

## CSRF Token Mechanism Demo for `/form`
The token mechanism itself is easy to follow. When a user visits `/form`, the server generates a CSRF token and sends it in a cookie (for example, `XSRF-TOKEN`). Because the cookie is not HTTP-only, client code or the form can read it. When the form is submitted, the token is included in the request (commonly as `_csrf` or `X-XSRF-TOKEN`). Spring Security compares the submitted token with the stored one. If they match, the request is accepted and the controller logic runs. If they don’t match, the request is blocked before it reaches `submitForm`. This is why the configuration also allows headers like `"X-CSRF-TOKEN"` and `"X-XSRF-TOKEN"` in CORS settings.

## Conclusion
Enabling CSRF protection for the `/form` endpoint is a practical security measure that prevents unauthorized form submissions while keeping the user experience smooth. The setup integrates naturally with the controller flow and requires minimal extra code. By issuing a token on page load and validating it on submission, the application ensures that only genuine user actions are processed. It’s a small configuration step that makes a big difference in protecting the application.
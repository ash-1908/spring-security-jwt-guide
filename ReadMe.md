# STEPS

## GETTING STARTED
1. ADD SPRING SECURITY STARTER DEPENDENCY
2. CREATE A REST CONTROLLER AND ADD A GET METHOD ROUTE
3. MAKING A GET REQUEST TO THAT ENDPOINT GIVES 401 UNAUTHORIZED
4. SPRING SECURITY BY DEFAULT CREATES A USER 'USER' AND CREATES A PASSWORD WHICH IS DISPLAYED IN THE LOGS.
5. WE CAN USE THEM TO LOG IN AND ACCESS DATA FROM /data ROUTE.

## EDITING SPRING SECURITY AUTHENTICATION AND AUTHORIZATION
1. SPRING SECURITY BY DEFAULT USES LOGIN FORM TYPE OF AUTHENTICATION AND CREATES A DEFAULT USER WITH DEFAULT PASSWORD.
2. WE CAN OVERRIDE THIS AND ADD OUR OWN TYPE OF AUTHENTICATION AND ADD OUR OWN TYPE OF USER.

### ADDING OUR OWN USERS FOR AUTHENTICATION
1. WE CREATE A CONFIGURATION CLASS WITH ANNOTATIONS 'CONFIGURATION' AND 'ENABLE WEB SECURITY'.
2. WE CREATE A METHOD WHICH RETURNS 'InMemoryUserDetailsManager' AND MARK THIS METHOD AS 'BEAN'.
3. THIS METHOD WILL RETURN A USER DETAILS MANAGER WHICH WILL CREATE USERS THAT WE ARE GOING TO CREATE.
4. A USER CONTAINS USERNAME, PASSWORD AND A COLLECTION OF 'GRANTED AUTHORITY'. 
5. GRANTED AUTHORITY IS BASICALLY AN AUTHORITY THAT WE ARE GOING TO GRANT TO THE USER.
6. AFTER CREATING USERS WE RETURN A NEW INSTANCE OF 'InMemoryUserDetailsManager' AND PASS THE LIST OF USERS TO IT.
7. NOW WE HAVE TO CREATE A METHOD THAT RETURNS A PASSWORD ENCODER AND MARK IT AS BEAN. IN OUR CASE WE USE 'NoOpPasswordEncoder'.

### ADDING OUR OWN AUTHORIZATION
1. TO SEE AUTHORIZATION IN ACTION WE WILL RENAME OUR EXISTING ROUTE TO USER AND CREATE TWO MORE ROUTES WITH THE NAMES ADMIN AND MANAGER AND RETURN RESPONSE ACCORDINGLY.
2. NOW IN OUR SECURITY CONFIGURATION CLASS WE CREATE A METHOD THAT RETURNS 'SecurityFilterChain' AND TAKES IN 'HttpSecurity' AS AN ARGUMENT, AND THROWS AN EXCEPTION, THAT COMES FROM 'HttpSecurity'.
3. IN THIS METHOD WE DEFINE WHICH AUTHORITY IS ALLOWED TO ACCESS WHICH ROUTE.
4. WE USE 'HttpSecurity' OBJECT, SUPPOSE 'http', TO FIRST DISABLE CROSS-SITE-REQUEST-FORGERY, TO BE ABLE TO MAKE REQUESTS THEN WE AUTHORIZE REQUEST USING ANTMATCHERS AND JOIN A ROLE TO THEM.
5. NOW WE SEE THAT USER WITH 'USER' ROLE IS NOT ABLE TO ACCESS ROUTES FOR MANAGER AND ADMIN, USER WITH 'MANAGER' ROLE IS NOT ABLE TO ACCESS ROUTE FOR ADMIN AND USER WITH ROLE 'ADMIN' IS ABLE TO ACCESS ALL THE ROUTES.

## ADDING OUR OWN USER REPOSITORY
1. SPRING SECURITY MAINTAINS ITS OWN 'User' CLASS, 'UserDetails' INTERFACE, AND 'UserDetailsService' INTERFACE.
2. WE CAN IMPLEMENT 'UserDetails' AND 'UserDetailsService' INTERFACES AND CONNECT OUR DATABASE TO THEM.
3. TO CONNECT OUR DATABASE, MAKE A REPOSITORY AND IMPLEMENT 'UserDetailsService', THEN OVERRIDE THE METHOD 'loadUserByUsername'.
4. THEN IN OUR SECURITY CONFIG CLASS, CHANGE THE NAME 'InMemoryUserDetailsManager' TO 'configAuthentication' TO MAKE IT MORE APPROPRIATE, AND ADD 'HttpSecurity' OBJECT TO ITS PARAMETER.
5. NOW IN SECURITY CONFIG CLASS, CREATE AN AUTOWIRED OBJECT FOR YOUR IMPLEMENTATION OF 'UserDetailsService'.
6. IN OUR 'configAuthentication' METHOD, USE 'HttpSecurity' OBJECT TO GET THE SHARED OBJECT OF AUTHENTICATION MANAGER BUILDER AND ADD THE AUTOWIRED OBJECT OF YOUR USER DETAILS SERVICE, THEN ADD THE BUILD METHOD.

## USING JWT TOKEN
1. FIRST STEP IS TO ADD DEPENDENCIES. FOR JWT TO WORK, ADD 'jjwt-api', 'jjwt-impl', and 'jjwt-gson' OF THE SAME VERSION FROM 'io.jsonwebtoken'. ALSO ADD 'jaxb-api' FROM 'javax.xml.bind'.

### CREATING A JWT TOKEN WHILE AUTHENTICATION
1. WE HAVE TO CREATE A UTILITY CLASS FOR JWT RELATED METHODS. CREATE CLASS 'MyJwtUtil' WHICH IMPLEMENTS 'Serializer'.
2. ADD UTILITY METHODS TO IT.
3. NOW WE CREATE A CUSTOM AUTHENTICATION MANAGER WHICH WILL CHECK WHETHER THE CREDENTIALS ARE CORRECT OR NOT. NOW THAT YOU HAVE A CUSTOM AUTHENTICATION MANAGER, REMOVE THE METHODS 'configAuthentication' AND 'passwordEncoder' FROM IT. 
4. CREATE A CLASS 'MyAuthenticationManager' WHICH IMPLEMENTS 'AuthenticationManager', AND OVERRIDE THE METHOD 'authenticate'.
5. CREATE AN AUTOWIRED OBJECT OF 'MyUserService' CLASS.
6. CREATE A METHOD FOR PASSWORD ENCODER AND MARK IT AS BEAN.
7. IN THE 'authenticate' METHOD, CHECK WHETHER THE CREDENTIALS ARE CORRECT OR NOT AND RETURN 'UsernamePasswordAuthenticationToken'.
8. CREATE TWO MODEL CLASSES FOR REQUEST AND RESPONSE. HERE REQUEST CLASS WILL HAVE USERNAME AND PASSWORD AS PARAMETERS AND RESPONSE CLASS WILL HAVE JWTTOKEN AS PARAMETER.
9. NOW IN THE CONTROLLER CLASS, CREATE A ROUTE FOR SIGN IN. TAKE IN CREDENTIALS AS REQUEST CLASS, AND SEND THEM TO YOUR CUSTOM AUTHENTICATION MANAGER FOR VERIFICATION.
10. THEN LOAD THE USER DETAILS FROM YOUR USER DETAILS SERVICE, AND NOW CREATE A TOKEN FROM YOUR JWT UTIL CLASS.
11. NOW RETURN THIS JWT TOKEN AS RESPONSE CLASS OBJECT.

### CHECKING THE VALIDITY OF JWT TOKEN
1. FOR THIS, WE HAVE TO CREATE A FILTER. CREATE A 'JwtFilter' CLASS EXTENDING 'OncePerRequestFilter' CLASS.
2. OVERRIDE THE METHOD 'doFilterInternal'. IN THE METHOD,
3. FIRST CHECK FOR BEARER TOKEN IN THE REQUEST AND EXTRACT IT.
4. USING THE TOKEN, GET THE USERNAME OF THE USER.
5. SECOND STEP IS TO CHECK IF THE USERNAME EXISTS AND THE AUTHENTICATION CONTEXT IS EMPTY, THEN GET THE USER DETAILS USING THE USERNAME FROM YOUR USER DETAILS SERVICE.
6. THIRD STEP IS TO SEND THE EXTRACTED TOKEN AND USER DETAILS FOR TOKEN VALIDATION.
7. FOURTH STEP IS TO CREATE 'UsernamePasswordAuthenticationToken' TOKEN, SET USER DETAILS TO IT, THEN ADD IT TO THE AUTHENTICATION CONTEXT.
8. NOW WE TELL SPRING SECURITY TO CONTINUE FILTERING REQUEST AND RESPONSE WITH OTHER FILTERS.
9. FINALLY, WE GO TO OUR SECURITY CONFIG, AND SET THE SESSION AS STATELESS IN THE 'filterChain' METHOD. THEN WE TELL 'HttpSecurity' TO ADD OUR JWT FILTER BEFORE 'UsernamePasswordAuthenticationFilter'.
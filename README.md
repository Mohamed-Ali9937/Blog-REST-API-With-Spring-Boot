# **Blog-REST-API-With-Spring-Boot**

## **This is a blog rest api with secured endpoints as follows:**

## **1. Post Resource**
![Post APIs](https://user-images.githubusercontent.com/97092441/232326073-ecd29959-2dbe-482b-98bb-78779a153109.png)

### - **createPost:** 
This endpoint is used to create a new post.  
- **Only Authenticated users can create new posts**
---
### - **getAllPosts:** 
This endpoint is used to get all posts  
- **It is permitted to all**
---
### - **getPost:** 
This endpoint is used to get a specific post  
- **It is permitted to all**
---
### - **updatePost:** 
This endpoint is used to update specific post  
- **Only permitted for users with ADMIN role or the user who created the post**
---
### - **deletePost:** 
This endpoint is used to delete specific post  
- **Only permitted for users with ADMIN role or the user who created the post**
---
---
---
## **2. Comment Resource**
![Comment APIs](https://user-images.githubusercontent.com/97092441/232326061-63e0b815-8e19-46d0-8f1b-681aab3e741f.png)

### - **createComment:** 
This endpoint is used to create a new comment.  
- **Only Authenticated users can create new comments**
---
### - **getCommentsByPost:** 
This endpoint is used to get all comments of a specific post  
- **It is permitted to all**
---
### - **getComment:** 
This endpoint is used to get a specific comment
- **It is permitted to all**
---
### - **updateComment:**
This endpoint is used to update specific comment
- **Only permitted for users with ADMIN role or the user who created the can update it**
---
### - **deletePost:** 
This endpoint is used to delete specific post  
- **Only permitted for users with ADMIN role or the user who created the comment**
---
---
---
## **3. Category Resource**
![Category APIs](https://user-images.githubusercontent.com/97092441/232326044-2517741a-2f67-42c5-9914-360f5189bb2d.png)

### - **createCategory:** 
This endpoint is used to create a new category.  
- **Only users with ADMIN role can create new categories**
---
### - **getAllCategories:** 
This endpoint is used to get all categories  
- **It is permitted to all**
---
### - **getCategory:** 
This endpoint is used to get a specific category
- **It is permitted to all**
---
### - **updateCategory:**
This endpoint is used to update a specific category
- **Only the user who has ADMIN role can update a category**
---
### - **deleteCategory:** 
This endpoint is used to delete specific category
- **Only the user who has ADMIN role can delete a category**
---
### - **getAllCategoryPosts:** 
This endpoint is used to get all posts of a specific category  
- **It is permitted to all**
---
---
---
## **4. User Resource**
![image](https://user-images.githubusercontent.com/97092441/232326253-fafe4964-f4fe-472d-9634-e152ba0b28ed.png)

### - **getAllUsers:** 
This endpoint is used to get all registered users
- **Only users with ADMIN role can create new categories**
---
### - **getUser:** 
This endpoint is used to get a specific user
- **It is permitted to all**
---
### - **updateUser:**
This endpoint is used to update a specific user
- **Only permitted to the user who has ADMIN role or the user that requires updates is the current authenticated user**
---
### - **deleteUser:** 
This endpoint is used to delete specific user
- **Only the user who has ADMIN role can delete a user**
---
### - **getPostsCreatedByUser:**  
This endpoint is used to get all posts created by a specific user
- **It is permitted to all**
---
---
---
## **5. Auth Resource**
![Auth APIs](https://user-images.githubusercontent.com/97092441/232326004-b1cba744-8dc3-4411-814a-5445cb85e035.png)

### - **login:** 
This endpoint is used by users to login
- **It is permitted to all**
---
### - **register:** 
This endpoint is used to register a new user
- **It is permitted to all**

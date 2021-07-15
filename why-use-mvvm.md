## Why use MVVM architecture?

Developers always prefer a clean and structured code for the projects.
By organizing the codes according to a design pattern helps in the maintenance of the software
. By having knowledge of all crucial logic parts of the android application, it is easier to add and remove app features.

Model — View — ViewModel ([MVVM](https://developer.android.com/jetpack/guide?gclid=CjwKCAjwtdeFBhBAEiwAKOIy54BbAQsy_XH_sdwfTNM0Ben6F4omB1u3v5rUrEt6JhV7m0CokKUUGxoCRgsQAvD_BwE&gclsrc=aw.ds)) is the industry-recognized software architecture pattern that overcomes all drawbacks of MVP and MVC design patterns. MVVM suggests separating the
data presentation logic(Views or UI) from the core business logic part of the application.
 MVVM is the best way to structure code.
 The separate code layers of MVVM are:
  -Model
  -View
  -ViewModel

**Benefits of using MVVM pattern are as follows :**
- The UI components are kept away from the business logic- 
  
  So ,Designers and developers can work independently and concurrently on their components during the development process. Designers can focus on the view, while developers can work on the view model and model components.
- The business logic is kept away from the database operations-

   If there's an existing model implementation that encapsulates existing business logic, it can be difficult or risky to change it. In this scenario, the view model acts as an adapter for the model classes and enables you to avoid making any major changes to the model code.
- It's easy to understand and read  
- And if done correctly, you have a lot less to worry about when it comes to lifecycle events (eg: screen rotations)

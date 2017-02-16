# SpringBootKotlinDemo
A repository to show the speed of bootstrapping a kotlin and spring boot project

If there's anything that's incorrect, or anything that I missed, submit a PR!


[Here are the slides that accompany the talk](http://www.slideshare.net/YousufHaque/spring-boot-kotlin-lt3)

1. You will need to set up a firebase project. Instructions to configure the project can be found [here](https://firebase.google.com/docs/admin/setup)

- The boilerplate for setting up firebase is already in the project, you just need to download the firebase config.json and specify the path in the DemoApplicaton class variable called `firebaseConfigPath`
You also need to set the url of your firebase database to the variable `firebaseDatabseUrl` 

2. If you're using intellij, you can just run the main function, or if youre using gradle you can run the `./gradlew bootRun` task

Here are some resources that helped me put this demo together.


[Developing Spring boot applications with kotlin](https://spring.io/blog/2016/02/15/developing-spring-boot-applications-with-kotlin)

[Introducing Kotlin support in spring framework 5.0](https://spring.io/blog/2017/01/04/introducing-kotlin-support-in-spring-framework-5-0)

[Spring Data JPA query method reference](http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation)

[Custom spring repository methods in java](http://stackoverflow.com/questions/11880924/how-to-add-custom-method-to-spring-data-jpa)

[Spring jpa specification in java](https://spring.io/blog/2011/04/26/advanced-spring-data-jpa-specifications-and-querydsl/)

[Spring jpa specification kotlin DSL](https://github.com/consoleau/kotlin-jpa-specification-dsl)

[Kotlin Exposed sql wrapper and DAO made by jetbrains](https://github.com/JetBrains/Exposed)

[Talking kotlin podcast with an episode about Spring boot and kotlin](http://talkingkotlin.com/spring-boot-with-sebastian-delueze/)


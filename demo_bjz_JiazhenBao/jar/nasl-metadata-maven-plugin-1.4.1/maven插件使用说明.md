NASL Metadata Maven Plugin
===========
扩展依赖库maven插件，用于通过collector脚手架编译后进行打包，会生成`library-<libraryName>-<version>.zip`格式压缩文件，支持在低代码平台直接进行上传。
压缩包中会包括当前依赖库的描述文件和所有的依赖jar包。在上传到平台时，会自动将所有的依赖项一并进行上传以保证依赖库的可用性。
## 部署方式
### 本地部署
1. 验证是否正确安装maven
    ```shell
    $ mvn -version                                                                                                                                                                        23-07-03 - 10:51:24
    Apache Maven 3.6.3 (cecedd343002696d0abb50b32b541b8a6ba2883f)
    Maven home: /Users/XXX/Documents/apache-maven-3.6.3
    Java version: 17.0.6, vendor: GraalVM Community, runtime: /Library/Java/JavaVirtualMachines/graalvm-ce-java17-22.3.1/Contents/Home
    Default locale: zh_CN_#Hans, platform encoding: UTF-8
    OS name: "mac os x", version: "13.4", arch: "aarch64", family: "mac"
    ```
    输出如下内容则表示maven已经安装成功，否则请参考[官方文档](https://maven.apache.org/install.html)进行安装。
2. 将[Jar文件]和[Pom文件]下载到本地并安装到本地仓库
    ```shell
    $ mvn install:install-file -Dfile=nasl-metadata-maven-plugin-1.4.1.jar -DpomFile=pom.xml
    ```
    输出如下内容则表示安装成功
    ```shell
    [INFO] Scanning for projects...
    [INFO]
    [INFO] ------------------< org.apache.maven:standalone-pom >-------------------
    [INFO] Building Maven Stub Project (No POM) 1
    [INFO] --------------------------------[ pom ]---------------------------------
    [INFO]
    [INFO] --- maven-install-plugin:2.4:install-file (default-cli) @ standalone-pom ---
    [INFO] Installing /Users/XXX/Documents/IdeaProjects/nasl-metadata-maven-plugin/target/nasl-metadata-maven-plugin-1.4.1.jar to /Users/XXX/.m2/repository/com/netease/lowcode/nasl-metadata-maven-plugin/1.4.1/nasl-metadata-maven-plugin-1.4.1.jar
    [INFO] Installing /Users/XXX/Documents/IdeaProjects/nasl-metadata-maven-plugin/pom.xml to /Users/XXX/.m2/repository/com/netease/lowcode/nasl-metadata-maven-plugin/1.4.1/nasl-metadata-maven-plugin-1.4.1.pom
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time:  0.144 s
    [INFO] Finished at: 2023-07-03T11:03:12+08:00
    [INFO] ------------------------------------------------------------------------
    ```
   
### 上传到私服
将[Jar文件]和[Pom文件]下载到本地并上传到私服，具体操作请参考[官方文档](https://maven.apache.org/guides/mini/guide-3rd-party-jars-remote.html)。
也可以通过图形化界面的方式进行上传。
## 使用方式
在Pom文件中添加插件依赖，如下所示：
```xml
<plugin>
    <groupId>com.netease.lowcode</groupId>
    <artifactId>nasl-metadata-maven-plugin</artifactId>
    <version>1.4.1</version>
    <executions>
        <execution>
            <goals>
                <goal>archive</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
会在执行打包时，生成一个`library-<libraryName>-<version>.zip`格式压缩文件。
### 配置项
#### jarWithDependencies
在打包时是否已经将所有的依赖项打包到jar文件中，默认为false。
如果设置为true的话，则会生成一个空的pom文件使依赖不会进行传递。
```xml
<plugin>
   ...
   <configuration>
      <jarWithDependencies>false</jarWithDependencies>
   </configuration>
   ...
</plugin>
```

#### includes
需要打包的依赖项，如果不设置则会打包所有的依赖项。
```xml
<plugin>
   ...
   <configuration>
      <includes>
         <include>
            <groupId>include-group-id</groupId>
            <artifactId>include-artifact-id</artifactId>
         </include>
      </includes>
   </configuration>
   ...
</plugin>
```

#### includeGroupIds
需要打包的依赖项的groupId通过`,`进行分割，如果不设置则会打包所有的依赖项。
```xml
<plugin>
   ...
   <configuration>
      <includeGroupIds>include-group-id1,include-group-id2</includeGroupIds>
   </configuration>
   ...
</plugin>
```

#### excludes
不需要打包的依赖项，如果不设置则会打包所有的依赖项。
```xml
<plugin>
   ...
   <configuration>
      <excludes>
         <exclude>
            <groupId>exclude-group-id</groupId>
            <artifactId>exclude-artifact-id</artifactId>
         </exclude>
      </excludes>
   </configuration>
   ...
</plugin>
```

#### excludeGroupIds
不需要打包的依赖项的groupId通过`,`进行分割，如果不设置则会打包所有的依赖项。
```xml
<plugin>
   ...
   <configuration>
      <excludeGroupIds>exclude-group-id1,exclude-group-id2</excludeGroupIds>
   </configuration>
   ...
</plugin>
```

#### includeSystemScope
是否需要打包系统依赖项，默认为false。设置为true则会包含`<scope>system</scope>`的依赖项。
```xml
<plugin>
   ...
   <configuration>
      <includeSystemScope>false</includeSystemScope>
   </configuration>
   ...
</plugin>
```

#### skip
是否跳过打包，默认为false。设置为true则会跳过打包。
```xml
<plugin>
   ...
   <configuration>
      <skip>false</skip>
   </configuration>
   ...
</plugin>
```

#### rewriteVersion
是否重写版本号，默认为true。默认改写GAV坐标中的版本号,会加上时间戳后缀(3.7.3版本后平台支持改写版本号上传).
```xml
<plugin>
   ...
   <configuration>
      <rewriteVersion>true</rewriteVersion>
   </configuration>
   ...
</plugin>
```

#### rewrittenGroupId
改写GAV坐标中的groupId,如果设置了该值,则会改写GAV坐标中的groupId为对应值.
```xml
<plugin>
   ...
   <configuration>
      <rewrittenGroupId>aaa.bbb.ccc</rewrittenGroupId>
   </configuration>
   ...
</plugin>
```

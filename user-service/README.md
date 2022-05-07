# 디스커버리 client server


```yaml
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZon: http://127.0.0.1:8761/eureka
```

* `eureka.client.register-with-eureka`
  * 레지스트리에 자신을 등록할지에 대한 여부이다.
  * `default`는 `true`


* `eureka.client.fetch-registry`
  * `registry`에 있는 정보를 가져올지 여부이다. 
  * `default`는 `true`
  * 30초마다 `Eureka Client` 가 유레카 레지스트리 변경 사항 여부 재확인한다.

```yaml
server:
  port: 0
```

* 0번으로 지정시 랜덤 포트를 사용하겠다는 의미이다.
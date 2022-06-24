# spring-cloud

## discovery-service

* MSA와 같은 분산환경은 서비스간 http 통신으로 구성된다.
* 클라우드 환경이 되면서 서비스가 오토 스케일링등 동적으로 생성되거나 컨테이너 기반의 배포로 인해 서비스 ip가 동적으로 변경되는 일이 잦아졌다.
  그래서 서비스를 호출할때 서비스 위치(ip, port)를 알아낼 수 있는 기능이 필요한데, 이것이 service discovery이다.

## API Gateway service

* 인증 및 권한 부여
* 마이크로서비스 검색 통합
* 응답할 수 있는 캐싱 정보 저장
* 일괄 정책, 마이크로서비스 문제시 회로 차단기 기능
* 속도 제한 및 부하 분산
* 로깅, 추적, 상관 관계
* 헤더, 쿼리 스트링 및 요청 데이터 값 변환
* `IP` 허용 목록 추가

### Spring Cloud MSA Client

1. `RestTemplate`
    ```java
    RestTemplate restTemplate = new RestTemplate();
    String fooResourceUrl = "http://localhost:8080/spring-rest/foos";
    ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl + "/1", String.class);
    Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    ```
   * [Spring RestTemplate 문서](https://www.baeldung.com/rest-template)


2. `Feign Client`
   ```java
   @FeignClient("name")
   public interface NameService {
        @RequestMapping("/")
        public String getName();
    }   
   ```
   * [Spring Feign client 문서](https://spring.io/projects/spring-cloud-openfeign)
   
## Netflix Ribbon

### Ribbon :  client side Load Balancer

* 마이크로서비스 이름으로 호출.
* 헬스 체크 제공.
* `Spring Cloud Ribbon`은 `Spring Boot 2.4`에서 `Maintenance`상태
* 공식 문서에서 `zuul` 대안으로 `Spring Cloud Loadbalancer`를 사용하라고 제안하고 있다.
* [Spring Cloud Loadbalancer 공식 문서](https://spring.io/guides/gs/spring-cloud-loadbalancer/)   

## Netflix zuul

* `Gateway` 역활을 담당한다.
* `Spring Cloud Zuul`은 `Spring Boot 2.4`에서 `Maintenance`상태
  [(Spring Cloud Zuul Maintenance 상태 설명)](https://spring.io/blog/2018/12/12/spring-cloud-greenwich-rc1-available-now#spring-cloud-netflix-projects-entering-maintenance-mode)
* 공식 문서에서 `zuul` 대안으로 `Spring Cloud Gateway`를 사용하라고 제안하고 있다.
* [Spring Cloud Gateway 공식 문서](https://spring.io/projects/spring-cloud-gateway)

## 디스커버리 client server


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

## spring boot Actuator

### 개요

* 애플리케이션이 최초 빌드될때 `yml`파일에 있는 설정 정보를 이용해 애플리케이션을 구동시킨다.
* 하지만 최초 빌드 시점에만 `config` 서버로 부터 설정 정보를 가져와 이용하기 때문에 **마이크로서비스들이 다시 빌드되지 않는 이상 설정 정보들은 반영되지 않는다.**
* 간단하게 다시 애플리케이션을 구동시키면 설정 정보가 동기화 되지만 마이크로서비스가 100개가 있다고 생각해보자 100개의 서비스를 다 재기동 시켜야 할까?
* 해결 방법은 `spring boot Actuator`를 사용하는 것이다.

### 다양한 기능

* [Actuator 공식 문서](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
>Spring Boot includes a number of additional features to help you monitor and manage your application when you push it to production. You can choose to manage and monitor your application by using HTTP endpoints or with JMX. Auditing, health, and metrics gathering can also be automatically applied to your application.

* 어플리케이션을 모니터링하고 관리하는 기능을 `Spring Boot`에서 자체적으로 제공해주는데 그게 `Actuator`라고 한다. 
 그리고 `Actuator`는 `http`와 `JMX`를 통해 확인할 수 있다.
* `spring boot Actuator`는 애플리케이션의 모니터링 관련한 다양한 기능을 제공한다.
* 애플리케이션의 실행 여부, 패키지 로깅 레벨, Bean 목록, Metric(CPU, Heap, Thread 등) 등 여러 정보들을 확인할 수 있는 기능을 제공한다.

### 엔드포인트

ID|설명
---|---|
beans| 애플리케이션에 있는 모든 빈의 전체 목록을 표시
health|애플리케이션 상태 정보를 표시
metrics|애플리케이션의 각종 지표(metrics)정보를 표시
httptrace|HTTP 추적 정보를 표시(기본적으로 마지막 100개의 HTTP 요청-응답 교환)
shutdown|애플리케이션을 정상적으로 종료한다. 기본적으로 비활성화

* 자세한 정보는 [Actuator 엔드포인트](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints) 에서 확인할 수 있다.

### 설정

* 의존성 추가

```gradle
implementation 'org.springframework.boot:spring-boot-starter-actuator'
```

* `application.yml`에 `include` 추가

```yml
management:
  endpoints:
    web:
      exposure:
        include: refresh, health, beans
```


## spring cloud bus

### 개요

* `config` 정보가 변경되면 각각의 마이크로 서비스는 변경된 값을 가져오기 위해 `/actuator/refresh`를 `POST`로 호출하여 동기화를 해줘야 한다.
* 이러한 불편함을 해소하기 위해 동적으로 `config` 변경을 동기화 하기 위한 ` message que` 를 연결하는 역활을 한다.

### 설정

* `rabbitmq` 설치
> brew update
<br></br>
> brew install rabbitmq
* rabbitmq 실행
<br></br>
> cd /usr/local/sbin
<br></br>
> ./rabbitmq-server   


* 의존성 추가

```gradle
implementation 'org.springframework.cloud:spring-cloud-starter-bus-amqp'
```

* `yml` 파일 설정

```yml
spring:
  rabbitmq:
    host: 127.0.0.1
    port: 5672
    username: guest
    password: guest

management:
  endpoints:
    web:
      exposure:
        include: health, busrefresh
```





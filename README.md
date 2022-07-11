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
*  `Rest Call`을 추상화 한 `Spring Cloud Netfix` 라이브러리


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

* `Ribbon`은 load balancing을 요청 애플리케이션 단에서 수행해주는 `client side Load Balancer`이다.
* `Ribbon`과 같이 `Load Balancer`가 필요한 이유는 **부하 분산을 적절하게 하여 서비스의 가용성을 최대화하기 위함**이다.
* 마이크로서비스 이름으로 호출.
* 헬스 체크 제공.
* `Spring Cloud Ribbon`은 `Spring Boot 2.4`에서 `Maintenance`상태
* 공식 문서에서 `zuul` 대안으로 `Spring Cloud Loadbalancer`를 사용하라고 제안하고 있다.
* [Spring Cloud Loadbalancer 공식 문서](https://spring.io/guides/gs/spring-cloud-loadbalancer/)   

## Netflix zuul

* `Netflix`에서 제공하는 `API Gateway`로 마이크로 서비스 아키텍쳐에서 여러 클라이언트 요청을 적절한 서비스로 프록시 및 라우팅하기 위한 서비스이다.
* `Gateway` 역활을 담당한다.
* `zuul`은 내부적으로 `Eureka` 서버를 사용하고 `zuul` 또한 `Eureka Clien`이다.
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

## Apache Kafka

* `Apache Software Foundation`으로 `Scalar` 언어로 된 오픈 소스 메시지 브로커 프로젝트
* 링크드인에서 개발, 2011년 오픈 소스화
* 실시간 데이터 피드를 관리하기 위해 통일된 높은 처리량, 낮은 지연 시간을 지닌 플랫폼 제공
* Apple, Netfilx, kakao 등 여러 기업에서 사용

### Kafka 도입 이전

* `End-To-End` 연결 방식의 아키텍처
* 데이터 연동의 복잡성 증가 (HW, 운영체제, 장애 등)
* 서로 다른 데이터 `Pipeline` 연결 구조
* 확장이 어려운 구조


**모든 시스템으로 데이터를 실시간으로 전송하여 처할 수 있는 시스템** <br>
**데이터가 많아지더라도 확장이 용이한 시스템**

### 특징

* 높은 처리량과 낮은 지연시간
* 높은 확장성
* 고가용성
* 내구성 
  * 프로듀서에 의해 카프카로 전송되는 모든 메세지는 안전한 저장소인 카프카 로컬 디스크에 저장된다.
* 개발 편의성
* 운영 및 관리 편의성

### Kafka Broker

* 실행된 `Kafka` 애플리케이션 서버
* 3대 이상의 `Broker Cluster` 구성
* `Zookeeper` 연동
  *  역할 : 메타데이터(`Broker ID, Controller ID` 등) 저장
  * `Controller` 정보 저장
* n개 `Broker` 중 1대는 `Controller` 기능 수행
  * `Controller` 역할
    * 각 `Broker`에게 담당 파티션 할당 수행
    * `Broker` 정상 동작 모니터링 관리

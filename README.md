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

## Kafka 구성 요소

### Topic

* 각각의 메시지를 목적에 맞게 구분할 때 사용한다.
* 메시지를 전송하거나 소비할 때 `Topic`을 반드시 입력한다.
* `Consumer`는 자신이 담당하는 `Topic`의 메시지를 처리한다.
* `한 개의 토픽은 한 개 이상의 파티션으로 구성된다.

### Partition

* 분산 처리를 위해 사용된다.
* `Topic` 생성 시 `partition` 개수를 지정할 수 있다. (파티션 개수 변경 가능. 추가만 가능)
* 파티션이 1개라면 모든 메시지에 대해 순서가 보장된다.
* 파티션 내부에서 각 메시지는 `offset(고유 번호)로 구분된다.
* 파티션이 여러개라면 `Kafka` 클러스터가 라운드 로빈 방식으로 분배해서 분산처리되기 때문에 순서 보장이 안된다.
* 파티션이 많을 수록 처리량이 좋지만 장애 복구 시간이 늘어난다.

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

## CircuitBreaker

* 장애가 발생하는 서비스에 반복적인 호출이 되지 못하게 차단
* 특정 서비스가 정상적으로 동작하지 않을 경우 다른 기능으로 대체 수행 -> 장애 회피
* http://martinfowler.com/bliki/CircuitBreaker.html

### CircuitBreaker 상태

* CLOSED
  * 초기 상태로 모든 마이크로 서비스에 접속 가능 상태
* OPEN
  * 서비스가 정상적으로 동작하지 않거나 에러가 발생하는 경우가 임계치를 넘어 open 상태가 됨

## Microservice 분산 추적

### Zipkin

* https://zipkin.io/
* Twitter에서 사용하는 분산 환경의 Timing 데이터 수집, 추적 시스템 (오픈소스)
* Google Drapper에서 발전하였으며, 분산환경에서의 시스템 병복 현상 파악
* Collector, Query Service, Database WebUI로 구성
* **Span**
  * 하나의 요청에 사용되는 작업의 단위
  * 64 bit unique ID

* **Trace**
  * 트리 구조로 이뤄진 Span 셋
  * 하나의 요청에 대한 같은 Trace ID 발급

### Spring Cloud Sleuth

* 스프링 부트 애플리케이션을 zipkin과 연동
  * 스프링 부트 서버에 가지고 있는 로그 파일 데이터나 스트리밍 데이터를 zipkin에 전달하는 역활
* 요청 값에 따른 Trace ID, Span ID 부여
* Trace와 Span Ids를 로그에 추가 가능
  * servlet filter
  * rest template
  * scheduled actions
  * message channels
  * feign client


### Turbin Service

* 마이크로서비스에 설치된 Hystrix 클라이언트의 스트림을 통합
  * 마이크로서비스에서 생성되는 Hrystrix 클라이언트 스트림 메시지를 터빈 서버로 수집

```yml
#Turbin Server
turbine:
 appconfig:
           msa-service-product-order,
           msa-service-product-member,
           msa-service-product-status
 clusterNameExpression: new String("default")
```

* 상품주문(productOrder)

```yml
spring:
 application:
  name: msa-service-order
```

* 회원 확인(productMember)

```yml
spring:
 application:
  name: msa-service-member
```

* 배송 처리(productDelivery)

```yml
spring:
 application:
  name: msa-service-delivery
```

#### Hystrix Dashboard

* Hystrix 클라이언트에서 생성하는 스트림을 시각화


#### Micrometer

* Micrometer
  * https://micrometer.io/
  * JVM기반의 애플리케이션의 Metrics 제공
  * Spring Framework 5, Spring Boot 2부터 Spring의 Metrics 처리
  * Prometheus등의 다양한 모니터링 시스템 지원

  ```gradle
  implementation 'io.micrometer:micrometer-registry-prometheus'
  ```

* Timer
  * 짧은 지연 시간, 이벤트의 사용 빈도를 측정
  * 시계열로 이벤트의 시간, 호출 빈도 등을 제공
  * @Timed 제공

#### Prometheus, Grafana

* Prometheus
  * Metrics를 수집하고 모니터링 및 알람에 사용되는 오픈소스 애플리케이션
  * 2016년 부터 CNCF에서 관리되는 2번째 공식 프로젝트
    * Level DB -> Time Series Database(TSDB)
  * Pull 방식의 구조와 다양한 Metric Exporter 제공
  * 시계열 DB에 Metrics 저장 -> 조회 가능 (Query)
  * https://prometheus.io/download/  다운로드

* Grafana
  * 데이터 시각화, 모니터링 및 분석을 위한 오픈소스 애플리케이션
  * 시계열 데이터를 시각화하기 위한 대시보드 제공


## docker 실행

```
$ docker run [OPTIONS] IMAGE[:TAG|@DIGEST][COMMAND][ARG...]
```

옵션|설명
---|---|
-d | detached mode 흔히 말하는 백그라운드 
-p | 호스트와 컨테이너의 포트를 연결 (포워딩)
-v | 호스트와 컨테이너의 디렉토리를 연결 (마운트)
-e | 컨테이너 내에서 사용할 환경변수 설정
--name | 컨테이너 이름 설정
--rm | 프로세스 종료시 컨테이너 자동 제거
-it | -i와 -t를 동시에 사용한 것으로 터미널 입력을 위한 옵션
--link | 컨테이너 연결 [컨테이너명:별칭]

```
$ docker run ubuntu:16.04
```

### Create Bridge Network

* Bridge network
  ```
  $ docker network create --driver bridge [브릿지 이름]
  ```

* Host network
  * 네트워크를 호스트로 설정하면 호스트의 네트워크 환경을 그대로 사용
  * 포트 포워딩 없이 내부 어플리케이션 사용

* None network
  * 네트워크를 사용하지 않음
  * IO 네트워크만 사용, 외부와 단절

```
$ docker network create ecommerce-network
& docker network ls
```

## Event Driven Architecture

### Monolithic

* 단일 데이터베이스
* 트랜잭션 처리 -> ACID
  * Atomicity   (원자성)
  * Consistency (일관성)
  * Isolation   (독립성)
  * Durable     (지속성)

### Microservice

* 각 서비스마다 독립적인 언어 및 DB (Plyglot)
* API를 통해 접근

### Event Sourcing

* 데이터의 마지막 상태만 저장하는 것이 아닌, 해당 데이터에 수행된 전체 이력을 기록
* 데이터 구조 단순
* 데이터의 일관성과 트랜잭션 처리 가능
* 데이터 저장소의 개체를 직접 업데이트 하지 않기 때문에, 동시성에 대한 충돌 문제 해결


* 도메인 주도 설계(Domain-Driven Design)
  * Aggregate
  * Projection
* 메시지 중심의 비동기 작업 처리
* 단점
  * 모든 이벤트에 대한 복원
  * 다양한 데이터 조회

### CQRS(Command and Query Responsibility Segregation)

* 명령과 조회의 책임 분리
  * 상태를 변경하는 Command
  * 조회를 담당하는 Query

## Saga pattern

* Application에서 Transaction 처리
  * Choreography, Orchestration
* Application이 분리된 경우에는 각각의 Local transaction만 처리
* 각 App에 대한 연속적인 Transaction에서 실패할 경우
  * Rollback 처리 구현 -> 보상 Transaction
* 데이터의 원자성을 보장하지는 않지만, 일관성을 보장

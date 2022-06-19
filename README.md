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

## Netflix Ribbon

### Spring Cloud MSA 

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





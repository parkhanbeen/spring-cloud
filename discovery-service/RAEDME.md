# discovery-service

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

//package com.b1nd.dauthserver.infrastructure.adapter.driven.client
//
//import com.b1nd.dauthserver.domain.common.error.BasicException
//import com.b1nd.dauthserver.domain.common.error.InternalServerException
//import org.slf4j.LoggerFactory
//import org.springframework.http.HttpHeaders
//import org.springframework.http.HttpStatusCode
//import org.springframework.stereotype.Component
//import org.springframework.util.MultiValueMap
//import org.springframework.web.reactive.function.BodyInserters
//import org.springframework.web.reactive.function.client.ClientResponse
//import org.springframework.web.reactive.function.client.WebClient
//import reactor.core.publisher.Mono
//
//@Component
//class WebClientSupport(
//    private val webClient: WebClient
//) {
//    private val log = LoggerFactory.getLogger(WebClientSupport::class.java)
//
//    fun <T> get(url: String,
//                responseDtoClass: Class<T>,
//                vararg headers: String,
//                exception: BasicException): Mono<T> {
//        return webClient.get()
//            .uri(url)
//            .headers { convertStringToHttpHeaders(*headers).invoke(it) }
//            .retrieve()
//            .onStatus(HttpStatusCode::isError) { onError(it, exception) }
//            .bodyToMono(responseDtoClass)
//    }
//
//    fun <T, V : Any> post(url: String,
//                          body: V,
//                          responseClass: Class<T>,
//                          vararg headers: String?,
//                          exception: BasicException): Mono<T> {
//        return webClient.post()
//            .uri(url)
//            .headers { convertStringToHttpHeaders(*headers).invoke(it) }
//            .bodyValue(body)
//            .retrieve()
//            .onStatus(HttpStatusCode::isError) { onError(it, exception) }
//            .bodyToMono(responseClass)
//    }
//
//    fun <T> postWithFormData(
//        url: String,
//        body: MultiValueMap<String, String>,
//        responseClass: Class<T>,
//        vararg headers: String,
//        exception: BasicException
//    ): Mono<T> {
//        return webClient.post()
//            .uri(url)
//            .headers { convertStringToHttpHeaders(*headers).invoke(it) }
//            .body(BodyInserters.fromFormData(body))
//            .retrieve()
//            .onStatus(HttpStatusCode::isError) { onError(it, exception) }
//            .bodyToMono(responseClass)
//    }
//
//    private fun onError(response: ClientResponse, exception: BasicException): Mono<Throwable> {
//        log.error("WebClient Error Status : ${response.statusCode()}")
//        log.error("WebClient Error Body : ${response.bodyToMono(String::class.java)}")
//        return Mono.error(exception(response.statusCode().value()))
//    }
//
//    private fun convertStringToHttpHeaders(vararg headers: String?): (HttpHeaders) -> Unit {
//        if (headers.size % 2 != 0) {
//            throw InternalServerException
//        }
//        return { httpHeaders ->
//            headers.asList().chunked(2) { (key, value) ->
//                if (key != null) {
//                    httpHeaders.add(key, value)
//                }
//            }
//        }
//    }
//}

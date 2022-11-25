package com.example.demo.Controller

import org.springframework.http.CacheControl
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

fun responseAlignment(headers: Map<String, String>): String {
    var response: String = ""
    headers.forEach { entry ->
        response += "${entry.key} : ${entry.value}<br>"
    }
    return response
}

@RestController
@RequestMapping("/alb/")
class MessageResource {
    @GetMapping
    fun index(): String {
        return "Hello! This is testAPI.<br> You can test your request."
    }

    @GetMapping("/listHeaders")
    fun listAllHeaders(@RequestHeader headers: Map<String, String>): ResponseEntity<String> {
        println(headers)
        val responseHeaders = HttpHeaders()
        responseHeaders.vary = listOf("User-Agent", "Accept-Encoding")
        responseHeaders.setCacheControl(CacheControl.noCache().sMaxAge(Duration.ofMillis(1000)))
        responseHeaders.setCacheControl(CacheControl.noStore())
        responseHeaders.contentType = MediaType.TEXT_HTML
        return ResponseEntity.ok().headers(responseHeaders).body(responseAlignment(headers))
    }

    @GetMapping("/listHeaders/{httpCode}")
    fun headerWithStatus(
        @RequestHeader requestHeaders: Map<String, String>,
        @PathVariable httpCode: Int
    ): ResponseEntity<String> {
        val responseHeaders = HttpHeaders()
        responseHeaders.contentType = MediaType.TEXT_HTML
        return ResponseEntity.status(httpCode).headers(responseHeaders)
            .body("<h2>Status Code=" + httpCode.toString() + "</h2><br>" + responseAlignment(requestHeaders))
    }

    @GetMapping("/listHeaders/{httpCode}/delay/{duration}")
    fun headerWithStatusAndDelay(
        @RequestHeader headers: Map<String, String>,
        @PathVariable httpCode: Int,
        @PathVariable duration: Long
    ): ResponseEntity<String> {
        Thread.sleep(duration)
        val responseHeaders = HttpHeaders()
        responseHeaders.contentType = MediaType.TEXT_HTML
        return ResponseEntity.status(httpCode).headers(responseHeaders).body(
            "<h1>Status Code=" + httpCode.toString() + "</h1><br>" + "<h2>delay=" + duration.toString() + "milliseconds</h2><br>" + responseAlignment(
                headers
            )
        )
    }

    @GetMapping("/delay/{duration}")
    fun headerWithDelay(
        @RequestHeader headers: Map<String, String>,
        @PathVariable duration: Long
    ): ResponseEntity<String> {
        Thread.sleep(duration)
        val responseHeaders = HttpHeaders()
        responseHeaders.contentType = MediaType.TEXT_HTML
        return ResponseEntity.ok().headers(responseHeaders)
            .body("<h1>delay=" + duration.toString() + "milliseconds</h1><br>" + responseAlignment(headers))
    }
}

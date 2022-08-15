package com.example.demo.Controller


import org.springframework.data.annotation.Id
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.http.ResponseEntity

fun responseAlignment(headers: Map<String, String>):String{
    var response : String =""
    headers.forEach { entry ->
            response += "${entry.key} : ${entry.value}<br>";
        }
    return response
    }
@RestController
@RequestMapping("/")
class MessageResource {
    @GetMapping
    fun index(): String{
        return "Hello! This is testAPI.<br> You can test your request."
    }
    
    @GetMapping("/listHeaders")
    fun listAllHeaders(@RequestHeader headers: Map<String, String>): ResponseEntity<String> {
        println(headers)
        return ResponseEntity.ok().body(responseAlignment(headers))
        }
        
    @GetMapping("/listHeaders/{httpCode}")
    fun headerWithStatus(
        @RequestHeader headers: Map<String, String>,
        @PathVariable httpCode: Int,
        ): ResponseEntity<String> {
        return ResponseEntity.status(httpCode).body("<h2>Status Code=" + httpCode.toString() + "</h2><br>"+responseAlignment(headers))
        }
    
    @GetMapping("/listHeaders/{httpCode}/delay/{duration}")
    fun headerWithStatusAndDelay(
        @RequestHeader headers: Map<String, String>,
        @PathVariable httpCode: Int,
        @PathVariable duration: Long,
        ): ResponseEntity<String> {
        Thread.sleep(duration)
        return ResponseEntity.status(httpCode).body("<h1>Status Code=" + httpCode.toString() + "</h1><br>" + "<h2>delay=" + duration.toString() + "milliseconds</h2><br>" + responseAlignment(headers))
    
        }
    
    @GetMapping("/delay/{duration}")
    fun headerWithDelay(
        @RequestHeader headers: Map<String, String>,
        @PathVariable duration: Long,
        ): ResponseEntity<String> {
        Thread.sleep(duration)
        return ResponseEntity.ok().body("<h1>delay=" + duration.toString() + "milliseconds</h1><br>" + responseAlignment(headers))
        }

}


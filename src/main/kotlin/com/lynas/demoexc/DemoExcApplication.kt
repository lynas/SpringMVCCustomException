package com.lynas.demoexc

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.repository.CrudRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@SpringBootApplication
class DemoExcApplication

fun main(args: Array<String>) {
    SpringApplication.run(DemoExcApplication::class.java, *args)
}

@RestController
class HomeController(val appUserService: AppUserService){

    @RequestMapping("/")
    fun home() = "Hello world"


    @RequestMapping("/{id}")
    fun findById(@PathVariable id: Long): AppUser {
        if (id > 100) {
            throw ConstrainViolationException("id can't be grater than 100")
        }
        return appUserService.findById(id)
    }
}

@Entity
class AppUser(@Id @GeneratedValue val id:Long)


interface AppUserRepo : CrudRepository<AppUser, Long>

@Service
class AppUserService(val appUserRepo: AppUserRepo){

    fun findById(id: Long): AppUser {
        return appUserRepo.findOne(id) ?: throw EntityNotFoundException("AppUser not found with given id : "+id)
    }
}

@ResponseStatus(value=HttpStatus.NOT_FOUND)
class EntityNotFoundException(message:String) : NullPointerException(message)


@ResponseStatus(value=HttpStatus.BAD_REQUEST)
class ConstrainViolationException(message:String) : Exception(message)
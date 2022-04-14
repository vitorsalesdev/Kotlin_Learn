package com.bank.account.controller

import com.bank.account.dto.AccountBalanceDto
import com.bank.account.dto.AccountDeactiveDto
import com.bank.account.dto.AccountDto
import com.bank.account.entity.Account
import com.bank.account.service.AccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/bank")
class AccountController(private val service: AccountService) {

    @PostMapping("/newaccount")
    fun createAccount(@RequestBody @Valid accountDto: AccountDto): ResponseEntity<Any> {
        if(service.existsByCpf(accountDto.cpf)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("There is already an account created using this CPF number")
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createAccount(accountDto))
    }

    @GetMapping("/account/id/{id}")
    fun findAccountById(@PathVariable(value = "id") id: Long): ResponseEntity<Any> {
        var account: Optional<Account> = service.findAccountById(id)
        if(!account.isPresent){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found")
        }
        return ResponseEntity.status(HttpStatus.OK).body(account.get())
    }

    @GetMapping("/account/cpf/{cpf}")
    fun findAccountByCpf(@PathVariable(value = "cpf") cpf: String): ResponseEntity<Any> {
        var account: Optional<Account> = service.findAccountByCpf(cpf)
        if(!account.isPresent){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found")
        }
        return ResponseEntity.status(HttpStatus.OK).body(account.get())
    }

    @PatchMapping("account/addbalance/{id}")
    fun addBalance(@PathVariable(value = "id") id: Long, @RequestBody accountAddBalance: AccountBalanceDto): ResponseEntity<Any> {
        var account: Optional<Account> = service.findAccountById(id)
        if(!account.isPresent){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found")
        }
        if(account.get().active == false){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Account is inactive")
        }
        return ResponseEntity.status(HttpStatus.OK).body(service.addBalance(account.get(), accountAddBalance))
    }
    @PatchMapping("account/withdrawbalance/{id}")
    fun withdrawBalance(@PathVariable(value = "id") id: Long, @RequestBody accountWithdrawBalance: AccountBalanceDto): ResponseEntity<Any> {
        var account: Optional<Account> = service.findAccountById(id)
        if(!account.isPresent){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found")
        }
        if(account.get().active == false){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Account is inactive")
        }
        if(account.get().balance!! < accountWithdrawBalance.balance){
            return ResponseEntity.status((HttpStatus.CONFLICT)).body("Insufficient balance to withdraw")
        }
        return ResponseEntity.status(HttpStatus.OK).body(service.withdrawBalance(account.get(), accountWithdrawBalance))
    }
    @PatchMapping("account/deactivate/{cpf}")
    fun deactivateAccount(@PathVariable(value = "cpf") cpf: String, @RequestBody accountDeactiveDto: AccountDeactiveDto): ResponseEntity<Any> {
        var account: Optional<Account> = service.findAccountByCpf(cpf)
        if(!account.isPresent){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found")
        }
        if(account.get().active == false){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Account is already inactive")
        }
        if(account.get().balance!! < 0){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("It is not possible to close an account with a negative balance")
        }
        if(account.get().balance!! > 0){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("It is not possible to close an account with a positive balance")
        }
        return ResponseEntity.status(HttpStatus.OK).body(service.deactiveAccountByCpf(account.get(), accountDeactiveDto))
    }



}
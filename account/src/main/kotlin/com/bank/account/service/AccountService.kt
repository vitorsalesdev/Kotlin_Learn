package com.bank.account.service

import com.bank.account.dto.AccountBalanceDto
import com.bank.account.dto.AccountDeactiveDto
import com.bank.account.dto.AccountDto
import com.bank.account.entity.Account
import com.bank.account.repository.AccountRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class AccountService(private val repository: AccountRepository) {

    fun createAccount(accountDto: AccountDto): Account {
        var account = Account(
            cpf = accountDto.cpf,
            name = accountDto.name,
            balance = 10.0
        )
        return repository.save(account)
    }

    fun existsByCpf(cpf: String): Boolean {
        return repository.existsByCpf(cpf)
    }

    fun findAccountById(id: Long): Optional<Account> {
        return repository.findById(id)
    }

    fun findAccountByCpf(cpf: String): Optional<Account> {
        return repository.findByCpf(cpf)
    }

    fun addBalance(account: Account, accountAddblance: AccountBalanceDto): Account {
        return repository.save(account.copy(balance =  account.balance?.plus(accountAddblance.balance)))
    }

    fun withdrawBalance(account: Account, accountWithdrawBalance: AccountBalanceDto): Account {
        return repository.save(account.copy(balance =  account.balance?.minus(accountWithdrawBalance.balance)))
    }

    fun deactiveAccountByCpf(account: Account, accountDeactiveDto: AccountDeactiveDto): Account {
        return repository.save(account.copy(active = false))
    }
}
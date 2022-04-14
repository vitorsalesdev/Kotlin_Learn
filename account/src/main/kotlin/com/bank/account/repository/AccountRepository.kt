package com.bank.account.repository

import com.bank.account.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountRepository : JpaRepository<Account, Long>{
    fun existsByCpf(cpf: String): Boolean
    fun findByCpf(cpf: String): Optional<Account>
}
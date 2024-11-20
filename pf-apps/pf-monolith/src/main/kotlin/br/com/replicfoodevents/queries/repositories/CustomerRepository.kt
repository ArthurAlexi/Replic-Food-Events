package br.com.replicfoodevents.queries.repositories

import br.com.replicfoodevents.queries.models.CustomerEntity
import org.springframework.data.repository.PagingAndSortingRepository

interface CustomerRepository : PagingAndSortingRepository<CustomerEntity, String>
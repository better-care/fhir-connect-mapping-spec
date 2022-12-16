package care.better.fhirconnectmappingspec

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FhirConnectMappingSpecApplication

fun main(args: Array<String>) {
    runApplication<FhirConnectMappingSpecApplication>(*args)
}

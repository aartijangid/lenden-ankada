package rt.com.n26challenge

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class N26ChallengeApplication

fun main(args: Array<String>) {
    runApplication<N26ChallengeApplication>(*args)
}

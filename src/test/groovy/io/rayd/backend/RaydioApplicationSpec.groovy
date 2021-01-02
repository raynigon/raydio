package io.rayd.backend

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles("test")
class RaydioApplicationSpec extends Specification{

    @Autowired
    RaydioBackendApplication application

    def 'setup context'(){
        expect:
        application != null
    }
}
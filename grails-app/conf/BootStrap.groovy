import com.cx.service.UtilityService
import grails.util.Environment

class BootStrap {
    UtilityService utilityService
    def init = { servletContext ->
        utilityService.createBootstrapData(Environment.current != Environment.DEVELOPMENT)
//        if(Environment.current != Environment.DEVELOPMENT) utilityService.loadDeviceData()
    }
    def destroy = {
    }
}

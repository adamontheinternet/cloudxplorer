import com.cx.service.ConfigurationService
import com.cx.service.UtilityService

class BootStrap {
    UtilityService utilityService
    def init = { servletContext ->
        utilityService.createBootstrapData()
    }
    def destroy = {
    }
}

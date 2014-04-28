package com.cx.service

import com.cx.domain.Vcenter
import com.vmware.vim25.mo.ServiceInstance
import grails.transaction.Transactional

@Transactional
class VcenterService {

    ConfigurationService configurationService
    VcenterConnectionService vcenterConnectionService

    private String buildVcenterUrl(String ip) {
        configurationService.getVcenterUrl().replace("\$ip", ip)
    }

    private ServiceInstance openConnection(Vcenter vcenter) {
        vcenterConnectionService.createOrGetShareableServiceInstance(buildVcenterUrl(vcenter.ip), vcenter.credential.username, vcenter.credential.password)
    }

    private void closeConnection(Vcenter vcenter) {
        vcenterConnectionService.destroyShareableServiceInstance(buildVcenterUrl(vcenter.ip), vcenter.credential.username)
    }

    public boolean verifyConnection(Vcenter vcenter) {
        try {
            ServiceInstance serviceInstance = openConnection(vcenter)
            String apiVersion = serviceInstance?.serviceContent?.about?.apiVersion?.trim()
            log.info "Connection established to vCenter $vcenter version $apiVersion"
            apiVersion != null
        } catch(Exception e) {
            log.error("Connection to vCenter ${vcenter.ip} failed $e")
            false
        } finally {
            closeConnection(vcenter)
        }

    }
}

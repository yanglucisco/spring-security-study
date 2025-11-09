package org.yanglu.spring.security.study.example.service;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Iterator;

// @Service
public class OAuth2ClientService {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    /**
     * 根据注册ID获取ClientRegistration
     */
    public ClientRegistration getClientRegistration(String registrationId) {
        // 方法一：如果知道确切的registrationId，可以直接获取
        return clientRegistrationRepository.findByRegistrationId(registrationId);

        // 方法二：遍历所有已配置的ClientRegistration（适用于不清楚所有ID的情况）
        // Iterable<ClientRegistration> clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        // Iterator<ClientRegistration> iterator = clientRegistrations.iterator();
        // while (iterator.hasNext()) {
        //     ClientRegistration registration = iterator.next();
        //     if (registrationId.equals(registration.getRegistrationId())) {
        //         return registration;
        //     }
        // }
        // return null; // 或抛出异常
    }
}

